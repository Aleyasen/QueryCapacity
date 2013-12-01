/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.retrieval;

import qcap.app.Constants;
import qcap.app.DBManager;
import qcap.app.query.Query;
import qcap.app.query.QueryResult;
import qcap.app.query.QueryStatement;
import qcap.app.query.QueryResultComparator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aleyase2-admin
 */
public class JoinIndex extends Index {

    Index core;
    List<Index> sides;

    @Override
    public List<QueryResult> retrieve(Query query, String ranking) {
        try {
            System.out.println("Retrieve JoinIndex:" + query.getText());
            Query subQueryCore = Query.generateSubQueries(query, core);
            System.out.println("Core SubQuery: " + subQueryCore);
            Map<Query, Index> sidesQueryMap = new HashMap<Query, Index>();
            for (Index sideInd : sides) {
                Query subQuerySide = Query.generateSubQueries(query, sideInd);
                if (subQuerySide != null) {
                    System.out.println("Side SubQuery: " + subQuerySide);
                    sidesQueryMap.put(subQuerySide, sideInd);
                } else {
                    System.out.println("Empty Side SubQuery for Index " + sideInd);
                }
            }
            if (ranking.equals(Constants.METHOD_PRMS)) {
                Double sum = 0.0;
                sum += subQueryCore.calcPRMS(core);
                for (Query subQuery : sidesQueryMap.keySet()) {
                    Index sideInd = sidesQueryMap.get(subQuery);
                    sum += subQuery.calcPRMS(sideInd);
                }
                subQueryCore.updateWeight(sum);
                for (Query subQuery : sidesQueryMap.keySet()) {
                    Index sideInd = sidesQueryMap.get(subQuery);
                    subQuery.updateWeight(sum);
                }
            } else if (ranking.equals(Constants.METHOD_IR_STYLE) || ranking.equals(Constants.METHOD_LM)) {
                subQueryCore.setAllWeight(1.0);
                for (Query subQuery : sidesQueryMap.keySet()) {
                    subQuery.setAllWeight(1.0);
                }
            }

            List<QueryResult> coreResults = core.retrieve(subQueryCore, ranking);
            System.out.println("Core Result#:" + coreResults.size());
            //List<QueryResult> coreResults = QueryResult.transformtoJQ(coreResultsQ);
            System.out.println("Execute Side Queries...");
            System.out.println("Side Queries#:" + sidesQueryMap.keySet().size());
            for (Query subQuery : sidesQueryMap.keySet()) {
                Index sideInd = sidesQueryMap.get(subQuery);
                List<QueryResult> sideResults = sideInd.retrieve(subQuery, ranking);
                System.out.println("Side Result");
                printQueryResult(sideResults, 100, "profession");
                List<QueryResult> joinResults = joinResult(coreResults, sideResults, sideInd);
                System.out.println("Join Result");
                Collections.sort(joinResults, new QueryResultComparator());

                printQueryResult(joinResults, 1000, "person");
                System.out.println("SubQuery:" + subQuery + " Index:" + sideInd);
                coreResults = joinResults;
                System.out.println("SideResults#:" + sideResults.size() + " JoinResult#:" + joinResults.size());
            }
            System.out.println("Scoring Query Results...");
            if (ranking.equals(Constants.METHOD_PRMS)) {
                for (QueryResult result : coreResults) {
                    Double score = 0.0;
                    for (QueryResult subresult : result.getIndvResults()) {
                        score += subresult.getScore() * subresult.getWeight();
                    }
                    result.setScore(score);
                }
            } else if (ranking.equals(Constants.METHOD_IR_STYLE) || ranking.equals(Constants.METHOD_LM)) {
                System.out.println("Number or Core Results:" + coreResults.size());
                for (QueryResult result : coreResults) {
                    int attrCount = 0;
                    printQueryResult(result, "person");
                    double totalScore = result.getScore();
                    System.out.println("core score"+result.getScore());
                    //calculate totalWeight
                    for (QueryResult subresult : result.getIndvResults()) {
                        System.out.println("subresult score: " + subresult.getScore());
                        totalScore += subresult.getScore();
                    }
                    //calculate number of attributes
                    attrCount += subQueryCore.getAttributesCount();
                    //System.out.println("");
                    for (Query subQuery : sidesQueryMap.keySet()) {
                        attrCount += subQuery.getAttributesCount();
                    }
                    System.out.println("totalScore:" + totalScore);
                    System.out.println("attrCount:" + attrCount);
                    double score = totalScore / attrCount;
                    System.out.println("Final Score:" + score);
                    result.setScore(score);
                    //  System.out.println("Attributes#:" + attrCount + " TotalScore:" + totalScore + " Score:" + score);
                }
            }
            Collections.sort(coreResults, new QueryResultComparator());
            printQueryResult(coreResults, 1000, "person");
            return coreResults;
        } catch (Exception ex) {
            System.out.println("Exception ON >>>>>>>>>>>> " + query);
            ex.printStackTrace();
        }
        return null;
    }

    public Index getCore() {
        return core;
    }

    public List<Index> getSides() {
        return sides;
    }

    public void addSideIndex(Index sideIndex) {
        if (sides == null) {
            sides = new ArrayList<Index>();
        }
        sides.add(sideIndex);
    }

    public void setCore(Index core) {
        this.core = core;
    }

    private List<QueryResult> joinResult(List<QueryResult> coreResults, List<QueryResult> sideResults, Index sideInd) {
        String sql = "";
        List<QueryResult> finalResult = new ArrayList<QueryResult>();
        try {
            System.out.println("coreResult#:" + coreResults.size());
            System.out.println("sideResult#:" + sideResults.size());
            Map<String, QueryResult> coreMap = new HashMap<String, QueryResult>();
            Map<String, QueryResult> sideMap = new HashMap<String, QueryResult>();
            for (QueryResult qr : coreResults) {
                coreMap.put(qr.getTupleId(), qr);
            }
            for (QueryResult qr : sideResults) {
                sideMap.put(qr.getTupleId(), qr);
            }
            sql = "SELECT " + sideInd.getPivot().getAttrId() + ", " + sideInd.getPivot().getAttrCore() + ", " + sideInd.getPivot().getAttrSide() + " from " + sideInd.getPivot().getTableName() + " where "
                    + sideInd.getPivot().getAttrCore() + " in (" + implode(coreResults) + ") and "
                    + sideInd.getPivot().getAttrSide() + " in (" + implode(sideResults) + ")";
            System.out.println("Running Query... [" + sql + "]");
            ResultSet rs = DBManager.execQuery(sql);
            int count = 0;
            while (rs.next()) {
                count++;
                String coreTupleId = rs.getString(sideInd.getPivot().getAttrCore());
                String sideTupleId = rs.getString(sideInd.getPivot().getAttrSide());
                QueryResult qr = coreMap.get(coreTupleId).clone();
                qr.addIndvResult(sideMap.get(sideTupleId));
                qr.setTupleId(rs.getString(sideInd.getPivot().getAttrId()));
                finalResult.add(qr);
            }
            System.out.println("Pivot#:" + count);
//            System.out.println("Sorting Results...");
            //  Collections.sort(finalResult, new JoinIndex.QueryResultComparator());
//            int rank = 0;
//            for (QueryResult qr : finalResult) {
//                qr.setRank(rank);
//                rank++;
//            }
//            System.out.println("Done");
            return finalResult;
        } catch (SQLException ex) {
            System.out.println("Exception SQL ON >>>>>>>>>>>> " + sql);
            ex.printStackTrace();
        }
        return null;
    }

    public String implode(List<QueryResult> list) {
        StringBuilder sbuilder = new StringBuilder();
        for (QueryResult qr : list) {
            sbuilder.append(qr.getTupleId());
            sbuilder.append(" ,");
        }
        String str = sbuilder.toString();
        if (str.length() > 1) {
            return str.substring(0, str.length() - 1);
        } else {
            return str;
        }
    }

    private void printQueryResult(List<QueryResult> results, int max, String type) {
        for (QueryResult qr : results) {
            max--;
            if (max == 0) {
                break;
            }
            printQueryResult(qr, type);
        }
    }

    private void printQueryResult(QueryResult result, String type) {
        System.out.println(DBManager.getName(result.getFbid(), type) + result);
        for (QueryResult indv : result.getIndvResults()) {
            System.out.println("Indv: " + DBManager.getName(indv.getFbid(), "profession") + indv);

        }
    }
}

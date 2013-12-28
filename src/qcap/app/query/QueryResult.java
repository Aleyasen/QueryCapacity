/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.query;

import qcap.app.DBManager;
import qcap.app.utils.IndexUtil;
import qcap.app.retrieval.BaseIndex;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aleyase2
 */
public class QueryResult {

    public static void printQueryResult(QueryResult result, String type) {
        System.out.println(DBManager.getName(result.getFbid(), type) + result);
        for (QueryResult indv : result.getIndvResults()) {
            System.out.println("Indv: " + DBManager.getName(indv.getFbid(), "profession") + indv);
        }
    }

    public static void printQueryResult(List<QueryResult> results, int max, String type) {
        for (QueryResult qr : results) {
            max--;
            if (max == 0) {
                break;
            }
            printQueryResult(qr, type);
        }
    }

    private List<QueryResult> indvResults;
    private String tupleId;
    private String queryId;
    private String fbid;
    private Double score;
    private Double weight;
    private Integer rank;
    private Integer sourceCollection;

    public QueryResult() {
        indvResults = new ArrayList<QueryResult>();
    }

    public Integer getSourceCollection() {
        return sourceCollection;
    }

    public void setSourceCollection(Integer sourceCollection) {
        this.sourceCollection = sourceCollection;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public List<QueryResult> getIndvResults() {
        return indvResults;
    }

    public void addIndvResult(QueryResult indvResult) {
        if (indvResult == null) {
            indvResults = new ArrayList<QueryResult>();
        }
        indvResults.add(indvResult);
    }

    public void setIndvResults(List<QueryResult> indvResults) {
        this.indvResults = indvResults;
    }

//    public static List<JoinQueryResult> transformtoJQ(List<QueryResult> results) {
//        List<JoinQueryResult> jqresults = new ArrayList<>();
//        for (QueryResult qr : results) {
//            jqresults.add(transformtoJQ(qr));
//        }
//        return jqresults;
//    }
//    public static JoinQueryResult transformtoJQ(QueryResult result) {
//        JoinQueryResult jqresutl = new JoinQueryResult();
//        jqresutl.setFbid(result.getFbid());
//        jqresutl.setIndvResults(new ArrayList<QueryResult>());
//        jqresutl.setQueryId(result.getQueryId());
//        jqresutl.setRank(result.getRank());
//        jqresutl.setScore(result.getScore());
//        jqresutl.setTupleId(result.getTupleId());
//        return jqresutl;
//    }
    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getTupleId() {
        return tupleId;
    }

    public void setTupleId(String tupleId) {
        this.tupleId = tupleId;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return " QueryResult{" + "tupleId=" + tupleId + ", queryId=" + queryId + ", fbid=" + fbid + ", score=" + score + ", rank=" + rank + "}\n";
    }

    @Override
    public QueryResult clone(){
        QueryResult newObj = new QueryResult();
        newObj.fbid = this.fbid;
        newObj.queryId = this.queryId;
        newObj.rank = this.rank;
        newObj.score = this.score;
        newObj.sourceCollection = this.sourceCollection;
        newObj.tupleId = this.tupleId;
        newObj.weight = this.weight;
        return newObj;
    }
    
    

    public static List<QueryResult> parseOutput(BaseIndex index, String output, Query objQ) {
        List<QueryResult> results = new ArrayList<QueryResult>();
        String[] lines = output.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i] != null && lines[i].length() > 0) {
                String[] token = lines[i].split("\\s+");
                if (token.length == 6) {
                    try {
                        QueryResult qresult = new QueryResult();
                        qresult.setQueryId(token[1]);
                        qresult.setTupleId(token[2]);
                        qresult.setRank(Integer.parseInt(token[3]));
                        qresult.setScore(Double.parseDouble(token[4]));
                        qresult.setWeight(objQ.getTotalWeight());
                        ResultSet rs = DBManager.execQuery(String.format(index.getIdQuery(), qresult.getTupleId()));
                        while (rs.next()) {
                            qresult.setFbid(rs.getString("fbid"));
                        }
                        results.add(qresult);
                    } catch (SQLException ex) {
                        Logger.getLogger(IndexUtil.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Token size in invalid:" + token.length);
                }
            }
        }
        return results;
    }
}

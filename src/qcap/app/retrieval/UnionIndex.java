/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.retrieval;

import qcap.app.Constants;
import qcap.app.query.Query;
import qcap.app.query.QueryStatement;
import qcap.app.query.QueryResult;
import qcap.app.query.QueryResultComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import qcap.app.DBManager;

/**
 *
 * @author aleyase2-admin
 */
public class UnionIndex extends Index {

    List<Index> children;
    final static double b = 0.4;

    public List<Index> getChildren() {
        return children;
    }

    public void setChildren(List<Index> children) {
        this.children = children;
    }

    @Override
    public Long getDF(String fieldName, String term) {
        Long df = 0L;
        for (Index ind : children) {
            df += ind.getDF(fieldName, term);
        }
        return df;
    }

    @Override
    public Long getWordCount(String fieldname) {
        Long wc = 0L;
        for (Index ind : children) {
            wc += ind.getWordCount(fieldname);
        }
        return wc;
    }

    @Override
    public Long getVocabCount(String fieldname) {
        Long vc = 0L;
        for (Index ind : children) {
            vc += ind.getVocabCount(fieldname);
        }
        return vc;
    }

    @Override
    public List<QueryResult> retrieve(Query query, String ranking) {
        List<QueryResult> allResults = new ArrayList<QueryResult>();
        System.out.println("Retrieve UnionIndex[children#:" + children.size() + "]" + query.getText());
        int count = 1;
        for (Index ind : children) {
            Query subQuery = Query.generateSubQueries(query, ind);
            System.out.println("child" + count + " : " + subQuery.getAttributesList());
            List<QueryResult> results = ind.retrieve(subQuery, ranking);
            int limit = 10;
            System.out.println("Result for child #" + count + ": (Before Normalization)");
            for (QueryResult qr : results) {
                if (limit == 0) {
                    break;
                }
                limit--;
                System.out.println(qr.getQueryId() + " " + qr.getScore() + " " + qr.getFbid() + " " + DBManager.getPersonName(qr.getFbid()));
            }
            double cNorm = getNormCollectionScore(subQuery, ind);
            double dMin = getMinScore(results);
            double dMax = getMaxScore(results);
            System.out.println("cNorm=" + cNorm + " dMin=" + dMin + " dMax=" + dMax);
            for (QueryResult result : results) {
                double dNorm = (result.getScore() - dMin) * 1.0 / (dMax - dMin);
                double score = (dNorm + (0.4 * cNorm * dNorm)) * 1.0 / 1.4;
                result.setScore(score);
                result.setSourceCollection(count);
            }
            allResults.addAll(results);
            Collections.sort(allResults, new QueryResultComparator());
            System.out.println("Union#: " + allResults.size());
            limit = 10;
            System.out.println("Result for child #" + count + ":");
            for (QueryResult qr : results) {
                if (limit == 0) {
                    break;
                }
                limit--;
                System.out.println(qr.getQueryId() + " " + qr.getScore() + " " + qr.getFbid() + " " + DBManager.getPersonName(qr.getFbid()));
            }
            count++;
        }
        int limit = 20;
        System.out.println("Final Merged Result:");
        for (QueryResult qr : allResults) {
            if (limit == 0) {
                break;
            }
            limit--;
            System.out.println(qr.getQueryId() + " " + qr.getScore() + " " + qr.getFbid() + " " + DBManager.getPersonName(qr.getFbid()));
        }
        return allResults;
    }

    public double getNormCollectionScore(Query q, Index chInd) {
        double scoreSum = 0;
        double Rmax = 0;
        double Rmin = b;
        for (QueryStatement qs : q.getStatements()) {
            double I = calcI(qs);
            double T = calcT(qs, chInd);
            scoreSum += b + ((1 - b) * T * I);
            Rmax += b + ((1 - b) * 1 * I);
        }
        double score = scoreSum * 1.0 / q.getStatements().size();
        Rmax = Rmax * 1.0 / q.getStatements().size();
        double normScore = (score - Rmin) * 1.0 / (Rmax - Rmin);
        return normScore;
    }

    private double calcI(QueryStatement qs) {
        int cf = 0;
        for (Index ind : children) {
            Long df = ind.getDF(Constants.attribute.get(qs.getAttribute()), qs.getValue());
            if (df != null && df > 0) {
                cf++;
            }
        }
        int C = children.size();
        double result = (Math.log((C + 0.5) / (cf))) * 1.0 / (Math.log(C + 1));
        return result;
    }

    private double calcT(QueryStatement qs, Index ind) {
        String fieldName = Constants.attribute.get(qs.getAttribute());
        Long df = ind.getDF(fieldName, qs.getValue());
        Long cw = ind.getVocabCount(fieldName);
        Long sum_cw = 0L;
        for (Index chInd : children) {
            sum_cw += chInd.getVocabCount(fieldName);
        }
        Double avg_cw = sum_cw * 1.0 / children.size();
        Double result = df * 1.0 / (df + 50 + (150 * (cw * 1.0 / avg_cw)));
        return result;
    }

    private double getMinScore(List<QueryResult> results) {
        double min = Double.MAX_VALUE;
        for (QueryResult qr : results) {
            if (qr.getScore() < min) {
                min = qr.getScore();
            }
        }
        return min;
    }

    private double getMaxScore(List<QueryResult> results) {
        double max = -1 * Double.MAX_VALUE;
        for (QueryResult qr : results) {
            if (qr.getScore() > max) {
                max = qr.getScore();
            }
        }
        return max;
    }
}

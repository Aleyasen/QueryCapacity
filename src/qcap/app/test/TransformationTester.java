/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import qcap.app.Constants;
import qcap.app.query.Query;
import qcap.app.query.QueryResult;
import qcap.app.retrieval.Index;
import qcap.app.utils.CSVWriter;
import qcap.app.utils.ScoringUtil;

/**
 *
 * @author aleyase2
 */
public class TransformationTester {

    public static double execBatchQueries(Collection<Query> queries, Index index, String resultFilePath) {
        printCurrentTime();
        CSVWriter writer = new CSVWriter(resultFilePath);
        CSVWriter writerRej = new CSVWriter(resultFilePath + ".reject");

        int count = 0;
        int rejected = 0;
        double precision_all_3 = 0;
        double precision_all_5 = 0;
        double precision_all_10 = 0;
        double mrr_all = 0;
        System.out.println("Queries#: " + queries.size());
        int countQ = 0;
        for (Query q : queries) {
            long time1 = System.currentTimeMillis();
            System.out.println("Current Q:" + q);
            //countQ++;
            //writer.append(countQ + ": " + q.getId());
            List<QueryResult> results = index.retrieve(q, Constants.METHOD_LM);
            if (results != null) {
                count++;
                System.out.println("Desired: " + q.getEntityId() + " " + q.getFbid() + " " + q.getId());
                Integer sourceCollection = null;
                QueryResult.printQueryResult(results, 30, "tv_program");
                for (QueryResult qr : results) {
                    if (qr.getFbid().equals(q.getFbid())) {
                        sourceCollection = qr.getSourceCollection();
                        break;
                    }
                }
                String sourceCollectionStr = "N/A";
                if (sourceCollection != null) {
                    sourceCollectionStr = sourceCollection + "";
                }
                final double precisionAt3 = ScoringUtil.precisionAtK(results, q, 3);
                precision_all_3 += precisionAt3;
                final double precisionAt5 = ScoringUtil.precisionAtK(results, q, 5);
                precision_all_5 += precisionAt5;
                final double precisionAt10 = ScoringUtil.precisionAtK(results, q, 10);
                precision_all_10 += precisionAt10;

                final double MRR = ScoringUtil.MRR(results, q);
                mrr_all += MRR;
                System.out.println("#" + count);
                System.out.println("Query:" + q.getStatements());
                System.out.println("Results#:" + results.size());
                System.out.println("P@3:" + precisionAt3);
                System.out.println("P@5:" + precisionAt5);
                System.out.println("P@10:" + precisionAt10);
                System.out.println("MRR:" + MRR);
                System.out.println();
                System.out.println("Precision3-UpToHere:" + precision_all_3 * 1.00 / count);
                System.out.println("Precision5-UpToHere:" + precision_all_5 * 1.00 / count);
                System.out.println("Precision5-UpToHere:" + precision_all_10 * 1.00 / count);
                System.out.println("MRR-UpToHere:" + mrr_all * 1.00 / count);
                System.out.println("SourceCollection:" + sourceCollectionStr);
                writer.append(q.getId() + "," + precisionAt3 + "," + precisionAt5 + "," + precisionAt10 + "," + MRR + "," + sourceCollectionStr);
            } else {
                System.out.println("Query Hasn't any result!");
                writerRej.append(q.getId() + "," + -1 + "," + -1 + "," + -1);
                rejected++;
            }
            long time2 = System.currentTimeMillis();
            System.out.println("Query Running Time (ms):" + (time2 - time1));
        }
        writer.close();
        writerRej.close();
        System.out.println("Count-All:" + count);
        System.out.println("Reject-All:" + rejected);
        System.out.println("Precision-All:" + precision_all_3 * 1.00 / count);
        System.out.println("MRR-All:" + mrr_all * 1.00 / count);
        printCurrentTime();
        return mrr_all * 1.00 / count;
    }

    private static void printCurrentTime() {
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String dateFormatted = formatter.format(date);
        System.out.println("Current Time: " + dateFormatted);
    }
}

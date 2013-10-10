/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.evaluation;

import qcap.app.AppConfig;
import qcap.app.utils.CSVWriter;
import qcap.app.query.Query;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aleyase2
 */
public class EvalResults {

    public static void main(String[] args) {
        // String filePath = AppConfig.RESULT_DIR + "Person-LM-10-2-2013.txt";
        //String filePath = AppConfig.RESULT_DIR + "TV-LM-10-3-2013-2.txt";
        //String filePath = AppConfig.RESULT_DIR + "Person-LM-CORI-US-10-3-2013.txt";
        String filePath = AppConfig.RESULT_DIR + "Book-LM-10-3-2013.txt";
        String difficultFilePath = AppConfig.RESULT_DIR + "Book-LM-10-3-2013-difficult.txt";

        //String difficultFilePath = AppConfig.RESULT_DIR + "Person-LM-10-2-2013-difficult.txt";
        //String difficultFilePath = AppConfig.RESULT_DIR + "TV-LM-10-3-2013-2-difficult.txt";

        evalResultFile(difficultFilePath);
        //extractDifficultQueries(filePath, difficultFilePath, 0.3);
    }

    public static void extractDifficultQueries(String filePath, String diffQfilePath, double MRRThreshold) {
        BufferedReader br = null;
        CSVWriter writer = new CSVWriter(diffQfilePath);
        int countdiffQ = 0;
        try {
            br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine();
            while (line != null) {
                if (line.startsWith("*")) {
                    line = br.readLine();
                    continue;
                }

                String split[] = line.split(",");
                Integer qId = Integer.parseInt(split[0]);
                if (Double.parseDouble(split[1]) == -1) {
                    line = br.readLine();
                    continue;
                }
                double prec3 = Double.parseDouble(split[1]);
                double prec5 = Double.parseDouble(split[2]);
                double mrr = Double.parseDouble(split[3]);
                if (mrr < MRRThreshold && mrr != 0) {
                    writer.append(line);
                    countdiffQ++;
                }

                line = br.readLine();
            }
            System.out.println("#DiffQ: " + countdiffQ);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(EvalResults.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void evalResultFile(String filePath) {
        BufferedReader br = null;
        Double precision_5_sum = 0.0;
        Double precision_3_sum = 0.0;
        Double MRR_sum = 0.0;
        int count = 0;

        Double dist_precision_5_sum = 0.0;
        Double dist_precision_3_sum = 0.0;
        Double dist_MRR_sum = 0.0;
        int dist_count = 0;

        int zeroMRR = 0;

        Collection<Query> queries = Query.findAll();
        Map<Integer, Query> queryMap = new HashMap<>();
        for (Query q : queries) {
            queryMap.put(q.getId(), q);
        }

        try {
            br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine();
            while (line != null) {
                if (line.startsWith("*")) {
                    line = br.readLine();
                    continue;
                }

                String split[] = line.split(",");
                Integer qId = Integer.parseInt(split[0]);
                if (Double.parseDouble(split[1]) == -1) {
                    line = br.readLine();
                    continue;
                }
                Query q = null;
                if (queryMap.containsKey(qId)) {
                    q = queryMap.get(qId);
                } else {
                    System.out.println("qId:" + qId + " not available in queryMap");
                }
                double prec3 = Double.parseDouble(split[1]);
                double prec5 = Double.parseDouble(split[2]);
                double mrr = Double.parseDouble(split[3]);

                precision_3_sum += prec3 * q.getFrequency();
                precision_5_sum += prec5 * q.getFrequency();
                MRR_sum += mrr * q.getFrequency();
                count += q.getFrequency();
                dist_precision_3_sum += prec3;
                dist_precision_5_sum += prec5;
                dist_MRR_sum += mrr;
                dist_count++;
                if (mrr == 0) {
                    zeroMRR++;
                }
                line = br.readLine();
            }
            System.out.println("P@3: " + precision_3_sum / count);
            System.out.println("P@5: " + precision_5_sum / count);
            System.out.println("MRR: " + MRR_sum / count);
            System.out.println("Count: " + count);
            System.out.println("");
            System.out.println("DP@3: " + dist_precision_3_sum / dist_count);
            System.out.println("DP@5: " + dist_precision_5_sum / dist_count);
            System.out.println("DMRR: " + dist_MRR_sum / dist_count);
            System.out.println("DCount: " + dist_count);
            System.out.println("");
            System.out.println("ZMRR: " + zeroMRR);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(EvalResults.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

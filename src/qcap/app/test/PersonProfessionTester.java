/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import qcap.app.AppConfig;
import qcap.app.Constants;
import qcap.app.query.Query;
import qcap.app.query.QueryResult;
import qcap.app.retrieval.BaseIndex;
import qcap.app.retrieval.Index;
import qcap.app.retrieval.JoinIndex;
import qcap.app.retrieval.PivotTable;
import qcap.app.utils.CSVWriter;
import qcap.app.utils.ScoringUtil;

/**
 *
 * @author aleyase2
 */
public class PersonProfessionTester {
    
    public static List<Integer> all_queries;
    public static List<Integer> art_queries;
    public static List<Integer> sport_queries;
    public static List<Integer> other_queries;
    
    static {
        all_queries = Arrays.asList(48566, 43709, 44883, 48492, 49527, 43415,
                49469, 43667, 47457, 44787, 45505, 46333, 49471, 44601, 47654,
                48129, 49285, 49904, 48511, 48558, 45329, 48500, 46267, 49062,
                47429, 48686, 48726, 43413, 44174, 49930, 48505, 49395, 50136,
                50309, 48476, 44847, 46132, 49395, 43862, 47483, 49396, 49721,
                50032, 44174, 45042, 46045, 43304, 47326, 48587, 44444, 47805,
                47893, 45967);
        art_queries = Arrays.asList(48566, 43709, 44883, 48492, 49527, 43415,
                49469, 43667, 47457, 44787, 45505, 46333, 49471, 44601, 47654,
                48129, 49285, 49904, 48511, 48558, 45329, 48500);
        
        sport_queries = Arrays.asList(43304, 47326, 48587, 44444, 47805,
                47893, 45967);
        
        other_queries = Arrays.asList(46267, 49062,
                47429, 48686, 48726, 43413, 44174, 49930, 48505, 49395, 50136,
                50309, 48476, 44847, 46132, 49395, 43862, 47483, 49396, 49721,
                50032, 44174, 45042, 46045);
    }
    
    public static void main(String[] args) {
          testPersonAll("person_all_profession_test_2.txt");
      //  testProfessionPerson("person_art_sport_other.txt");
    }
    
    public static void testProfessionPerson(String resultFile) {
        Collection<Query> artOrgQueries = Query.findById(art_queries);
        Collection<Query> sportOrgQueries = Query.findById(sport_queries);
        Collection<Query> otherOrgQueries = Query.findById(other_queries);
        
        Collection<Query> artQueries = Query.dropStatement(artOrgQueries, "profession");
        Collection<Query> sportQueries = Query.dropStatement(sportOrgQueries, "profession");
        Collection<Query> otherQueries = Query.dropStatement(otherOrgQueries, "profession");
        
        BaseIndex artIndex = new BaseIndex(Constants.VI_PERSON_ART);
        artIndex.setAcceptList(Arrays.asList("person_name", "person_description"));
        
        BaseIndex sportIndex = new BaseIndex(Constants.VI_PERSON_SPORT);
        sportIndex.setAcceptList(Arrays.asList("person_name", "person_description"));
        
        BaseIndex otherIndex = new BaseIndex(Constants.VI_PERSON_PROFESSION_OTHER);
        otherIndex.setAcceptList(Arrays.asList("person_name", "person_description"));
        
        execBatchQueries(artQueries, artIndex, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(sportQueries, sportIndex, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(otherQueries, otherIndex, AppConfig.RESULT_DIR + resultFile);
    }
    
    public static void testPersonAll(String resultFile) {
        
        Collection<Query> queries = Query.findById(all_queries);
        //Collection<Query> queries = Query.findById(48476);
        
        
        System.out.println(queries.size() + " query fetched for execute");
        JoinIndex index = new JoinIndex();

        //Add Core Index - Person
        BaseIndex personIndex = new BaseIndex(Constants.TBL_PERSON);
        personIndex.setAcceptList(Arrays.asList("person_name", "person_description"));
        index.setCore(personIndex);

        //Add Side Index - Profession
        BaseIndex profIndex = new BaseIndex(Constants.TBL_PROFESSION);
        profIndex.setAcceptList(Arrays.asList("profession"));
        profIndex.setPivot(PivotTable.PERSON_PROFESSION_PIVOT);
        index.addSideIndex(profIndex);
        
        execBatchQueries(queries, index, AppConfig.RESULT_DIR + resultFile);
    }
    
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

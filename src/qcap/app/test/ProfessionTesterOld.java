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
public class ProfessionTesterOld extends TransformationTester {

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

    }

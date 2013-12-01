/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import qcap.app.AppConfig;
import qcap.app.Constants;
import qcap.app.query.Query;
import qcap.app.retrieval.BaseIndex;
import qcap.app.retrieval.JoinIndex;
import qcap.app.retrieval.PivotTable;

/**
 *
 * @author aleyase2-admin
 */
public class AllEducationTester extends TransformationTester {

    public static List<Integer> all_queries;

    static {
        all_queries = Arrays.asList(
                44390, 44488, 45150, 45603, 45652, 45860, 47321, 48965, 49882, 49978);
    }

    public static void main(String[] args) {
//        testEducationMultipleTable("person_education_multiple.txt");
        testEducationSingleTable("person_education_single_4000.txt");
    }

    public static void testEducationMultipleTable(String resultFile) {

        Collection<Query> hong_kong_college_of_medicine_for_chinese_queries = Query.dropStatement(Query.findById(Arrays.asList(44390)), "education");
        Collection<Query> university_of_kentucky_queries = Query.dropStatement(Query.findById(Arrays.asList(44488)), "education");
        Collection<Query> st_vincent_st_mary_high_school_queries = Query.dropStatement(Query.findById(Arrays.asList(45150)), "education");
        Collection<Query> the_boston_conservatory_queries = Query.dropStatement(Query.findById(Arrays.asList(45603)), "education");
        Collection<Query> louisiana_state_university_queries = Query.dropStatement(Query.findById(Arrays.asList(45652)), "education");
        Collection<Query> lower_merion_high_school_queries = Query.dropStatement(Query.findById(Arrays.asList(45860)), "education");
        Collection<Query> university_of_alabama_queries = Query.dropStatement(Query.findById(Arrays.asList(47321)), "education");
        Collection<Query> north_fort_myers_high_school_queries = Query.dropStatement(Query.findById(Arrays.asList(48965)), "education");
        Collection<Query> university_of_california_los_angeles_queries = Query.dropStatement(Query.findById(Arrays.asList(49882)), "education");
        Collection<Query> south_side_high_school_queries = Query.dropStatement(Query.findById(Arrays.asList(49978)), "education");

        BaseIndex hong_kong_college_of_medicine_for_chinese_index = new BaseIndex(Constants.VI_PERSON_EDU_HONG_KONG_COLLEGE_OF_MEDICINE_FOR_CHINESE);
        hong_kong_college_of_medicine_for_chinese_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex university_of_kentucky_index = new BaseIndex(Constants.VI_PERSON_EDU_UNIVERSITY_OF_KENTUCKY);
        university_of_kentucky_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex st_vincent_st_mary_high_school_index = new BaseIndex(Constants.VI_PERSON_EDU_ST_VINCENT_ST_MARY_HIGH_SCHOOL);
        st_vincent_st_mary_high_school_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex the_boston_conservatory_index = new BaseIndex(Constants.VI_PERSON_EDU_THE_BOSTON_CONSERVATORY);
        the_boston_conservatory_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex louisiana_state_university_index = new BaseIndex(Constants.VI_PERSON_EDU_LOUISIANA_STATE_UNIVERSITY);
        louisiana_state_university_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex lower_merion_high_school_index = new BaseIndex(Constants.VI_PERSON_EDU_LOWER_MERION_HIGH_SCHOOL);
        lower_merion_high_school_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex university_of_alabama_index = new BaseIndex(Constants.VI_PERSON_EDU_UNIVERSITY_OF_ALABAMA);
        university_of_alabama_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex north_fort_myers_high_school_index = new BaseIndex(Constants.VI_PERSON_EDU_NORTH_FORT_MYERS_HIGH_SCHOOL);
        north_fort_myers_high_school_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex university_of_california_los_angeles_index = new BaseIndex(Constants.VI_PERSON_EDU_UNIVERSITY_OF_CALIFORNIA_LOS_ANGELES);
        university_of_california_los_angeles_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex south_side_high_school_index = new BaseIndex(Constants.VI_PERSON_EDU_SOUTH_SIDE_HIGH_SCHOOL);
        south_side_high_school_index.setAcceptList(Arrays.asList("person_name", "person_description"));


        execBatchQueries(hong_kong_college_of_medicine_for_chinese_queries, hong_kong_college_of_medicine_for_chinese_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(university_of_kentucky_queries, university_of_kentucky_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(st_vincent_st_mary_high_school_queries, st_vincent_st_mary_high_school_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(the_boston_conservatory_queries, the_boston_conservatory_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(louisiana_state_university_queries, louisiana_state_university_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(lower_merion_high_school_queries, lower_merion_high_school_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(university_of_alabama_queries, university_of_alabama_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(north_fort_myers_high_school_queries, north_fort_myers_high_school_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(university_of_california_los_angeles_queries, university_of_california_los_angeles_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(south_side_high_school_queries, south_side_high_school_index, AppConfig.RESULT_DIR + resultFile);

    }

    public static void testEducationSingleTable(String resultFile) {

        Collection<Query> queries = Query.findById(all_queries);
        //Collection<Query> queries = Query.findById(48476);


        System.out.println(queries.size() + " query fetched for execute");
        JoinIndex index = new JoinIndex();

        //Add Core Index - Person
        BaseIndex personIndex = new BaseIndex(Constants.TBL_PERSON);
        personIndex.setAcceptList(Arrays.asList("person_name", "person_description"));
        index.setCore(personIndex);

        //Add Side Index - Education
        BaseIndex eduIndex = new BaseIndex(Constants.TBL_EDUCATION);
        eduIndex.setAcceptList(Arrays.asList("education"));
        eduIndex.setPivot(PivotTable.PERSON_EDUCATION_PIVOT);
        index.addSideIndex(eduIndex);

        execBatchQueries(queries, index, AppConfig.RESULT_DIR + resultFile);
    }
}

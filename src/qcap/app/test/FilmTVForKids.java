/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.test;

import java.util.ArrayList;
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
public class FilmTVForKids extends TransformationTester {

    public static List<Integer> q_desired_tv_contain_tv;
    public static List<Integer> q_desired_tv_not_contain_tv;
    public static List<Integer> q_desired_film_contain_film;
    public static List<Integer> q_desired_film_not_contain_film;

    static {
        q_desired_tv_contain_tv = Arrays.asList(43254, 44178, 44757, 45264, 45990, 46448,
                46733, 48395, 49034, 49119, 49253, 49969, 50321);
        q_desired_film_not_contain_film = Arrays.asList(44882, 50041, 43019, 43267, 43580,
                45110, 45192, 45347, 45516, 45596, 47052, 47059, 48368, 48442, 48788,
                48964, 49375, 49512, 49708, 49730, 49800, 49863, 48494);
        q_desired_film_contain_film = Arrays.asList(44882, 50041);
        q_desired_tv_not_contain_tv = Arrays.asList(43891, 44456, 45755, 47936, 44384, 45123, 46367, 47030,
                48640, 48685, 44124, 46079, 46551, 47906, 49189);
    }

    public static void main(String[] args) {
//        testSourceSchema("tv__film_for_kids_source_schema_1.txt");
        testTargetSchema("tv_film_for_kids_target_schema_1.txt");
    }

    public static void testSourceSchema(String resultFile) {
        Collection<Query> q_desired_tv_contain_tv_obj = Query.findById(q_desired_tv_contain_tv);
        Collection<Query> q_desired_tv_not_contain_tv_obj = Query.findById(q_desired_tv_not_contain_tv);
        Collection<Query> q_desired_film_not_contain_film_obj = Query.findById(q_desired_film_not_contain_film);
        Collection<Query> q_desired_film_contain_film_obj = Query.findById(q_desired_film_contain_film);

//        Collection<Query> queries = Query.findById(47654);
        List<String> filmTVacceptList = Arrays.asList("tv_program_name", "tv_program_description", "film_name", "film_description");

        BaseIndex tv_program_index = new BaseIndex(Constants.TBL_TV_PROGRAM_W_TYPE);
        tv_program_index.setAcceptList(filmTVacceptList);

        BaseIndex film_index = new BaseIndex(Constants.TBL_FILM_W_TYPE);
        film_index.setAcceptList(filmTVacceptList);

        execBatchQueries(q_desired_tv_contain_tv_obj, tv_program_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_desired_tv_not_contain_tv_obj, tv_program_index, AppConfig.RESULT_DIR + resultFile);

        execBatchQueries(q_desired_film_contain_film_obj, film_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_desired_film_not_contain_film_obj, film_index, AppConfig.RESULT_DIR + resultFile);

    }

    public static void testTargetSchema(String resultFile) {

        Collection<Query> q_desired_tv_contain_tv_obj = Query.findById(q_desired_tv_contain_tv);
        Collection<Query> q_desired_tv_not_contain_tv_obj = Query.findById(q_desired_tv_not_contain_tv);
        Collection<Query> q_desired_film_not_contain_film_obj = Query.findById(q_desired_film_not_contain_film);
        Collection<Query> q_desired_film_contain_film_obj = Query.findById(q_desired_film_contain_film);

//        Collection<Query> queries = Query.findById(47654);
        List<String> filmTVacceptList = Arrays.asList("tv_program_name", "tv_program_description", "film_name", "film_description");

        BaseIndex tv_program_for_kids_union_other_tv_program_index = new BaseIndex(Constants.TBL_TV_PROGRAM_FOR_KIDS_UNION_OTHER_TV_PROGRAM);
        tv_program_for_kids_union_other_tv_program_index.setAcceptList(filmTVacceptList);

        BaseIndex tv_program_not_for_kids_index = new BaseIndex(Constants.TBL_TV_PROGRAM_NOT_FOR_KIDS);
        tv_program_not_for_kids_index.setAcceptList(filmTVacceptList);

        BaseIndex film_for_kids_union_other_film_index = new BaseIndex(Constants.TBL_FILM_FOR_KIDS_UNION_OTHER_FILM);
        film_for_kids_union_other_film_index.setAcceptList(filmTVacceptList);

        BaseIndex film_not_for_kids_index = new BaseIndex(Constants.TBL_FILM_NOT_FOR_KIDS);
        film_not_for_kids_index.setAcceptList(filmTVacceptList);

        execBatchQueries(q_desired_tv_contain_tv_obj, tv_program_for_kids_union_other_tv_program_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_desired_tv_not_contain_tv_obj, tv_program_not_for_kids_index, AppConfig.RESULT_DIR + resultFile);

        execBatchQueries(q_desired_film_contain_film_obj, film_for_kids_union_other_film_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_desired_film_not_contain_film_obj, film_not_for_kids_index, AppConfig.RESULT_DIR + resultFile);

    }
}

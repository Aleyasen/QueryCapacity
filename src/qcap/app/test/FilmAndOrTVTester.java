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
public class FilmAndOrTVTester extends TransformationTester {

    public static List<Integer> tv_program_queries;
    public static List<Integer> film_queries;

    static {
        tv_program_queries = Arrays.asList(43254, 44178, 44757, 45264, 45990, 46448, 46733, 48395, 48494,
                49034, 49119, 49253, 49969, 50321, 44124, 46079, 46551, 47906, 49189);
        film_queries = Arrays.asList(43891, 44456, 44882, 45755, 47936, 50041, 43019, 43267, 43580, 44384,
                45110, 45123, 45192, 45347, 45516, 45596, 46367, 47030, 47052, 47059, 48368, 48442, 48640,
                48685, 48788, 48964, 49375, 49512, 49708, 49730, 49800, 49863);
    }

    public static void main(String[] args) {
//        testSourceSchema("tv_and_or_film_source_schema_4.txt");
        testTargetSchema("tv_and_or_film_target_schema_5.txt");
    }

    public static void testSourceSchema(String resultFile) {

        Collection<Query> tv_program_queries_obj = Query.findById(tv_program_queries);
        Collection<Query> film_queries_obj = Query.findById(film_queries);
//        Collection<Query> queries = Query.findById(47654);

        BaseIndex tv_program_index = new BaseIndex(Constants.TBL_TV_PROGRAM_W_TYPE);
        tv_program_index.setAcceptList(Arrays.asList("tv_program_name", "tv_program_description"));
        BaseIndex film_index = new BaseIndex(Constants.TBL_FILM_W_TYPE);
        film_index.setAcceptList(Arrays.asList("film_name", "film_description"));

        execBatchQueries(tv_program_queries_obj, tv_program_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(film_queries_obj, film_index, AppConfig.RESULT_DIR + resultFile);

    }

    public static void testTargetSchema(String resultFile) {

        Collection<Query> tv_program_queries_obj = Query.findById(tv_program_queries);
        Collection<Query> film_queries_obj = Query.findById(film_queries);
//        Collection<Query> queries = Query.findById(47654);

        BaseIndex tv_program_union_tv_and_film_index = new BaseIndex(Constants.TBL_TV_PROGRAM_UNION_FILM_AND_TV_PROGRAM);
        tv_program_union_tv_and_film_index.setAcceptList(Arrays.asList("tv_program_name", "tv_program_description"));
        BaseIndex film_union_tv_and_film_index = new BaseIndex(Constants.TBL_FILM_UNION_FILM_AND_TV_PROGRAM);
        film_union_tv_and_film_index.setAcceptList(Arrays.asList("film_name", "film_description"));

        execBatchQueries(tv_program_queries_obj, tv_program_union_tv_and_film_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(film_queries_obj, film_union_tv_and_film_index, AppConfig.RESULT_DIR + resultFile);

    }
}

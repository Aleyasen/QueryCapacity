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
public class FilmTVNewRelease extends TransformationTester {

    public static List<Integer> q_desired_tv_contain_tv;
    public static List<Integer> q_desired_tv_not_contain_tv;
    public static List<Integer> q_desired_film_contain_film;
    public static List<Integer> q_desired_film_not_contain_film;

    static {
        q_desired_tv_contain_tv = Arrays.asList(43891, 44456, 45755, 47936, 44384, 45123, 46367, 47030, 48640, 48685, 43254, 44178, 44757, 45264, 45990, 46448, 46733, 48395, 49034, 49119, 49253, 49969, 50321, 44124, 46079, 46551, 47906, 49189);
        q_desired_film_not_contain_film = Arrays.asList(44882, 50041, 43019, 43267, 43580, 45110, 45192, 45347, 45516, 45596, 47052, 47059, 48368, 48442, 48788, 48964, 49375, 49512, 49708, 49730, 49800, 49863, 48494);
        q_desired_film_contain_film = new ArrayList<Integer>();
        q_desired_tv_not_contain_tv = new ArrayList<Integer>();
   }

    public static void main(String[] args) {
//        testSourceSchemaDuplicate("tv__film_notable_type_source_schema_duplicate_new_incorrect.txt");
//        testSourceSchema("tv_and_or_film_source_schema_7.txt");
              testTargetSchema("tv__film_notable_type_target_schema_new_incorrect.txt");
    }

    public static void testSourceSchemaDuplicate(String resultFile) {

        Collection<Query> q_tv_text_obj = Query.findById(q_desired_tv_contain_tv);
        Collection<Query> q_television_text_obj = Query.findById(q_desired_tv_not_contain_tv);
        Collection<Query> q_film_text_obj = Query.findById(q_desired_film_not_contain_film);
        Collection<Query> q_movie_text_obj = Query.findById(q_desired_film_contain_film);

//        Collection<Query> queries = Query.findById(47654);

        List<String> filmTVacceptList = Arrays.asList("tv_program_name", "tv_program_description", "film_name", "film_description");

        BaseIndex tv_program_index = new BaseIndex(Constants.TBL_TV_PROGRAM_W_NOTABLE_TYPE);
        tv_program_index.setAcceptList(filmTVacceptList);

        BaseIndex film_index = new BaseIndex(Constants.TBL_FILM_W_NOTABLE_TYPE);
        film_index.setAcceptList(filmTVacceptList);


        execBatchQueries(q_tv_text_obj, tv_program_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_television_text_obj, tv_program_index, AppConfig.RESULT_DIR + resultFile);

        execBatchQueries(q_film_text_obj, film_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_movie_text_obj, film_index, AppConfig.RESULT_DIR + resultFile);


    }

    public static void testSourceSchema(String resultFile) {

        Collection<Query> q_tv_text_obj = Query.findById(q_desired_tv_contain_tv);
        Collection<Query> q_television_text_obj = Query.findById(q_desired_tv_not_contain_tv);
        Collection<Query> q_film_text_obj = Query.findById(q_desired_film_not_contain_film);
        Collection<Query> q_movie_text_obj = Query.findById(q_desired_film_contain_film);

//        Collection<Query> queries = Query.findById(47654);

        List<String> filmTVacceptList = Arrays.asList("tv_program_name", "tv_program_description", "film_name", "film_description");

        BaseIndex tv_program_index = new BaseIndex(Constants.TBL_TV_PROGRAM_W_TYPE);
        tv_program_index.setAcceptList(filmTVacceptList);

        BaseIndex film_index = new BaseIndex(Constants.TBL_FILM_W_TYPE);
        film_index.setAcceptList(filmTVacceptList);


        execBatchQueries(q_tv_text_obj, tv_program_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_television_text_obj, tv_program_index, AppConfig.RESULT_DIR + resultFile);

        execBatchQueries(q_film_text_obj, film_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_movie_text_obj, film_index, AppConfig.RESULT_DIR + resultFile);


    }

    public static void testTargetSchema(String resultFile) {

        Collection<Query> q_tv_text_obj = Query.findById(q_desired_tv_contain_tv);
        Collection<Query> q_television_text_obj = Query.findById(q_desired_tv_not_contain_tv);
        Collection<Query> q_film_text_obj = Query.findById(q_desired_film_not_contain_film);
        Collection<Query> q_movie_text_obj = Query.findById(q_desired_film_contain_film);

//        Collection<Query> queries = Query.findById(47654);


        List<String> filmTVacceptList = Arrays.asList("tv_program_name", "tv_program_description", "film_name", "film_description");

        BaseIndex tv_program_w_notable_type_union_tv_text = new BaseIndex(Constants.TBL_TV_PROGRAM_W_NOTABLE_TYPE_UNION_TV_TEXT);
        tv_program_w_notable_type_union_tv_text.setAcceptList(filmTVacceptList);

        BaseIndex tv_program_w_notable_type_union_television_text = new BaseIndex(Constants.TBL_TV_PROGRAM_W_NOTABLE_TYPE_UNION_TELEVISION_TEXT);
        tv_program_w_notable_type_union_television_text.setAcceptList(filmTVacceptList);

        BaseIndex film_w_notable_type_union_film_text = new BaseIndex(Constants.TBL_FILM_W_NOTABLE_TYPE_UNION_FILM_TEXT);
        film_w_notable_type_union_film_text.setAcceptList(filmTVacceptList);

        BaseIndex film_w_notable_type_union_movie_text = new BaseIndex(Constants.TBL_FILM_W_NOTABLE_TYPE_UNION_MOVIE_TEXT);
        film_w_notable_type_union_movie_text.setAcceptList(filmTVacceptList);


        execBatchQueries(q_tv_text_obj, tv_program_w_notable_type_union_tv_text, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_television_text_obj, tv_program_w_notable_type_union_television_text, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_film_text_obj, film_w_notable_type_union_film_text, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_movie_text_obj, film_w_notable_type_union_movie_text, AppConfig.RESULT_DIR + resultFile);


    }
}

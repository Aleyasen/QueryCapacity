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

/**
 *
 * @author aleyase2-admin
 */
public class FilmTVPlayOperaCVG_80s_90s1 extends TransformationTester {

    public static List<Integer> q_desired_tv_contain_tv;
    public static List<Integer> q_desired_tv_not_contain_tv;
    public static List<Integer> q_desired_film_contain_film;
    public static List<Integer> q_desired_film_not_contain_film;

    public static List<Integer> q_desired_opera_contain_opera;
    public static List<Integer> q_desired_opera_not_contain_opera;

    public static List<Integer> q_desired_play_contain_play;
    public static List<Integer> q_desired_play_not_contain_play;

    public static List<Integer> q_desired_game_contain_game;
    public static List<Integer> q_desired_game_not_contain_game;

    static {
        q_desired_tv_contain_tv = Arrays.asList(43254, 44178, 44757, 45264, 45990, 46448,
                46733, 48395, 49034, 49119, 49253, 49969, 50321);
        q_desired_film_not_contain_film = Arrays.asList(44882, 50041, 43019, 43267, 43580,
                45110, 45192, 45347, 45516, 45596, 47052, 47059, 48368, 48442, 48788,
                48964, 49375, 49512, 49708, 49730, 49800, 49863, 48494);
        q_desired_film_contain_film = Arrays.asList(44882, 50041);
        q_desired_tv_not_contain_tv = Arrays.asList(43891, 44456, 45755, 47936, 44384, 45123, 46367, 47030,
                48640, 48685, 44124, 46079, 46551, 47906, 49189);
        q_desired_opera_contain_opera = Arrays.asList();
        q_desired_opera_not_contain_opera = Arrays.asList();

        q_desired_play_contain_play = Arrays.asList();
        q_desired_play_not_contain_play = Arrays.asList();

        q_desired_game_contain_game = Arrays.asList();
        q_desired_game_not_contain_game = Arrays.asList();

    }

    public static void main(String[] args) {
//        testSourceSchema("tv_film_play_opera_cvg_80s_90s_source_schema_1.txt");
        testTargetSchema("tv_film_play_opera_cvg_80s_90s_target_schema_1.txt");
    }

    public static void testSourceSchema(String resultFile) {
        Collection<Query> q_desired_tv_contain_tv_obj = Query.findById(q_desired_tv_contain_tv);
        Collection<Query> q_desired_tv_not_contain_tv_obj = Query.findById(q_desired_tv_not_contain_tv);
        Collection<Query> q_desired_film_not_contain_film_obj = Query.findById(q_desired_film_not_contain_film);
        Collection<Query> q_desired_film_contain_film_obj = Query.findById(q_desired_film_contain_film);
        Collection<Query> q_desired_play_contain_play_obj = Query.findById(q_desired_play_contain_play);
        Collection<Query> q_desired_play_not_contain_play_obj = Query.findById(q_desired_play_not_contain_play);
        Collection<Query> q_desired_opera_contain_opera_obj = Query.findById(q_desired_opera_contain_opera);
        Collection<Query> q_desired_opera_not_contain_opera_obj = Query.findById(q_desired_opera_not_contain_opera);
        Collection<Query> q_desired_game_contain_game_obj = Query.findById(q_desired_game_contain_game);
        Collection<Query> q_desired_game_not_contain_game_obj = Query.findById(q_desired_game_not_contain_game);

//        Collection<Query> queries = Query.findById(47654);
        List<String> filmTVOperaPlayCVGacceptList = Arrays.asList("tv_program_name", "tv_program_description",
                "film_name", "film_description",
                "opera_name", "opera_description",
                "play_name", "play_description",
                "computer_videogame_description", "computer_videogame_name");

        BaseIndex tv_program_index = new BaseIndex(Constants.TBL_TV_PROGRAM_W_TYPE);
        tv_program_index.setAcceptList(filmTVOperaPlayCVGacceptList);

        BaseIndex film_index = new BaseIndex(Constants.TBL_FILM_W_TYPE);
        film_index.setAcceptList(filmTVOperaPlayCVGacceptList);

        BaseIndex opera_index = new BaseIndex(Constants.TBL_OPERA);
        opera_index.setAcceptList(filmTVOperaPlayCVGacceptList);

        BaseIndex play_index = new BaseIndex(Constants.TBL_PLAY);
        play_index.setAcceptList(filmTVOperaPlayCVGacceptList);

        BaseIndex game_index = new BaseIndex(Constants.TBL_GAME);
        game_index.setAcceptList(filmTVOperaPlayCVGacceptList);

        execBatchQueries(q_desired_tv_contain_tv_obj, tv_program_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_desired_tv_not_contain_tv_obj, tv_program_index, AppConfig.RESULT_DIR + resultFile);

        execBatchQueries(q_desired_film_contain_film_obj, film_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_desired_film_not_contain_film_obj, film_index, AppConfig.RESULT_DIR + resultFile);

        execBatchQueries(q_desired_play_contain_play_obj, play_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_desired_play_not_contain_play_obj, play_index, AppConfig.RESULT_DIR + resultFile);

        execBatchQueries(q_desired_opera_contain_opera_obj, opera_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_desired_opera_not_contain_opera_obj, opera_index, AppConfig.RESULT_DIR + resultFile);

        execBatchQueries(q_desired_game_contain_game_obj, game_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_desired_game_not_contain_game_obj, game_index, AppConfig.RESULT_DIR + resultFile);

    }

    public static void testTargetSchema(String resultFile) {

        Collection<Query> q_desired_tv_contain_tv_obj = Query.findById(q_desired_tv_contain_tv);
        Collection<Query> q_desired_tv_not_contain_tv_obj = Query.findById(q_desired_tv_not_contain_tv);
        Collection<Query> q_desired_film_not_contain_film_obj = Query.findById(q_desired_film_not_contain_film);
        Collection<Query> q_desired_film_contain_film_obj = Query.findById(q_desired_film_contain_film);
        Collection<Query> q_desired_play_contain_play_obj = Query.findById(q_desired_play_contain_play);
        Collection<Query> q_desired_play_not_contain_play_obj = Query.findById(q_desired_play_not_contain_play);
        Collection<Query> q_desired_opera_contain_opera_obj = Query.findById(q_desired_opera_contain_opera);
        Collection<Query> q_desired_opera_not_contain_opera_obj = Query.findById(q_desired_opera_not_contain_opera);
        Collection<Query> q_desired_game_contain_game_obj = Query.findById(q_desired_game_contain_game);
        Collection<Query> q_desired_game_not_contain_game_obj = Query.findById(q_desired_game_not_contain_game);


//        Collection<Query> queries = Query.findById(47654);
        List<String> filmTVOperaPlayCVGacceptList = Arrays.asList("tv_program_name", "tv_program_description",
                "film_name", "film_description",
                "opera_name", "opera_description",
                "play_name", "play_description",
                "computer_videogame_description", "computer_videogame_name");

        BaseIndex tv_program_80s_90s_union_other_tv_program_index = new BaseIndex(Constants.TBL_TV_PROGRAM_80S_90S_UNION_OTHER_TV_PROGRAM);
        tv_program_80s_90s_union_other_tv_program_index.setAcceptList(filmTVOperaPlayCVGacceptList);

        BaseIndex tv_program_not_80s_90s_index = new BaseIndex(Constants.TBL_TV_PROGRAM_NOT_80S_90S);
        tv_program_not_80s_90s_index.setAcceptList(filmTVOperaPlayCVGacceptList);

        BaseIndex film_80s_90s_union_other_film_index = new BaseIndex(Constants.TBL_FILM_80S_90S_UNION_OTHER_FILM);
        film_80s_90s_union_other_film_index.setAcceptList(filmTVOperaPlayCVGacceptList);

        BaseIndex film_not_80s_90s_index = new BaseIndex(Constants.TBL_FILM_NOT_80S_90S);
        film_not_80s_90s_index.setAcceptList(filmTVOperaPlayCVGacceptList);
        
        
        BaseIndex play_80s_90s_union_other_play_index = new BaseIndex(Constants.TBL_PLAY_80S_90S_UNION_OTHER_PLAY);
        play_80s_90s_union_other_play_index.setAcceptList(filmTVOperaPlayCVGacceptList);

        BaseIndex play_not_80s_90s_index = new BaseIndex(Constants.TBL_PLAY_NOT_80S_90S);
        play_not_80s_90s_index.setAcceptList(filmTVOperaPlayCVGacceptList);
        
        
        BaseIndex opera_80s_90s_union_other_opera_index = new BaseIndex(Constants.TBL_OPERA_80S_90S_UNION_OTHER_OPERA);
        opera_80s_90s_union_other_opera_index.setAcceptList(filmTVOperaPlayCVGacceptList);

        BaseIndex opera_not_80s_90s_index = new BaseIndex(Constants.TBL_OPERA_NOT_80S_90S);
        opera_not_80s_90s_index.setAcceptList(filmTVOperaPlayCVGacceptList);
        
        
        BaseIndex game_80s_90s_union_other_game_index = new BaseIndex(Constants.TBL_GAME_80S_90S_UNION_OTHER_GAME);
        game_80s_90s_union_other_game_index.setAcceptList(filmTVOperaPlayCVGacceptList);

        BaseIndex game_not_80s_90s_index = new BaseIndex(Constants.TBL_GAME_NOT_80S_90S);
        game_not_80s_90s_index.setAcceptList(filmTVOperaPlayCVGacceptList);

        execBatchQueries(q_desired_tv_contain_tv_obj, tv_program_80s_90s_union_other_tv_program_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_desired_tv_not_contain_tv_obj, tv_program_not_80s_90s_index, AppConfig.RESULT_DIR + resultFile);

        execBatchQueries(q_desired_film_contain_film_obj, film_80s_90s_union_other_film_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_desired_film_not_contain_film_obj, film_not_80s_90s_index, AppConfig.RESULT_DIR + resultFile);
        
        execBatchQueries(q_desired_play_contain_play_obj, play_80s_90s_union_other_play_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_desired_play_not_contain_play_obj, play_not_80s_90s_index, AppConfig.RESULT_DIR + resultFile);
        
        execBatchQueries(q_desired_opera_contain_opera_obj, opera_80s_90s_union_other_opera_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_desired_opera_not_contain_opera_obj, opera_not_80s_90s_index, AppConfig.RESULT_DIR + resultFile);
        
        execBatchQueries(q_desired_game_contain_game_obj, game_80s_90s_union_other_game_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(q_desired_game_not_contain_game_obj, game_not_80s_90s_index, AppConfig.RESULT_DIR + resultFile);

    }
}

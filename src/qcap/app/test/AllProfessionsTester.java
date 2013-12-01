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
public class AllProfessionsTester extends TransformationTester {

    public static List<Integer> all_queries;

    static {
        all_queries = Arrays.asList(47483, 49396, 49721, 50032, 44883, 50309, 48511, 48558, 48500, 45329, 43667, 45967, 48587,
                46045, 43709, 49527, 46267, 49930, 45042, 44601, 47654, 48129, 49904, 43304, 47326, 47893, 43862, 45505, 46333,
                47429, 49471, 48492, 43415, 49469, 44444, 47805, 47457, 48686, 48505, 48726);
    }

    public static void testSingerOverView() {
        Collection<Query> singer_queries = Query.dropStatement(Query.findById(Arrays.asList(48129)), "profession");
        BaseIndex singer_index = new BaseIndex(Constants.VI_PERSON_PROF_SINGER);
        singer_index.setAcceptList(Arrays.asList("person_name", "person_description"));
        execBatchQueries(singer_queries, singer_index, AppConfig.RESULT_DIR + "singer-test");

    }
    
    public static void testFilmProducerOverView() {
        Collection<Query> film_producer_queries = Query.dropStatement(Query.findById(Arrays.asList(48492)), "profession");
        BaseIndex film_producer_index = new BaseIndex(Constants.VI_PERSON_PROF_FILM_PRODUCER);
        film_producer_index.setAcceptList(Arrays.asList("person_name", "person_description"));
        execBatchQueries(film_producer_queries, film_producer_index, AppConfig.RESULT_DIR + "film_procuder_test");

    }
    
    public static void main(String[] args) {
     //   testSingerOverView();
//        testFilmProducerOverView();
    //    testProfessionMultipleTable("person_profession_multiple_4.txt");
        testProfessionSingleTable("person_profession_one_view_temp.txt");
//        testProfessionSingleTable("test_film_producer_one_view.txt");
    }

    public static void testProfessionMultipleTable(String resultFile) {

        Collection<Query> prophet_queries = Query.dropStatement(Query.findById(Arrays.asList(47483, 49396, 49721, 50032)), "profession");
        Collection<Query> designer_queries = Query.dropStatement(Query.findById(Arrays.asList(44883)), "profession");
        Collection<Query> pirate_queries = Query.dropStatement(Query.findById(Arrays.asList(50309)), "profession");
        Collection<Query> stunt_performer_queries = Query.dropStatement(Query.findById(Arrays.asList(48511, 48558)), "profession");
        Collection<Query> writer_queries = Query.dropStatement(Query.findById(Arrays.asList(48500)), "profession");
        Collection<Query> violinist_queries = Query.dropStatement(Query.findById(Arrays.asList(45329)), "profession");
        Collection<Query> playwright_queries = Query.dropStatement(Query.findById(Arrays.asList(43667)), "profession");
        Collection<Query> basketball_player_queries = Query.dropStatement(Query.findById(Arrays.asList(45967, 48587)), "profession");
        Collection<Query> trade_unionist_queries = Query.dropStatement(Query.findById(Arrays.asList(46045)), "profession");
        Collection<Query> comedian_queries = Query.dropStatement(Query.findById(Arrays.asList(43709)), "profession");
        Collection<Query> nude_glamour_model_queries = Query.dropStatement(Query.findById(Arrays.asList(49527)), "profession");
        Collection<Query> diplomat_queries = Query.dropStatement(Query.findById(Arrays.asList(46267)), "profession");
        Collection<Query> lawyer_queries = Query.dropStatement(Query.findById(Arrays.asList(49930)), "profession");
        Collection<Query> sociologist_queries = Query.dropStatement(Query.findById(Arrays.asList(45042)), "profession");
        Collection<Query> singer_queries = Query.dropStatement(Query.findById(Arrays.asList(44601, 47654, 48129, 49904)), "profession");
        Collection<Query> baseball_player_queries = Query.dropStatement(Query.findById(Arrays.asList(43304, 47326)), "profession");
        Collection<Query> ice_hockey_player_queries = Query.dropStatement(Query.findById(Arrays.asList(47893)), "profession");
        Collection<Query> crown_princess_queries = Query.dropStatement(Query.findById(Arrays.asList(43862)), "profession");
        Collection<Query> rapper_queries = Query.dropStatement(Query.findById(Arrays.asList(45505, 46333)), "profession");
        Collection<Query> talk_show_host_queries = Query.dropStatement(Query.findById(Arrays.asList(47429, 49471)), "profession");
        Collection<Query> film_producer_queries = Query.dropStatement(Query.findById(Arrays.asList(48492)), "profession");
        Collection<Query> opera_composer_queries = Query.dropStatement(Query.findById(Arrays.asList(43415, 49469)), "profession");
        Collection<Query> american_football_player_queries = Query.dropStatement(Query.findById(Arrays.asList(44444, 47805)), "profession");
        Collection<Query> poet_queries = Query.dropStatement(Query.findById(Arrays.asList(47457)), "profession");
        Collection<Query> inventor_queries = Query.dropStatement(Query.findById(Arrays.asList(48686)), "profession");
        Collection<Query> mathematician_queries = Query.dropStatement(Query.findById(Arrays.asList(48505)), "profession");
        Collection<Query> journalist_queries = Query.dropStatement(Query.findById(Arrays.asList(48726)), "profession");

        BaseIndex prophet_index = new BaseIndex(Constants.VI_PERSON_PROF_PROPHET);
        prophet_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex designer_index = new BaseIndex(Constants.VI_PERSON_PROF_DESIGNER);
        designer_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex pirate_index = new BaseIndex(Constants.VI_PERSON_PROF_PIRATE);
        pirate_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex stunt_performer_index = new BaseIndex(Constants.VI_PERSON_PROF_STUNT_PERFORMER);
        stunt_performer_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex writer_index = new BaseIndex(Constants.VI_PERSON_PROF_WRITER);
        writer_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex violinist_index = new BaseIndex(Constants.VI_PERSON_PROF_VIOLINIST);
        violinist_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex playwright_index = new BaseIndex(Constants.VI_PERSON_PROF_PLAYWRIGHT);
        playwright_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex basketball_player_index = new BaseIndex(Constants.VI_PERSON_PROF_BASKETBALL_PLAYER);
        basketball_player_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex trade_unionist_index = new BaseIndex(Constants.VI_PERSON_PROF_TRADE_UNIONIST);
        trade_unionist_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex comedian_index = new BaseIndex(Constants.VI_PERSON_PROF_COMEDIAN);
        comedian_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex nude_glamour_model_index = new BaseIndex(Constants.VI_PERSON_PROF_NUDE_GLAMOUR_MODEL);
        nude_glamour_model_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex diplomat_index = new BaseIndex(Constants.VI_PERSON_PROF_DIPLOMAT);
        diplomat_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex lawyer_index = new BaseIndex(Constants.VI_PERSON_PROF_LAWYER);
        lawyer_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex sociologist_index = new BaseIndex(Constants.VI_PERSON_PROF_SOCIOLOGIST);
        sociologist_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex singer_index = new BaseIndex(Constants.VI_PERSON_PROF_SINGER);
        singer_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex baseball_player_index = new BaseIndex(Constants.VI_PERSON_PROF_BASEBALL_PLAYER);
        baseball_player_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex ice_hockey_player_index = new BaseIndex(Constants.VI_PERSON_PROF_ICE_HOCKEY_PLAYER);
        ice_hockey_player_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex crown_princess_index = new BaseIndex(Constants.VI_PERSON_PROF_CROWN_PRINCESS);
        crown_princess_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex rapper_index = new BaseIndex(Constants.VI_PERSON_PROF_RAPPER);
        rapper_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex talk_show_host_index = new BaseIndex(Constants.VI_PERSON_PROF_TALK_SHOW_HOST);
        talk_show_host_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex film_producer_index = new BaseIndex(Constants.VI_PERSON_PROF_FILM_PRODUCER);
        film_producer_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex opera_composer_index = new BaseIndex(Constants.VI_PERSON_PROF_OPERA_COMPOSER);
        opera_composer_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex american_football_player_index = new BaseIndex(Constants.VI_PERSON_PROF_AMERICAN_FOOTBALL_PLAYER);
        american_football_player_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex poet_index = new BaseIndex(Constants.VI_PERSON_PROF_POET);
        poet_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex inventor_index = new BaseIndex(Constants.VI_PERSON_PROF_INVENTOR);
        inventor_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex mathematician_index = new BaseIndex(Constants.VI_PERSON_PROF_MATHEMATICIAN);
        mathematician_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        BaseIndex journalist_index = new BaseIndex(Constants.VI_PERSON_PROF_JOURNALIST);
        journalist_index.setAcceptList(Arrays.asList("person_name", "person_description"));

        execBatchQueries(prophet_queries, prophet_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(designer_queries, designer_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(pirate_queries, pirate_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(stunt_performer_queries, stunt_performer_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(writer_queries, writer_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(violinist_queries, violinist_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(playwright_queries, playwright_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(basketball_player_queries, basketball_player_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(trade_unionist_queries, trade_unionist_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(comedian_queries, comedian_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(nude_glamour_model_queries, nude_glamour_model_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(diplomat_queries, diplomat_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(lawyer_queries, lawyer_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(sociologist_queries, sociologist_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(singer_queries, singer_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(baseball_player_queries, baseball_player_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(ice_hockey_player_queries, ice_hockey_player_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(crown_princess_queries, crown_princess_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(rapper_queries, rapper_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(talk_show_host_queries, talk_show_host_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(film_producer_queries, film_producer_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(opera_composer_queries, opera_composer_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(american_football_player_queries, american_football_player_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(poet_queries, poet_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(inventor_queries, inventor_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(mathematician_queries, mathematician_index, AppConfig.RESULT_DIR + resultFile);
        execBatchQueries(journalist_queries, journalist_index, AppConfig.RESULT_DIR + resultFile);

    }

    public static void testProfessionSingleTable(String resultFile) {

//        Collection<Query> queries = Query.findById(all_queries);
        Collection<Query> queries = Query.findById(47654);


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

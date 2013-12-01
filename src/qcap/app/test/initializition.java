/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.test;

import java.sql.Connection;
import java.util.Arrays;
import qcap.app.AppConfig;
import qcap.app.Constants;
import qcap.app.DBManager;
import qcap.app.retrieval.BaseIndex;
import qcap.app.retrieval.Field;

/**
 *
 * @author aleyase2
 */
public class initializition {

    public static void main(String[] args) {
        buildSemanticTypesTable();
        //  buildAllPersonTable();
        //    buildProfessionViews();
        // buildEducationViews();
    }

    public static void buildSemanticTypesTable() {
//        buildTables(Constants.TBL_BOOK, true);
//        buildTables(Constants.TBL_ALBUM, true);
    //    buildTables(Constants.VI_FILM_TV_GAME, true);

        buildTables(Constants.VI_ALBUM_BOOK, true);

        buildTables(Constants.VI_FILM_TV, true);

        buildTables(Constants.VI_GAME_ALBUM, true);

        buildTables(Constants.VI_BOOK, true);
    }

    public static void buildAllPersonTable() {
//        buildTables(Constants.TBL_PERSON, true);
//        buildTables(Constants.TBL_PROFESSION, false);
//        buildTables(Constants.TBL_NATIONALITY, false);
//        buildTables(Constants.VI_PERSON_ART, true);
//        buildTables(Constants.VI_PERSON_SPORT, true);
//        buildTables(Constants.VI_PERSON_PROFESSION_OTHER, true);
        buildTables(Constants.TBL_EDUCATION, false);
    }

    public static void buildEducationViews() {
        buildTables(Constants.VI_PERSON_EDU_HONG_KONG_COLLEGE_OF_MEDICINE_FOR_CHINESE, true);
        buildTables(Constants.VI_PERSON_EDU_UNIVERSITY_OF_KENTUCKY, true);
        buildTables(Constants.VI_PERSON_EDU_ST_VINCENT_ST_MARY_HIGH_SCHOOL, true);
        buildTables(Constants.VI_PERSON_EDU_THE_BOSTON_CONSERVATORY, true);
        buildTables(Constants.VI_PERSON_EDU_LOUISIANA_STATE_UNIVERSITY, true);
        buildTables(Constants.VI_PERSON_EDU_LOWER_MERION_HIGH_SCHOOL, true);
        buildTables(Constants.VI_PERSON_EDU_UNIVERSITY_OF_ALABAMA, true);
        buildTables(Constants.VI_PERSON_EDU_NORTH_FORT_MYERS_HIGH_SCHOOL, true);
        buildTables(Constants.VI_PERSON_EDU_UNIVERSITY_OF_CALIFORNIA_LOS_ANGELES, true);
        buildTables(Constants.VI_PERSON_EDU_SOUTH_SIDE_HIGH_SCHOOL, true);
    }

    public static void buildProfessionViews() {
        buildTables(Constants.VI_PERSON_PROF_PROPHET, true);
        buildTables(Constants.VI_PERSON_PROF_DESIGNER, true);
        buildTables(Constants.VI_PERSON_PROF_PIRATE, true);
        buildTables(Constants.VI_PERSON_PROF_STUNT_PERFORMER, true);
        buildTables(Constants.VI_PERSON_PROF_WRITER, true);
        buildTables(Constants.VI_PERSON_PROF_VIOLINIST, true);
        buildTables(Constants.VI_PERSON_PROF_PLAYWRIGHT, true);
        buildTables(Constants.VI_PERSON_PROF_BASKETBALL_PLAYER, true);
        buildTables(Constants.VI_PERSON_PROF_TRADE_UNIONIST, true);
        buildTables(Constants.VI_PERSON_PROF_COMEDIAN, true);
        buildTables(Constants.VI_PERSON_PROF_NUDE_GLAMOUR_MODEL, true);
        buildTables(Constants.VI_PERSON_PROF_DIPLOMAT, true);
        buildTables(Constants.VI_PERSON_PROF_LAWYER, true);
        buildTables(Constants.VI_PERSON_PROF_SOCIOLOGIST, true);
        buildTables(Constants.VI_PERSON_PROF_SINGER, true);
        buildTables(Constants.VI_PERSON_PROF_BASEBALL_PLAYER, true);
        buildTables(Constants.VI_PERSON_PROF_ICE_HOCKEY_PLAYER, true);
        buildTables(Constants.VI_PERSON_PROF_CROWN_PRINCESS, true);
        buildTables(Constants.VI_PERSON_PROF_RAPPER, true);
        buildTables(Constants.VI_PERSON_PROF_TALK_SHOW_HOST, true);
        buildTables(Constants.VI_PERSON_PROF_FILM_PRODUCER, true);
        buildTables(Constants.VI_PERSON_PROF_OPERA_COMPOSER, true);
        buildTables(Constants.VI_PERSON_PROF_AMERICAN_FOOTBALL_PLAYER, true);
        buildTables(Constants.VI_PERSON_PROF_POET, true);
        buildTables(Constants.VI_PERSON_PROF_INVENTOR, true);
        buildTables(Constants.VI_PERSON_PROF_MATHEMATICIAN, true);
        buildTables(Constants.VI_PERSON_PROF_JOURNALIST, true);
    }

    public static void buildTables(String table, boolean desc) {

        Connection conn = new DBManager().getConnection();
        BaseIndex index;
        if (desc) {
            index = getIndexMetadataNameDesc(table);
        } else {
            index = getIndexMetadataName(table);
        }
        index.save();
        index.generateTrecFile(conn);
        index.build();
        for (Field f : index.getFields()) {
            f.setVocabCount(index.calcVocabCount(f.getName()));
            f.setWordCount(index.calcWordCount(f.getName()));
        }
        index.save();
    }

    public static BaseIndex getIndexMetadataNameDesc(String tableName) {
        BaseIndex mdata = new BaseIndex();
        mdata.setTable(tableName);
        mdata.setQuery("select id, fbid, name, description from " + tableName);

        mdata.setIndexDirectory(AppConfig.BASE_DIR + "/index/" + tableName + "/");
        mdata.setTrecFile(AppConfig.BASE_DIR + "/trec/" + tableName + ".trectext");
        mdata.setQueryFile(AppConfig.BASE_DIR + "/query/" + tableName + ".query");
        mdata.setResultFile(AppConfig.BASE_DIR + "/result/" + tableName + ".result");
        Field f1 = new Field("name");
        Field f2 = new Field("description");
        mdata.setFields(Arrays.asList(f1, f2));
        //mdata.setFields(Arrays.asList(f2));

        mdata.setIdQuery("select fbid from " + tableName + " where id = %s");
        return mdata;
    }

    public static BaseIndex getIndexMetadataName(String tableName) {
        BaseIndex mdata = new BaseIndex();
        mdata.setTable(tableName);
        mdata.setQuery("select id, fbid, name from " + tableName);

        mdata.setIndexDirectory(AppConfig.BASE_DIR + "/index/" + tableName + "/");
        mdata.setTrecFile(AppConfig.BASE_DIR + "/trec/" + tableName + ".trectext");
        mdata.setQueryFile(AppConfig.BASE_DIR + "/query/" + tableName + ".query");
        mdata.setResultFile(AppConfig.BASE_DIR + "/result/" + tableName + ".result");
        Field f1 = new Field("name");
        mdata.setFields(Arrays.asList(f1));

        mdata.setIdQuery("select fbid from " + tableName + " where id = %s");
        return mdata;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        buildFilmAndOrTVIndices();
//        buildSemanticTypesTable();
        //  buildAllPersonTable();
        //    buildProfessionViews();
        // buildEducationViews();
    }

    public static void buildFilmAndOrTVIndices() {
        List<String> fields = Arrays.asList("name", "description", "type");
//        buildIndex(Constants.TBL_TV_PROGRAM_W_TYPE, fields);
//        buildIndex(Constants.TBL_FILM_W_TYPE, fields);
//        buildIndex(Constants.TBL_FILM_UNION_FILM_AND_TV_PROGRAM, fields);
//        buildIndex(Constants.TBL_TV_PROGRAM_UNION_FILM_AND_TV_PROGRAM, fields);
        buildIndex(Constants.TBL_OTHER_UNION_FILM_AND_TV_PROGRAM, fields);
    }

    public static void buildSemanticTypesTable() {
//        buildTables(Constants.TBL_BOOK);
//        buildTables(Constants.TBL_ALBUM);
        //    buildTables(Constants.VI_FILM_TV_GAME);

        buildDefaultIndex(Constants.VI_ALBUM_BOOK);

        buildDefaultIndex(Constants.VI_FILM_TV);

        buildDefaultIndex(Constants.VI_GAME_ALBUM);

        buildDefaultIndex(Constants.VI_BOOK);
    }

    public static void buildAllPersonTable() {
//        buildDefaultIndex(Constants.TBL_PERSON);
//        buildTables(Constants.TBL_PROFESSION, false);
//        buildTables(Constants.TBL_NATIONALITY, false);
//        buildDefaultIndex(Constants.VI_PERSON_ART);
//        buildDefaultIndex(Constants.VI_PERSON_SPORT);
//        buildDefaultIndex(Constants.VI_PERSON_PROFESSION_OTHER);
        buildIndexOnlyName(Constants.TBL_EDUCATION);
    }

    public static void buildEducationViews() {
        buildDefaultIndex(Constants.VI_PERSON_EDU_HONG_KONG_COLLEGE_OF_MEDICINE_FOR_CHINESE);
        buildDefaultIndex(Constants.VI_PERSON_EDU_UNIVERSITY_OF_KENTUCKY);
        buildDefaultIndex(Constants.VI_PERSON_EDU_ST_VINCENT_ST_MARY_HIGH_SCHOOL);
        buildDefaultIndex(Constants.VI_PERSON_EDU_THE_BOSTON_CONSERVATORY);
        buildDefaultIndex(Constants.VI_PERSON_EDU_LOUISIANA_STATE_UNIVERSITY);
        buildDefaultIndex(Constants.VI_PERSON_EDU_LOWER_MERION_HIGH_SCHOOL);
        buildDefaultIndex(Constants.VI_PERSON_EDU_UNIVERSITY_OF_ALABAMA);
        buildDefaultIndex(Constants.VI_PERSON_EDU_NORTH_FORT_MYERS_HIGH_SCHOOL);
        buildDefaultIndex(Constants.VI_PERSON_EDU_UNIVERSITY_OF_CALIFORNIA_LOS_ANGELES);
        buildDefaultIndex(Constants.VI_PERSON_EDU_SOUTH_SIDE_HIGH_SCHOOL);
    }

    public static void buildProfessionViews() {
        buildDefaultIndex(Constants.VI_PERSON_PROF_PROPHET);
        buildDefaultIndex(Constants.VI_PERSON_PROF_DESIGNER);
        buildDefaultIndex(Constants.VI_PERSON_PROF_PIRATE);
        buildDefaultIndex(Constants.VI_PERSON_PROF_STUNT_PERFORMER);
        buildDefaultIndex(Constants.VI_PERSON_PROF_WRITER);
        buildDefaultIndex(Constants.VI_PERSON_PROF_VIOLINIST);
        buildDefaultIndex(Constants.VI_PERSON_PROF_PLAYWRIGHT);
        buildDefaultIndex(Constants.VI_PERSON_PROF_BASKETBALL_PLAYER);
        buildDefaultIndex(Constants.VI_PERSON_PROF_TRADE_UNIONIST);
        buildDefaultIndex(Constants.VI_PERSON_PROF_COMEDIAN);
        buildDefaultIndex(Constants.VI_PERSON_PROF_NUDE_GLAMOUR_MODEL);
        buildDefaultIndex(Constants.VI_PERSON_PROF_DIPLOMAT);
        buildDefaultIndex(Constants.VI_PERSON_PROF_LAWYER);
        buildDefaultIndex(Constants.VI_PERSON_PROF_SOCIOLOGIST);
        buildDefaultIndex(Constants.VI_PERSON_PROF_SINGER);
        buildDefaultIndex(Constants.VI_PERSON_PROF_BASEBALL_PLAYER);
        buildDefaultIndex(Constants.VI_PERSON_PROF_ICE_HOCKEY_PLAYER);
        buildDefaultIndex(Constants.VI_PERSON_PROF_CROWN_PRINCESS);
        buildDefaultIndex(Constants.VI_PERSON_PROF_RAPPER);
        buildDefaultIndex(Constants.VI_PERSON_PROF_TALK_SHOW_HOST);
        buildDefaultIndex(Constants.VI_PERSON_PROF_FILM_PRODUCER);
        buildDefaultIndex(Constants.VI_PERSON_PROF_OPERA_COMPOSER);
        buildDefaultIndex(Constants.VI_PERSON_PROF_AMERICAN_FOOTBALL_PLAYER);
        buildDefaultIndex(Constants.VI_PERSON_PROF_POET);
        buildDefaultIndex(Constants.VI_PERSON_PROF_INVENTOR);
        buildDefaultIndex(Constants.VI_PERSON_PROF_MATHEMATICIAN);
        buildDefaultIndex(Constants.VI_PERSON_PROF_JOURNALIST);
    }

    public static void buildIndexOnlyName(String table) {
        buildIndex(table, Arrays.asList("name"));
    }

    public static void buildDefaultIndex(String table) {
        buildIndex(table, Arrays.asList("name", "description"));
    }

    public static void buildIndex(String table, List<String> fields) {
        Connection conn = new DBManager().getConnection();
        BaseIndex index = getIndexMetadata(table, fields);
        System.out.println("building index:" + index);
        index.save();
        index.generateTrecFile(conn);
        index.build();
        for (Field f : index.getFields()) {
            f.setVocabCount(index.calcVocabCount(f.getName()));
            f.setWordCount(index.calcWordCount(f.getName()));
        }
        index.save();
    }

    public static BaseIndex getIndexMetadata(String tableName, List<String> fields) {
        BaseIndex mdata = new BaseIndex();
        mdata.setTable(tableName);
        String fieldStr = "";
        for (String field : fields) {
            fieldStr += ", " + field;
        }
        mdata.setQuery("select id, fbid" + fieldStr + " from " + tableName);

        mdata.setIndexDirectory(AppConfig.BASE_DIR + "/index/" + tableName + "/");
        mdata.setTrecFile(AppConfig.BASE_DIR + "/trec/" + tableName + ".trectext");
        mdata.setQueryFile(AppConfig.BASE_DIR + "/query/" + tableName + ".query");
        mdata.setResultFile(AppConfig.BASE_DIR + "/result/" + tableName + ".result");
        List<Field> fieldObjs = new ArrayList<Field>();
        for (String field : fields) {
            Field f = new Field(field);
            fieldObjs.add(f);
        }
        mdata.setFields(fieldObjs);
        //mdata.setFields(Arrays.asList(f2));

        mdata.setIdQuery("select fbid from " + tableName + " where id = %s");
        return mdata;
    }
}

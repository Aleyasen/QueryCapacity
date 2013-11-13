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
        buildAllPersonTable();
    }

    public static void buildAllPersonTable() {
//        buildTables(Constants.TBL_PERSON, true);
//        buildTables(Constants.TBL_PROFESSION, false);
//        buildTables(Constants.TBL_NATIONALITY, false);
        buildTables(Constants.VI_PERSON_ART, true);
        buildTables(Constants.VI_PERSON_SPORT, true);
        buildTables(Constants.VI_PERSON_PROFESSION_OTHER, true);

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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.utils;

import qcap.app.query.Query;
import qcap.app.query.QueryResult;
import qcap.app.retrieval.Field;
import qcap.app.retrieval.BaseIndex;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lemurproject.galago.core.index.corpus.CorpusFileWriter;
import org.lemurproject.galago.core.parse.Document;
import org.lemurproject.galago.core.tools.App;
import org.lemurproject.galago.core.tools.apps.StatsFn;
import org.lemurproject.galago.tupleflow.FakeParameters;
import org.lemurproject.galago.tupleflow.Parameters;
import org.lemurproject.galago.tupleflow.Utility;

/**
 *
 * @author Aale
 */
public class IndexUtil {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        IndexUtil ib = new IndexUtil();
        //null.doQuery();
    }

    public List<QueryResult> runUnionQuery(Query query, BaseIndex mdata1, BaseIndex mdata2) {
        return null;
    }

    public static Long getSumWordCount(List<BaseIndex> list, String fieldName) {
        Long sum = 0L;
        for (BaseIndex mdata : list) {
            sum += mdata.getWordCount(fieldName);
        }
        return sum;
    }

    public static void testCorpus() throws Exception {

        File corpus = null;
        try {
            List<Document> docs = new ArrayList();
            for (int i = 0; i < 100; i++) {
                Document d = new Document();

                d.identifier = i;
                d.name = "name-" + i;
                d.text = "<pro>salam moti</pro> <title>hello world moti</title> <other>god and</other> <desc>todo salam<desc>";
//                d.metadata = new HashMap();
//                d.metadata.put("meta", "data-" + i);
//                d.terms = new ArrayList();
//                d.terms.add("term-" + i);
//                d.tags = new ArrayList();
//                d.tags.add(new Tag("tag", new HashMap(), i, i));
//                d.tags.get(0).attributes.put("attr", "value-" + i);

                docs.add(d);
            }

            System.out.println("Size" + docs.size());
            corpus = new File("/srv/data/projects/galago/datasets/temp/data1.corpus");

            System.out.println("FilePath" + corpus.getAbsolutePath());
            // test defaulty behaviour:y
            Parameters p = new Parameters();
            p.set("filename", corpus.getAbsolutePath());
            CorpusFileWriter writer = new CorpusFileWriter(new FakeParameters(p));
            for (Document d : docs) {
                writer.process(d);
            }
            writer.close();

        } finally {
        }
    }

    public static ArrayList<String> getTuple(PreparedStatement pstmt, String field) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
//                System.out.println(rs.getString("id") + " " + rs.getString("id") + " " + rs.getString(field));
//                System.out.println();
                list.addAll(Arrays.asList(BaseIndex.nullSafe(rs.getString("id")).trim(), BaseIndex.nullSafe(rs.getString("fbid")).trim(), BaseIndex.nullSafe(rs.getString("name")).trim(), BaseIndex.nullSafe(rs.getString("description")).trim()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

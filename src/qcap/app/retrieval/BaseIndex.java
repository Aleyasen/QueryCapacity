/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.retrieval;

import qcap.app.AppConfig;
import qcap.app.Constants;
import qcap.app.DBManager;
import qcap.app.query.QueryResult;
import qcap.app.query.QueryStatement;
import qcap.app.query.Query;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lemurproject.galago.core.index.disk.DiskIndex;
import org.lemurproject.galago.core.retrieval.LocalRetrieval;
import org.lemurproject.galago.core.retrieval.query.Node;
import org.lemurproject.galago.core.retrieval.query.StructuredQuery;
import org.lemurproject.galago.core.retrieval.traversal.BM25FTraversal;
import org.lemurproject.galago.core.tools.App;
import org.lemurproject.galago.core.tools.apps.StatsFn;
import org.lemurproject.galago.tupleflow.Parameters;
import org.lemurproject.galago.tupleflow.Utility;

/**
 *
 * @author Aale
 */
public class BaseIndex extends Index {

    public static String nullSafe(String str) {
        if (str != null) {
            return str;
        }
        return "";
    }

    public static String trecDocument(String docno, String text) {
        return "<DOC>\n<DOCNO>" + docno + "</DOCNO>\n" + "<TEXT>\n" + text + "</TEXT>\n</DOC>\n";
    }

    public Long getDF(String fieldName, String term) {
        long df = 0;
        String keyStr = "#counts:" + term + ":part=field.porter." + fieldName + "()";
        String input = "--node+" + keyStr;
        Parameters parameters = getStat(input, this);
        if (parameters != null) {
            Parameters innerParam = parameters.getMap(keyStr);
            df = innerParam.getLong("nodeDocumentCount");
            return df;
        } else {
            return 0L;
        }
    }

    public static Long getSumDF(List<BaseIndex> list, String fieldName, String term) {
        Long sum = 0L;
        for (BaseIndex mdata : list) {
            sum += mdata.getDF(fieldName, term);
        }
        return sum;
    }

    public static Parameters getStat(String input, BaseIndex mdata) {
        try {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(array);
            (new StatsFn()).run(new String[]{"stats", "--index=" + mdata.getIndexDirectory(), input}, printStream);
          //  System.out.println("Stat Input: " + array.toString());
            Parameters results = Parameters.parse(array.toString());
            array.close();
            printStream.close();
            return results;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(BaseIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Long getWordCount(String fieldname) {
        Field f = fieldsMap.get(fieldname);
        return f.getWordCount();
    }

    @Override
    public Long calcWordCount(String fieldname) {
        try {
            Connection conn = DBManager.staticConnection;
            String sql = this.getQuery();
            Statement stmt = conn.createStatement();
            conn.setAutoCommit(false);
            stmt.setFetchSize(AppConfig.FETCH_SIZE);
            ResultSet rs = stmt.executeQuery(sql);
            long count = 0;
            while (rs.next()) {
                String str = rs.getString(fieldname);
                if (str != null) {
                    count += str.split("\\s+").length;
                    //  System.out.println(str + " split#:" + count);
                }
            }
            return count;
        } catch (SQLException ex) {
            Logger.getLogger(BaseIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    private Properties prop = new Properties();
    protected String query;
    protected String idQuery;
    protected String indexDirectory;
    protected String trecFile;
    protected String queryFile;
    protected String resultFile;
    protected String table;
    protected List<Field> fields;
    protected Map<String, Field> fieldsMap;
    File indexFile = null;
    File trecCorpusFile = null;

    public BaseIndex(String table) {
        this.table = table;
        acceptList = new ArrayList<>();
        load();
    }

    public void load() {
        try {
            //load a properties file
            String filePath = AppConfig.BASE_DIR + "/" + getTable() + ".properties";
            prop.load(new FileInputStream(filePath));

            //get the property value and print it out
            this.table = prop.getProperty(PROP_TABLE);
            this.query = prop.getProperty(PROP_QUERY);
            this.idQuery = prop.getProperty(PROP_ID_QUERY);
            this.indexDirectory = prop.getProperty(PROP_INDEX_DIRECTORY);
            this.queryFile = prop.getProperty(PROP_QUERY_FILE);
            this.resultFile = prop.getProperty(PROP_RESULT_FILE);
            this.trecFile = prop.getProperty(PROP_TREC_FILE);

            String fieldsStr = prop.getProperty(PROB_FIELDS);
            String[] split = fieldsStr.split(",");
            fields = new ArrayList<>();
            fieldsMap = new HashMap<>();
            for (String field : split) {
                Field f = new Field(field);
                System.out.println(PROB_FIELD + "." + f.getName() + "." + PROB_FIELD_VOCAB_COUNT);
                System.out.println("Field:" + prop.getProperty(PROB_FIELD + "." + f.getName() + "." + PROB_FIELD_VOCAB_COUNT));
                f.setVocabCount(Long.parseLong(prop.getProperty(PROB_FIELD + "." + f.getName() + "." + PROB_FIELD_VOCAB_COUNT)));
                f.setWordCount(Long.parseLong(prop.getProperty(PROB_FIELD + "." + f.getName() + "." + PROB_FIELD_WORD_COUNT)));
                fieldsMap.put(f.getName(), f);
                fields.add(f);
            }

            System.out.println("Index " + getTable() + " loaded from " + filePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public double getCollectionScore(String term, Field f, Long sumDF, Long sumWordCount) {
        double score = 0;
        score = AppConfig.SMOOTHING_FACTOR * (this.getDF(f.getName(), term) / f.getWordCount())
                + (1 - AppConfig.SMOOTHING_FACTOR) * (sumDF * 1.0 / sumWordCount);
        return score;
    }

    public void save() {
        try {
            //set the properties value
            prop.setProperty(PROP_TABLE, getTable());
            prop.setProperty(PROP_QUERY, getQuery());
            prop.setProperty(PROP_ID_QUERY, getIdQuery());

            prop.setProperty(PROP_INDEX_DIRECTORY, getIndexDirectory());
            prop.setProperty(PROP_QUERY_FILE, getQueryFile());
            prop.setProperty(PROP_TREC_FILE, getTrecFile());
            prop.setProperty(PROP_RESULT_FILE, getResultFile());

            String fieldsStr = "";
            for (Field f : getFields()) {
                fieldsStr += "," + f.getName();
                prop.setProperty(PROB_FIELD + "." + f.getName() + "." + PROB_FIELD_VOCAB_COUNT, f.getVocabCount() + "");
                prop.setProperty(PROB_FIELD + "." + f.getName() + "." + PROB_FIELD_WORD_COUNT, f.getWordCount() + "");
            }
            prop.setProperty(PROB_FIELDS, fieldsStr.substring(1));
            String filePath = AppConfig.BASE_DIR + "/" + getTable() + ".properties";
            prop.store(new FileOutputStream(filePath), null);
            System.out.println("Properties file of " + getTable() + " saved in " + filePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public BaseIndex() {
    }

    public List<QueryResult> doQuery(String query, Query objQ) {
        File queryFile = null;
        try {
            String randNumber = Math.floor((Math.random() * 100000) % 37) + "";
            queryFile = new File(AppConfig.TEMP_DIR + "/" + "t" + randNumber + ".query");
            String queries = "{ \"queries\" : [" + "{ \"number\" : \"1\", \"text\" : \"" + query + "\"}\n" + "]}\n";
            Utility.copyStringToFile(queries, queryFile);
            return doQueryOverFile(queryFile.getAbsolutePath(), objQ);
        } catch (IOException ex) {
            Logger.getLogger(BaseIndex.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BaseIndex.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (queryFile != null) {
                queryFile.delete();
            }
        }
        return null;
    }

//    private List<QueryResult> doQuery() {
//        return doQueryOverFile(getQueryFile());
//    }
    public void startSearchWebInterface() {
        try {
            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayStream);
            new App().run(new String[]{"search", "--index=" + this.getIndexDirectory(), this.getQueryFile(), "--port=5000"}, printStream);
            System.out.println("output:\n" + byteArrayStream.toString());
        } catch (Exception ex) {
            Logger.getLogger(BaseIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getBM25Query(String query) {
        try {
            DiskIndex index = new DiskIndex(this.getIndexDirectory());
            Parameters wMap = new Parameters();
            Parameters bMap = new Parameters();
            Parameters fMap = new Parameters();

//            fMap.set("weights", wMap);
            fMap.set("smoothing", bMap);
            //          fMap.set("K", 1.2);

            wMap.set("name", 3.7);

            // Set smoothing per field
            //         bMap.set("name", 0.3);
            bMap.set("description", 0.8);

            // Now set it to the retrieval
            Parameters p = new Parameters();
            p.set("bm25f", fMap);

            String[] fields = {"name"};
            p.set("fields", Arrays.asList(fields));
            LocalRetrieval retrieval = new LocalRetrieval(index, p);

            Parameters qp = new Parameters();
            BM25FTraversal traversal = new BM25FTraversal(retrieval, qp);
            Node q1 = StructuredQuery.parse(query);
            System.out.println("q1:\n" + q1.toString());
            Node q2 = StructuredQuery.copy(traversal, q1);
            return q2.toString();
        } catch (Exception ex) {
            Logger.getLogger(BaseIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<QueryResult> runQuery_basic(Query query, String ranking) {
        String qstr = "";
        String wstr = "";
        if (ranking.equals(Constants.METHOD_PRMS) || ranking.equals(Constants.METHOD_LM)) {
            wstr = "#combine";
        } else if (ranking.equals(Constants.METHOD_IR_STYLE)) {
            wstr = "#bm25fcomb";
        } else {
            System.out.println("Unknow Ranking Method!");
        }
        int attr_count = 0;
        for (QueryStatement st : query.getStatements()) {
            String str = " #inside(" + st.getValue() + "  #field:" + Constants.attribute.get(st.getAttribute()) + "()) ";
            qstr += str;
            wstr += ":" + attr_count + "=" + st.getWeight();
            attr_count++;
        }
        if (ranking.equals(Constants.METHOD_IR_STYLE)) {
            wstr = "#bm25fcomb";
        }
        qstr = wstr + "(" + qstr + ")";
        System.out.println("Query:" + qstr);
        List<QueryResult> result = doQuery(qstr, query);
        return result;
    }

    @Override
    public List<QueryResult> retrieve(Query query, String ranking) {
        if (this.accept(query.getAttributesList())) {
            return runQuery_basic(query, ranking);
        } else {
            System.out.println("Query# " + query.getId() + " [attributes " + query.getAttributesList() + "] rejected");
        }
        return null;
    }

    public void build() {
        try {
            indexFile = new File(this.getIndexDirectory());
            trecCorpusFile = new File(this.getTrecFile());
            List<String> params = new ArrayList<>();
            params.addAll(Arrays.asList("build", "--indexPath=" + indexFile.getAbsolutePath(), "--inputPath=" + trecCorpusFile.getAbsolutePath(), "--server=true", "--galagoJobDir=" + AppConfig.TEMP_DIR + "galagoJob"));
            for (Field field : this.getFields()) {
                String str = "--tokenizer/fields+" + field.getName();
                params.add(str);
            }
            App.main(params.toArray(new String[0]));
            String str = "";
            for (String str1 : params) {
                str += str1 + " ";
            }
            System.out.println("command:\n" + str);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(BaseIndex.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(BaseIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<QueryResult> doQueryOverFile(String queryFilePath, Query objQ) {
        try {
            ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayStream);
            new App().run(new String[]{"batch-search", "--index=" + this.getIndexDirectory(), "--requested=" + AppConfig.QUERY_MAX_RESULTS, "--casefold=true", queryFilePath}, printStream);
            String output = byteArrayStream.toString();
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.getResultFile(), true));
            final List<QueryResult> parseOutput = QueryResult.parseOutput(this, output, objQ);
            writer.write(output);
            writer.close();
            printStream.close();
            byteArrayStream.close();
            return parseOutput;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(BaseIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<QueryResult> getResult() {
        try {
            List<QueryResult> list = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(this.getResultFile()));
            String line = reader.readLine();
            list.add(new QueryResult());
            while (line != null) {
                line = reader.readLine();
            }
            return list;
        } catch (IOException ex) {
            Logger.getLogger(BaseIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Long getVocabCount(String fieldname) {
        Field f = fieldsMap.get(fieldname);
        return f.getVocabCount();
    }

    @Override
    public Long calcVocabCount(String fieldName) {
        long vocabCount = 0;
        String keyStr = "field.porter." + fieldName;
        String input = "--part+" + keyStr;
        Parameters parameters = getStat(input, this);
        Parameters innerParam = parameters.getMap(keyStr);
        vocabCount = innerParam.getLong("statistics/vocabCount");
        return vocabCount;
    }

    public void generateTrecFile(Connection conn) {
        BufferedWriter writer = null;
        try {
            int docCount = 0;
            trecCorpusFile = new File(this.getTrecFile());
            writer = new BufferedWriter(new FileWriter(trecCorpusFile, true));
            String sql = this.getQuery();
            Statement stmt = conn.createStatement();
            conn.setAutoCommit(false);
            stmt.setFetchSize(AppConfig.FETCH_SIZE);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                docCount++;
                String trecInnerStr = "";
                String trecStr = "";
                for (Field field : this.getFields()) {
                    if (rs.getString(field.getName()) != null) {
                        trecInnerStr += "<" + field.getName() + ">" + rs.getString(field.getName()) + "</" + field.getName() + ">";
                    }
                    trecStr = trecDocument(rs.getString("ID"), trecInnerStr);
                }
                writer.write(trecStr);
            }
            System.out.println("Docs#:" + docCount);
        } catch (IOException ex) {
            Logger.getLogger(BaseIndex.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BaseIndex.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(BaseIndex.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTrecFile() {
        return trecFile;
    }

    public void setTrecFile(String trecDirectory) {
        this.trecFile = trecDirectory;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getIdQuery() {
        return idQuery;
    }

    public void setIdQuery(String idQuery) {
        this.idQuery = idQuery;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getIndexDirectory() {
        return indexDirectory;
    }

    public void setIndexDirectory(String indexDirectory) {
        this.indexDirectory = indexDirectory;
    }

    public String getQueryFile() {
        return queryFile;
    }

    public void setQueryFile(String queryFile) {
        this.queryFile = queryFile;
    }

    public String getResultFile() {
        return resultFile;
    }

    public void setResultFile(String resultFile) {
        this.resultFile = resultFile;
    }
    private static String PROP_TABLE = "table";
    private static String PROP_QUERY = "query";
    private static String PROP_ID_QUERY = "idQuery";
    private static String PROP_INDEX_DIRECTORY = "indexDirectory";
    private static String PROP_TREC_FILE = "trecFile";
    private static String PROP_QUERY_FILE = "queryFile";
    private static String PROP_RESULT_FILE = "resultFile";
    private static String PROB_FIELDS = "fields";
    private static String PROB_FIELD = "field";
    private static String PROB_FIELD_VOCAB_COUNT = "vocabCount";
    private static String PROB_FIELD_WORD_COUNT = "wordCount";
}

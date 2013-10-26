/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app;

import qcap.app.query.Query;
import qcap.app.utils.IndexUtil;
import qcap.app.utils.ScoringUtil;
import qcap.app.utils.CSVWriter;
import qcap.app.query.QueryResult;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lemurproject.galago.tupleflow.Parameters;
import qcap.app.retrieval.*;

/**
 *
 * @author Aale
 */
public class IndexTester {

    static Map<String, BaseIndex> indsmap = new HashMap<>();
    static String[] tables = {/*TBL_PERSON, TBL_PROFESSION,*/Constants.TBL_CHILDREN, Constants.TBL_EDUCATION, Constants.TBL_EMPLOYMENT, Constants.TBL_ETHNICITY, Constants.TBL_GENDER, Constants.TBL_NATIONALITY, Constants.TBL_PARENT, Constants.TBL_PLACES_LIVED, Constants.TBL_PLACE_OF_BIRTH, Constants.TBL_QUOTATION, Constants.TBL_RELIGION, Constants.TBL_SIBLING, Constants.TBL_SPOUSE};

    public static void main(String[] args) {
//        testPerson_Continent(0, "Person-Continent-1.txt", "person-400-1.txt");
//        buildTables(Constants.VI_PERSON_AFRICA, true);
//        buildTables(Constants.VI_PERSON_ANTARCTICA, true);
//        buildTables(Constants.VI_PERSON_ASIA, true);
//        buildTables(Constants.VI_PERSON_AUSTRALIA, true);
//        buildTables(Constants.VI_PERSON_EURASIA, true);
//        buildTables(Constants.VI_PERSON_EUROPE, true);
//        buildTables(Constants.VI_PERSON_NORTH_AMERICA, true);
//        buildTables(Constants.VI_PERSON_SOUTH_AMERICA, true);
//        if (1 == 1) {
//            return;
//        }
        //testWebInterface();
        //testBM25F();
        // testQuerySplitConsistancy();
//        testPersonAll_US_NONUS(0);
//        if (1 == 1) {
//            return;
//        }
        //       UpdateVWCountAllTables();
//        testTVAll(0);
        //      buildTables(Constants.TBL_SUBJECT, false);
        //buildTables(Constants.TBL_CHARACTER, false);
//        if (1 == 1) {
//            return;
//        }
//        buildTables(TBL_PERSON, true);
//        buildTables(VI_PERSON_NOT_USA, true);
//        buildTables(VI_PERSON_USA, true);
//        
//        buildTables(TBL_PROFESSION, false);
//        buildTables(TBL_ETHNICITY, false);
//        buildTables(TBL_GENDER, false);
//        buildTables(TBL_NATIONALITY, false);
//        buildTables(TBL_PLACE_OF_BIRTH, false);
//        buildTables(TBL_RELIGION, false);
//        
//        buildTables(Constants.TBL_BOOK, true);
//        buildTables(Constants.TBL_AUTHOR, false);
//        buildTables(Constants.TBL_CHARACTER, false);
//        buildTables(Constants.TBL_SUBJECT, false);
//        buildTables(Constants.TBL_TV, true);
//        buildTables(Constants.TBL_SEASON, false);
//        buildTables(Constants.TBL_NETWORK, false);
//        buildTables(Constants.TBL_PROG_GENRE, false);
        //testPersonAll(0);
//        buildTables(Constants.VI_TV_ANIMATION, true);
//        buildTables(Constants.VI_TV_COMEDY, true);
//        buildTables(Constants.VI_TV_DOCUMENTARY, true);
//        buildTables(Constants.VI_TV_DRAMA, true);
//        buildTables(Constants.VI_TV_OTHER, true);
//        buildTables(Constants.VI_TV_REALITY, true);
        if (args.length > 0) {
            System.out.println("offset:" + args[0]);
            int offset = Integer.parseInt(args[0]);
            //testPersonAll_US_NONUS(offset);
            testPerson_Continent(offset, "Person-Continent-1.txt", "person-400-1.txt");
            //testTVAll(offset);
        } else {
            Query.getQueriesCount(Constants.STYPE_TV);
        }

        //testDF();
        //  testWebInterface();
        //buildTables();
        //   testPersonUSA();
//   testOneQuery();
        //  testSomeQuery();
//       testMultiIndex();
        //   for (int i = 0; i < tables.length; i++) {
        //       indsmap.put(tables[i], getIndexMetadata(tables[i]));
        //   }
        //testCountWord();
        //  testSingleQuery();
        //   saveIndexes();
//        testStat();
//
//        IndexUtil indbuilder = new IndexUtil();
//        Connection conn = new DBManager().getConnection();
//        for (BaseIndex ind : indsmap.values()) {
//            ind.generateTrecFile(conn);
//            ind.build();
        //indbuilder.doQuery(ind);
        //indbuilder.doSearch(ind);
        //   }
    }

    public static void testBM25F() {
        String query = "#bm25f(book)";
        BaseIndex ind = new BaseIndex(Constants.TBL_TV);
        System.out.println("Q::\n" + ind.getBM25Query(query));

    }

    public static void testTVGenreDecomposition(int offset) {
        Collection<Query> queries = Query.findBySemanticType(Constants.STYPE_TV, offset, AppConfig.LIMIT_QUERY);
        System.out.println(queries.size() + " query fetched for execute");
        JoinIndex index = new JoinIndex();
        List tv_accept_list = Arrays.asList("tv_program_name", "tv_program_description");
        //Add Core Index
        Index animationIndex = new BaseIndex(Constants.VI_TV_ANIMATION);
        animationIndex.setAcceptList(tv_accept_list);

        Index comedyIndex = new BaseIndex(Constants.VI_TV_COMEDY);
        comedyIndex.setAcceptList(tv_accept_list);

        Index documentryIndex = new BaseIndex(Constants.VI_TV_DOCUMENTARY);
        documentryIndex.setAcceptList(tv_accept_list);

        Index dramaIndex = new BaseIndex(Constants.VI_TV_DRAMA);
        dramaIndex.setAcceptList(tv_accept_list);

        Index otherIndex = new BaseIndex(Constants.VI_TV_OTHER);
        otherIndex.setAcceptList(tv_accept_list);

        Index realityIndex = new BaseIndex(Constants.VI_TV_REALITY);
        realityIndex.setAcceptList(tv_accept_list);

        UnionIndex tvIndex = new UnionIndex();
        tvIndex.setChildren(Arrays.asList(animationIndex, comedyIndex,
                documentryIndex, dramaIndex, otherIndex, realityIndex));
        tvIndex.setAcceptList(tv_accept_list);

        index.setCore(tvIndex);

        BaseIndex seasonIndex = new BaseIndex(Constants.TBL_SEASON);
        seasonIndex.setAcceptList(Arrays.asList("seasons"));
        index.addSideIndex(seasonIndex);

//        BaseIndex networkIndex = new BaseIndex(TBL_NETWORK);
//        networkIndex.setAcceptList(Arrays.asList("original_network"));
//        index.addSideIndex(networkIndex);
//
//        BaseIndex genreIndex = new BaseIndex(TBL_PROG_GENRE);
//        genreIndex.setAcceptList(Arrays.asList("tv_program_genre"));
//        index.addSideIndex(genreIndex);
        execBatchQueries(queries, index, AppConfig.RESULT_DIR + "TV-LM-Genre2-10-3-2013-2.txt");

    }

    public static void UpdateVWCountAllTables() {
        updateVWCountIndex(Constants.TBL_PERSON, true);
        updateVWCountIndex(Constants.VI_PERSON_NOT_USA, true);
        updateVWCountIndex(Constants.VI_PERSON_USA, true);
        updateVWCountIndex(Constants.TBL_PROFESSION, false);
        updateVWCountIndex(Constants.TBL_ETHNICITY, false);
        updateVWCountIndex(Constants.TBL_GENDER, false);
        updateVWCountIndex(Constants.TBL_NATIONALITY, false);
        updateVWCountIndex(Constants.TBL_PLACE_OF_BIRTH, false);
        updateVWCountIndex(Constants.TBL_RELIGION, false);

        updateVWCountIndex(Constants.TBL_BOOK, true);
        updateVWCountIndex(Constants.TBL_AUTHOR, false);
        updateVWCountIndex(Constants.TBL_CHARACTER, false);
        updateVWCountIndex(Constants.TBL_SUBJECT, false);

        updateVWCountIndex(Constants.TBL_TV, true);
        updateVWCountIndex(Constants.TBL_SEASON, false);
        //  updateVWCountIndex(Constants.TBL_NETWORK, false);
        //  updateVWCountIndex(Constants.TBL_PROG_GENRE, false);

    }

    public static void testRetrieve() {
        JoinIndex ind = new JoinIndex();
        Index personInd = new BaseIndex(Constants.TBL_PERSON);
        ind.setAcceptList(Arrays.asList("person_name", "person_description"));
        ind.setCore(personInd);
        Index profInd = new BaseIndex(Constants.TBL_PROFESSION);
        profInd.setAcceptList(Arrays.asList("profession"));
        ind.addSideIndex(profInd);
        Index spouseInd = new BaseIndex(Constants.TBL_SPOUSE);
        spouseInd.setAcceptList(Arrays.asList("spouse_s"));
        ind.addSideIndex(spouseInd);

        Collection<Query> queries = Query.findBySemanticType("person"); //jimmy hoffa
        for (Query q : queries) {
        }
    }

    public static void testSingleQuery() {
//        IndexUtil indbuilder = new IndexUtil();
//        // Collection<Query> queries = Query.findById(47245); // 2 live stew
//        Collection<Query> queries = Query.findById(42409); //jimmy hoffa
//        for (Query q : queries) {
//            List<QueryResult> results = indbuilder.runQuery(q);
//            System.out.println("Results#:" + results.size());
//            System.out.println("P@K:" + ScoringUtil.precisionAtK(results, q, 3));
//            System.out.println("MRR:" + ScoringUtil.MRR(results, q));
//        }
    }

    public static void testOneQuery() {
        Index ind = new BaseIndex(Constants.TBL_PERSON);
        ind.setAcceptList(Arrays.asList("person_name", "person_description"));
        Collection<Query> queries = Query.findById(42409); //jimmy hoffa
        for (Query q : queries) {
            List<QueryResult> results = ind.retrieve(q, Constants.METHOD_PRMS);
            if (results != null) {
                System.out.println("Query:" + q.getStatements());
                System.out.println("Results#:" + results.size());
                System.out.println("P@K:" + ScoringUtil.precisionAtK(results, q, 3));
                System.out.println("MRR:" + ScoringUtil.MRR(results, q));

            }
        }
    }

    public static void testSomeQuery() {
        Collection<Query> queries = Query.findBySemanticType("person");
        Index index = new BaseIndex(Constants.TBL_PERSON);
        index.setAcceptList(Arrays.asList("person_name", "person_description"));
        execBatchQueries(queries, index, "ABC");
    }

    public static void testPersonAll_US_NONUS(int offset) {

        Collection<Query> queries = Query.findBySemanticType(Constants.STYPE_PERSON, offset, AppConfig.LIMIT_QUERY);
        //Collection<Query> queries = Query.findById(44002);
        System.out.println(queries.size() + " query fetched for execute");
        JoinIndex index = new JoinIndex();

        //Add Core Index - US - NON US
        Index personUSIndex = new BaseIndex(Constants.VI_PERSON_USA);
        personUSIndex.setAcceptList(Arrays.asList("person_name", "person_description"));
        Index personNonUSIndex = new BaseIndex(Constants.VI_PERSON_NOT_USA);
        personNonUSIndex.setAcceptList(Arrays.asList("person_name", "person_description"));
        UnionIndex personIndex = new UnionIndex();
        personIndex.setChildren(Arrays.asList(personUSIndex, personNonUSIndex));
        personIndex.setAcceptList(Arrays.asList("person_name", "person_description"));

        index.setCore(personIndex);

        //Add Side Index - Profession
        BaseIndex profIndex = new BaseIndex(Constants.TBL_PROFESSION);
        profIndex.setAcceptList(Arrays.asList("profession"));
        index.addSideIndex(profIndex);

        //Add Side Index - Nationality
        BaseIndex nationalityIndex = new BaseIndex(Constants.TBL_NATIONALITY);
        nationalityIndex.setAcceptList(Arrays.asList("nationality"));
        index.addSideIndex(nationalityIndex);

        //Add Side Index - Gender
        BaseIndex genderIndex = new BaseIndex(Constants.TBL_GENDER);
        genderIndex.setAcceptList(Arrays.asList("gender"));
        index.addSideIndex(genderIndex);

        //Add Side Index - Place of Birth
        BaseIndex placeOfBirthIndex = new BaseIndex(Constants.TBL_PLACE_OF_BIRTH);
        placeOfBirthIndex.setAcceptList(Arrays.asList("place_of_birth"));
        index.addSideIndex(placeOfBirthIndex);

        //Add Side Index - Ethnicity
        BaseIndex ethnicityIndex = new BaseIndex(Constants.TBL_ETHNICITY);
        ethnicityIndex.setAcceptList(Arrays.asList("ethnicity"));
        index.addSideIndex(ethnicityIndex);

        //Add Side Index - Gender
        BaseIndex religionIndex = new BaseIndex(Constants.TBL_RELIGION);
        religionIndex.setAcceptList(Arrays.asList("religion"));
        index.addSideIndex(religionIndex);

        execBatchQueries(queries, index, AppConfig.RESULT_DIR + "Person-LM-CORI-US-10-3-2013.txt");
    }

    public static void testPerson_Continent(int offset, String resultFileName, String queryFile) {

        //Collection<Query> queries = Query.findBySemanticType(Constants.STYPE_PERSON, offset, AppConfig.LIMIT_QUERY);
        //Collection<Query> queries = Query.findById(49100);
        Collection<Query> queries = Query.loadQueryFromFile(AppConfig.QUERY_DIR + queryFile, offset, AppConfig.LIMIT_QUERY);
        System.out.println(queries.size() + " query fetched for execute");
        JoinIndex index = new JoinIndex();

        //Add Core Index - US - NON US
        Index africaIndex = new BaseIndex(Constants.VI_PERSON_AFRICA);
        africaIndex.setAcceptList(Arrays.asList("person_name", "person_description"));
//        Index antarcticaIndex = new BaseIndex(Constants.VI_PERSON_ANTARCTICA);
//        antarcticaIndex.setAcceptList(Arrays.asList("person_name", "person_description"));
        Index asiaIndex = new BaseIndex(Constants.VI_PERSON_ASIA);
        asiaIndex.setAcceptList(Arrays.asList("person_name", "person_description"));
//        Index australiaIndex = new BaseIndex(Constants.VI_PERSON_AUSTRALIA);
//        australiaIndex.setAcceptList(Arrays.asList("person_name", "person_description"));
        Index eurasiaIndex = new BaseIndex(Constants.VI_PERSON_EURASIA);
        eurasiaIndex.setAcceptList(Arrays.asList("person_name", "person_description"));
        Index europeIndex = new BaseIndex(Constants.VI_PERSON_EUROPE);
        europeIndex.setAcceptList(Arrays.asList("person_name", "person_description"));
        Index northAmericaIndex = new BaseIndex(Constants.VI_PERSON_NORTH_AMERICA);
        northAmericaIndex.setAcceptList(Arrays.asList("person_name", "person_description"));
//        Index southAmericaIndex = new BaseIndex(Constants.VI_PERSON_SOUTH_AMERICA);
//        southAmericaIndex.setAcceptList(Arrays.asList("person_name", "person_description"));

        UnionIndex personIndex = new UnionIndex();
        personIndex.setChildren(Arrays.asList(africaIndex, asiaIndex, eurasiaIndex, europeIndex,
                northAmericaIndex));
        personIndex.setAcceptList(Arrays.asList("person_name", "person_description"));

        index.setCore(personIndex);

        //Add Side Index - Profession
        BaseIndex profIndex = new BaseIndex(Constants.TBL_PROFESSION);
        profIndex.setAcceptList(Arrays.asList("profession"));
        profIndex.setPivot(PivotTable.PERSON_PROFESSION_PIVOT);
        index.addSideIndex(profIndex);

        //Add Side Index - Nationality
        BaseIndex nationalityIndex = new BaseIndex(Constants.TBL_NATIONALITY);
        nationalityIndex.setAcceptList(Arrays.asList("nationality"));
        nationalityIndex.setPivot(PivotTable.PERSON_NATIONALITY_PIVOT);
        index.addSideIndex(nationalityIndex);

        //Add Side Index - Gender
        BaseIndex genderIndex = new BaseIndex(Constants.TBL_GENDER);
        genderIndex.setAcceptList(Arrays.asList("gender"));
        genderIndex.setPivot(PivotTable.PERSON_GENDER_PIVOT);
        index.addSideIndex(genderIndex);

        //Add Side Index - Place of Birth
        BaseIndex placeOfBirthIndex = new BaseIndex(Constants.TBL_PLACE_OF_BIRTH);
        placeOfBirthIndex.setAcceptList(Arrays.asList("place_of_birth"));
        placeOfBirthIndex.setPivot(PivotTable.PERSON_PLACE_OF_BIRTH_PIVOT);
        index.addSideIndex(placeOfBirthIndex);

        //Add Side Index - Ethnicity
        BaseIndex ethnicityIndex = new BaseIndex(Constants.TBL_ETHNICITY);
        ethnicityIndex.setAcceptList(Arrays.asList("ethnicity"));
        ethnicityIndex.setPivot(PivotTable.PERSON_ETHNICITY_PIVOT);
        index.addSideIndex(ethnicityIndex);

        //Add Side Index - Gender
        BaseIndex religionIndex = new BaseIndex(Constants.TBL_RELIGION);
        religionIndex.setAcceptList(Arrays.asList("religion"));
        religionIndex.setPivot(PivotTable.PERSON_RELIGION_PIVOT);
        index.addSideIndex(religionIndex);

        execBatchQueries(queries, index, AppConfig.RESULT_DIR + resultFileName);
    }

    public static void testTVAll(int offset) {
        Collection<Query> queries = Query.findBySemanticType(Constants.STYPE_TV, offset, AppConfig.LIMIT_QUERY);
        System.out.println(queries.size() + " query fetched for execute");
        JoinIndex index = new JoinIndex();

        BaseIndex tvIndex = new BaseIndex(Constants.TBL_TV);
        tvIndex.setAcceptList(Arrays.asList("tv_program_name", "tv_program_description"));
        index.setCore(tvIndex);

        BaseIndex seasonIndex = new BaseIndex(Constants.TBL_SEASON);
        seasonIndex.setAcceptList(Arrays.asList("seasons"));
        index.addSideIndex(seasonIndex);

//        BaseIndex networkIndex = new BaseIndex(TBL_NETWORK);
//        networkIndex.setAcceptList(Arrays.asList("original_network"));
//        index.addSideIndex(networkIndex);
//
//        BaseIndex genreIndex = new BaseIndex(TBL_PROG_GENRE);
//        genreIndex.setAcceptList(Arrays.asList("tv_program_genre"));
//        index.addSideIndex(genreIndex);
        execBatchQueries(queries, index, AppConfig.RESULT_DIR + "TV-LM-10-3-2013-2.txt");

    }

    public static void testPersonAll(int offset) {

        Collection<Query> queries = Query.findBySemanticType("person", offset, AppConfig.LIMIT_QUERY);
        System.out.println(queries.size() + " query fetched for execute");
        JoinIndex index = new JoinIndex();

        //Add Core Index - Person
        BaseIndex personIndex = new BaseIndex(Constants.TBL_PERSON);
        personIndex.setAcceptList(Arrays.asList("person_name", "person_description"));
        index.setCore(personIndex);

        //Add Side Index - Profession
        BaseIndex profIndex = new BaseIndex(Constants.TBL_PROFESSION);
        profIndex.setAcceptList(Arrays.asList("profession"));
        index.addSideIndex(profIndex);

        //Add Side Index - Nationality
        BaseIndex nationalityIndex = new BaseIndex(Constants.TBL_NATIONALITY);
        nationalityIndex.setAcceptList(Arrays.asList("nationality"));
        index.addSideIndex(nationalityIndex);

        //Add Side Index - Gender
        BaseIndex genderIndex = new BaseIndex(Constants.TBL_GENDER);
        genderIndex.setAcceptList(Arrays.asList("gender"));
        index.addSideIndex(genderIndex);

        //Add Side Index - Place of Birth
        BaseIndex placeOfBirthIndex = new BaseIndex(Constants.TBL_PLACE_OF_BIRTH);
        placeOfBirthIndex.setAcceptList(Arrays.asList("place_of_birth"));
        index.addSideIndex(placeOfBirthIndex);

        //Add Side Index - Ethnicity
        BaseIndex ethnicityIndex = new BaseIndex(Constants.TBL_ETHNICITY);
        ethnicityIndex.setAcceptList(Arrays.asList("ethnicity"));
        index.addSideIndex(ethnicityIndex);

        //Add Side Index - Gender
        BaseIndex religionIndex = new BaseIndex(Constants.TBL_RELIGION);
        religionIndex.setAcceptList(Arrays.asList("religion"));
        index.addSideIndex(religionIndex);

        execBatchQueries(queries, index, AppConfig.RESULT_DIR + "Person-LM-10-2-2013.txt");
    }

    public static void testQuerySplitConsistancy() {
        Collection<Query> queries = Query.findBySemanticType("person");
        String filePath = AppConfig.RESULT_DIR + "PersonAll-QCheck-All.txt";
        CSVWriter writer = new CSVWriter(filePath);
        System.out.println("Queries#: " + queries.size());
        int countQ = 0;
        for (Query q : queries) {
            System.out.println("Current Q:" + q);
            countQ++;
            writer.append(countQ + ": " + q.getId());
        }
        writer.close();
    }

    public static void testPersonOnlyName() {
        Collection<Query> queries = Query.findBySemanticType("person");
        Index index = new BaseIndex(Constants.TBL_PERSON);
        index.setAcceptList(Arrays.asList("person_name"));
        execBatchQueries(queries, index, "ABC");
    }

    public static void testPersonOnlyDesc() {
        Collection<Query> queries = Query.findBySemanticType("person");
        Index index = new BaseIndex(Constants.TBL_PERSON);
        index.setAcceptList(Arrays.asList("person_description"));
        execBatchQueries(queries, index, "ABC");
    }

    public static void testPersonUSA() {
        Collection<Query> queries = Query.findBySemanticType("person");
        Index index = new BaseIndex(Constants.VI_PERSON_USA);
        index.setAcceptList(Arrays.asList("person_description", "person_name"));
        execBatchQueries(queries, index, "ABC");
    }

    public static void testPersonNotUSA() {
        Collection<Query> queries = Query.findBySemanticType("person");
        Index index = new BaseIndex(Constants.VI_PERSON_NOT_USA);
        index.setAcceptList(Arrays.asList("person_description", "person_name"));
        execBatchQueries(queries, index, "ABC");
    }

    public static double execBatchQueries(Collection<Query> queries, Index index, String resultFilePath) {
        printCurrentTime();
        CSVWriter writer = new CSVWriter(resultFilePath);
        CSVWriter writerRej = new CSVWriter(resultFilePath + ".reject");

        int count = 0;
        int rejected = 0;
        double precision_all_3 = 0;
        double precision_all_5 = 0;
        double precision_all_10 = 0;
        double mrr_all = 0;
        System.out.println("Queries#: " + queries.size());
        int countQ = 0;
        for (Query q : queries) {
            long time1 = System.currentTimeMillis();
            System.out.println("Current Q:" + q);
            //countQ++;
            //writer.append(countQ + ": " + q.getId());
            List<QueryResult> results = index.retrieve(q, Constants.METHOD_LM);
            if (results != null) {
                count++;
                System.out.println("Desired: " + q.getEntityId() + " " + q.getFbid() + " " + q.getId());
                final double precisionAt3 = ScoringUtil.precisionAtK(results, q, 3);
                precision_all_3 += precisionAt3;
                final double precisionAt5 = ScoringUtil.precisionAtK(results, q, 5);
                precision_all_5 += precisionAt5;
                final double precisionAt10 = ScoringUtil.precisionAtK(results, q, 10);
                precision_all_10 += precisionAt10;

                final double MRR = ScoringUtil.MRR(results, q);
                mrr_all += MRR;
                System.out.println("#" + count);
                System.out.println("Query:" + q.getStatements());
                System.out.println("Results#:" + results.size());
                System.out.println("P@3:" + precisionAt3);
                System.out.println("P@5:" + precisionAt5);
                System.out.println("P@10:" + precisionAt10);
                System.out.println("MRR:" + MRR);
                System.out.println();
                System.out.println("Precision3-UpToHere:" + precision_all_3 * 1.00 / count);
                System.out.println("Precision5-UpToHere:" + precision_all_5 * 1.00 / count);
                System.out.println("Precision5-UpToHere:" + precision_all_10 * 1.00 / count);
                System.out.println("MRR-UpToHere:" + mrr_all * 1.00 / count);
                writer.append(q.getId() + "," + precisionAt3 + "," + precisionAt5 + "," + precisionAt10 + "," + MRR);
            } else {
                System.out.println("Query Hasn't any result!");
                writerRej.append(q.getId() + "," + -1 + "," + -1 + "," + -1);
                rejected++;
            }
            long time2 = System.currentTimeMillis();
            System.out.println("Query Running Time (ms):" + (time2 - time1));
        }
        writer.close();
        writerRej.close();
        System.out.println("Count-All:" + count);
        System.out.println("Reject-All:" + rejected);
        System.out.println("Precision-All:" + precision_all_3 * 1.00 / count);
        System.out.println("MRR-All:" + mrr_all * 1.00 / count);
        printCurrentTime();
        return mrr_all * 1.00 / count;
    }

    public static void testStat() {
        IndexUtil indbuilder = new IndexUtil();
        //String input = "--part+field.porter.name";
        //Parameters results = indbuilder.getStat(input, indsmap.get(TBL_PERSON));
        System.out.println("VocabCount:" + indsmap.get(Constants.TBL_PERSON).getVocabCount("name"));
        System.out.println("DF:" + indsmap.get(Constants.TBL_PERSON).getDF("name", "pooya"));
//        for (String key : results.get) {
//            System.out.println(key);
//        }
        //System.out.println("Results/n" + results);
    }

    public static void updateVWCountIndex(String table, boolean desc) {
        BaseIndex index;
        if (desc) {
            index = getIndexMetadataNameDesc(table);
        } else {
            index = getIndexMetadataName(table);
        }
        for (Field f : index.getFields()) {
            f.setVocabCount(index.calcVocabCount(f.getName()));
            f.setWordCount(index.calcWordCount(f.getName()));
        }
        index.save();
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

    public static void printResult(List<ArrayList<String>> list) {
        int count = 1;
        for (ArrayList<String> l : list) {
            System.out.print(count + ") ");
            count++;
            for (String val : l) {
                System.out.print("[" + val + "] ");
            }
            System.out.println();
        }
    }

    private static void testWebInterface() {
        BaseIndex index = new BaseIndex(Constants.TBL_TV);
        index.startSearchWebInterface();
    }

    private static void testDF() {
        BaseIndex index = new BaseIndex(Constants.TBL_PERSON);
        Long df = index.getDF("description", "hossein amir");
        System.out.println(df);
    }

    private static void testBookAll(int offset) {

        Collection<Query> queries = Query.findBySemanticType(Constants.STYPE_BOOK, offset, AppConfig.LIMIT_QUERY);
        System.out.println(queries.size() + " query fetched for execute");
        JoinIndex index = new JoinIndex();

        //core index - book
        BaseIndex bookIndex = new BaseIndex(Constants.TBL_BOOK);
        bookIndex.setAcceptList(Arrays.asList("book_name", "book_description"));
        index.setCore(bookIndex);

        //side index - author
        BaseIndex authorIndex = new BaseIndex(Constants.TBL_AUTHOR);
        authorIndex.setAcceptList(Arrays.asList("book_author"));
        index.addSideIndex(authorIndex);

        BaseIndex subjectIndex = new BaseIndex(Constants.TBL_SUBJECT);
        subjectIndex.setAcceptList(Arrays.asList("book_subjects"));
        index.addSideIndex(subjectIndex);

        BaseIndex characterIndex = new BaseIndex(Constants.TBL_CHARACTER);
        characterIndex.setAcceptList(Arrays.asList("book_characters"));
        index.addSideIndex(characterIndex);

        execBatchQueries(queries, index, AppConfig.RESULT_DIR + "Book-LM-10-3-2013.txt");

    }

    private static void printCurrentTime() {
        Date date = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String dateFormatted = formatter.format(date);
        System.out.println("Current Time: " + dateFormatted);
    }
}

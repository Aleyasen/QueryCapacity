/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.query;

import com.sun.org.apache.xerces.internal.dom.AttributeMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import qcap.app.Constants;
import qcap.app.DBManager;
import qcap.app.retrieval.Index;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.javaml.tools.sampling.SamplingMethod;
import net.sf.javaml.tools.sampling.SubSampling;
import qcap.app.AppConfig;
import qcap.app.utils.CSVWriter;

/**
 *
 * @author aleyase2-admin
 */
public class Query {

    Integer id;
    String entityId;
    Integer frequency;
    String text;
    Integer attributesCount;
    String semanticType;
    List<QueryStatement> statements;
    String fbid;

    public Query() {
        statements = new ArrayList<>();
    }
    private static String baseQuery = "SELECT q.id qid, entity_id, frequency, q.text qtext, attributes_count, semantic_type, fbid, s.id sid, attribute, value "
            + "FROM tbl_query q, tbl_statement s "
            + "WHERE s.query = q.id ";

    public static void main(String[] args) {
        //  System.out.println(findById(47245));
        //   Query.calcQueriesStats("person");
        //System.out.println(Query.getSampleQueries("person", 2));
        String semanticType = "person";
        int size = 400;
        String filePath = AppConfig.QUERY_DIR + semanticType + "-" + size + "-1.txt";
        // Query.storeSampleQueriesInFile(semanticType, 400, filePath);
        System.out.println(Query.loadQueryFromFile(filePath).size());
    }

    public static void storeSampleQueriesInFile(String semanticType, int size, String filePath) {
        List<Query> queries = Query.getSampleQueries(semanticType, size);
        CSVWriter writer = new CSVWriter(filePath);
        for (Query q : queries) {
            writer.append(q.getId() + "");
        }
        System.out.println("Query# " + queries.size() + " saved in " + filePath);
        writer.close();
    }

    public static List<Query> loadQueryFromFile(String filePath, int offset, int limit) {
        List<Query> queries = Query.loadQueryFromFile(filePath);
        List<Query> sub_queries = new ArrayList<>();

        int upper_bound = Math.min(limit + offset, queries.size());
        for (int i = offset; i < upper_bound; i++) {
            sub_queries.add(queries.get(i));
        }
        System.out.println("[loadQueryFromFile] Selected Queries for Execution:");
        System.out.println(sub_queries);

        return sub_queries;
    }

    public static List<Query> loadQueryFromFile(String filePath) {
        try {
            List<Query> allQueries = Query.findAll();
            List<Query> loadedQueries = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            String everything = sb.toString();
            String[] split = everything.split("\\n");
            List<String> ids = Arrays.asList(split);
            for (Query q : allQueries) {
                if (ids.contains(q.getId() + "")) {
                    loadedQueries.add(q);
                }
            }
            return loadedQueries;
        } catch (IOException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Double calcPRMS(Index ind) {
        Double sum = 0.0;
        for (QueryStatement st : statements) {
            Long df = ind.getDF(Constants.attribute.get(st.attribute), st.getValue());
            Long wc = ind.getWordCount(Constants.attribute.get(st.attribute));
            st.weight = df * 1.0 / wc;
            sum += st.weight;
        }
        return sum;
    }

    public void setAllWeight(Double weight) {
        for (QueryStatement st : statements) {
            st.weight = weight;
        }
    }

    public void updateWeight(Double sum) {
        for (QueryStatement st : statements) {
            st.weight = st.weight / sum;
        }
    }

    public Double getTotalWeight() {
        Double total = 0.0;
        for (QueryStatement st : statements) {
            total += st.weight;
        }
        return total;
    }

    public void addQueryStatement(QueryStatement statement) {
        if (statements == null) {
            statements = new ArrayList<>();
        }
        statements.add(statement);
    }

    public List<String> getAttributesList() {
        List<String> list = new ArrayList<>();
        for (QueryStatement st : statements) {
            list.add(st.getAttribute());
        }
        return list;
    }

    public static List<Query> findById(Integer id) {
        String query = baseQuery + " and q.id=" + id;
        return find(query);
    }

    public static List<Query> findBySemanticType(String semanticType) {
        String query = baseQuery + " and q.semantic_type='" + semanticType + "'";
        System.out.println(query);
        return find(query);
    }

    public static List<Query> findBySemanticType(String semanticType, int offset, int limit) {
        List<Query> queries = Query.findBySemanticType(semanticType);
        List<Query> sub_queries = new ArrayList<>();

        int upper_bound = Math.min(limit + offset, queries.size());
        for (int i = offset; i < upper_bound; i++) {
            sub_queries.add(queries.get(i));
        }
        return sub_queries;
    }

    public static List<Query> findAll() {
        String query = baseQuery;
        return find(query);
    }

    private static List<Query> find(String query) {
        try {
            Map<Integer, Query> map = new HashMap<>();
            ResultSet rs = DBManager.execQuery(query);
            while (rs.next()) {
                int queryId = rs.getInt("qid");
                if (!map.containsKey(queryId)) {
                    Query newQuery = new Query();
                    newQuery.setId(rs.getInt("qid"));
                    newQuery.setEntityId(rs.getString("entity_id"));
                    newQuery.setFrequency(rs.getInt("frequency"));
                    newQuery.setText(rs.getString("qtext"));
                    newQuery.setFbid(rs.getString("fbid"));
                    newQuery.setAttributesCount(rs.getInt("attributes_count"));
                    newQuery.setSemanticType(rs.getString("semantic_type"));
                    map.put(queryId, newQuery);
                }
                Query q = map.get(queryId);
                if (q.getStatements() == null) {
                    q.setStatements(new ArrayList<QueryStatement>());
                }
                QueryStatement st = new QueryStatement();
                st.setId(rs.getInt("sid"));
                st.setAttribute(rs.getString("attribute"));
                st.setValue(rs.getString("value"));
                q.getStatements().add(st);
            }
            return new ArrayList(map.values());
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Query generateSubQueries(Query query, Index index) {
        Query newq = new Query();
        int attCount = 0;
        for (QueryStatement qst : query.getStatements()) {
            if (index.accept(qst.getAttribute())) {
                newq.addQueryStatement(qst);
                attCount++;
            }
        }
        if (attCount == 0) {
            return null;
        }
        newq.setAttributesCount(attCount);
        newq.setEntityId(query.getEntityId());
        newq.setFbid(query.getFbid());
        newq.setFrequency(query.getFrequency());
        newq.setId(query.getId());
        newq.setSemanticType(query.getSemanticType());
        return newq;
    }

    public static List<Query> getSampleQueries(String semanticType, int size) {
        List<Query> queries = Query.findBySemanticType(semanticType);
        List<Query> subSampleQueries = new ArrayList<>();
        List<Integer> idList = new ArrayList<>();
        for (Query query : queries) {
            idList.add(query.getId());
        }
        SamplingMethod samplingMethod = new SubSampling();
        List<Integer> idSubSampleList = samplingMethod.sample(idList, size);
        for (Query query : queries) {
            if (idSubSampleList.contains(query.getId())) {
                subSampleQueries.add(query);
            }
        }
        return subSampleQueries;
    }

//    protected void getFieldWeightsByStep(int step) {
//		this.ws.clear();
//		if(step>=0) {
//			int param_id = step%this.total_params;
//			int param_iter = step/this.total_params;
//			lambda = 0.1d+((double)param_iter)*0.1d;
//			if(lambda>0.9d) lambda = 0.9d;
//			if(param_id>0) {
//				for(int i=0; i<this.params.size(); i++) {
//					this.ws.put(this.params.get(i),1.0d*param_iter+1.0d);
//					if(i<=param_id-1) this.ws.put(this.params.get(i),1.0d*param_iter+1.0d+1.0d);
//				}
//			}
//		}
//		else {
//			lambda = 0.1d;
//			for(int i=0; i<this.params.size(); i++) {
//				this.ws.put(this.params.get(i),1.0d);
//			}
//		}
//	}
    public static void trainFieldWeights(String semanticType, List<Query> trainSet, int steps) {
    }

    public static Map<Set<String>, Integer> calcQueriesStats(String semanticType) {
        Map<Set<String>, Integer> stats = new HashMap<Set<String>, Integer>();
        List<Query> queries = Query.findBySemanticType(semanticType);
        for (Query query : queries) {
            Set<String> attrs = new TreeSet<String>(query.getAttributesList());
            if (!stats.containsKey(attrs)) {
                stats.put(attrs, 0);
            }
            stats.put(attrs, stats.get(attrs) + 1);
        }
        //System.out.println(stats);
        for (Set attSet : stats.keySet()) {
            System.out.println(attSet + ":" + stats.get(attSet));
        }
        return stats;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getAttributesCount() {
        return attributesCount;
    }

    public void setAttributesCount(Integer attributesCount) {
        this.attributesCount = attributesCount;
    }

    public String getSemanticType() {
        return semanticType;
    }

    public void setSemanticType(String semanticType) {
        this.semanticType = semanticType;
    }

    public List<QueryStatement> getStatements() {
        return statements;
    }

    public void setStatements(List<QueryStatement> statements) {
        this.statements = statements;
    }

    @Override
    public String toString() {
        return "Query{" + statements + " ,id=" + id + ", entityId=" + entityId + ", frequency=" + frequency + ", text=" + text + ", attributesCount=" + attributesCount + ", semanticType=" + semanticType + ", fbid=" + fbid + "}\n";
    }
}

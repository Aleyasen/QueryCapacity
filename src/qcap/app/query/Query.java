/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.query;

import com.sun.org.apache.xerces.internal.dom.AttributeMap;
import qcap.app.Constants;
import qcap.app.DBManager;
import qcap.app.retrieval.Index;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        System.out.println(findById(47245));
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

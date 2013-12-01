/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author aleyase2
 */
public class QuerySet {

    public List<Query> queries;
    public List<QueryStatement> weights;

    public QuerySet() {
        queries = new ArrayList<Query>();
        weights = new ArrayList<QueryStatement>();
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

    public static QuerySet getQuerySet(List<Query> queries, List<String> attributes) {
        QuerySet qset = new QuerySet();
        for (Query query : queries) {
            if (query.getStatements().size() == attributes.size()) {
                boolean accept = true;
                for (String attr : attributes) {
                    if (!query.getAttributesList().contains(attr)) {
                        accept = false;
                    }
                }
                if (accept) {
                    qset.queries.add(query);
                }
            }
        }
        return qset;
    }
}

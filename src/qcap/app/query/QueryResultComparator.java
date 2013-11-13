/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.query;

import java.util.Comparator;

/**
 *
 * @author aleyase2
 */
public class QueryResultComparator implements Comparator<QueryResult> {

    @Override
    public int compare(QueryResult o1, QueryResult o2) {
        return o1.getScore().compareTo(o2.getScore());
    }
}

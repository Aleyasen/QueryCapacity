/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.utils;

import qcap.app.query.Query;
import qcap.app.query.QueryResult;
import java.util.List;

/**
 *
 * @author aleyase2-admin
 */
public class ScoringUtil {

    public static double precisionAtK(List<QueryResult> results, Query query, int k) {
        if (results == null) {
            System.out.println("Results is null");
            return 0.00;
        }
        int k_min = Math.min(k, results.size());
        for (int i = 0; i < k_min; i++) {
            //System.out.println(results.get(i).getFbid()+" "+query.getFbid());
            if (results.get(i).getFbid().equals(query.getFbid())) {
                return 1.00 / k;
            }
        }
        return 0.00;
    }

    public static double MRR(List<QueryResult> results, Query query) {
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getFbid().equals(query.getFbid())) {
                return 1.00 / (i + 1);
            }
        }
        return 0.00;
    }
}

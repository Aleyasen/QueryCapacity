/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.retrieval;

import qcap.app.query.QueryResult;
import qcap.app.query.Query;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aleyase2-admin
 */
public class Index {

    List<String> acceptList;
    PivotTable pivot;

    public Index(){
        acceptList = new ArrayList<>();
    }
    public PivotTable getPivot() {
        return pivot;
    }

    public void setPivot(PivotTable pivot) {
        this.pivot = pivot;
    }

    public List<String> getAcceptList() {
        return acceptList;
    }

    public void setAcceptList(List<String> acceptList) {
        this.acceptList = acceptList;
    }

//    public List<QueryResult> retrieve(Query query) {
//        return null;
//    }
    public List<QueryResult> retrieve(Query query, String ranking) {
        return null;
    }

    public Long getDF(String fieldName, String term) {
        return null;
    }

    public Long calcWordCount(String fieldname) {
        return null;
    }

    public Long getWordCount(String fieldname) {
        return null;
    }

    public Long getVocabCount(String fieldName) {
        return null;
    }
    
    public Long calcVocabCount(String fieldName) {
        return null;
    }

    public boolean accept(List<String> attributes) {
        for (String attr : attributes) {
            if (!accept(attr)) {
                return false;
            }
        }
        return true;
    }

    public boolean accept(String attribute) {
        for (String attr : acceptList) {
            if (attr.equals(attribute)) {
                return true;
            }
        }
        return false;
    }
}

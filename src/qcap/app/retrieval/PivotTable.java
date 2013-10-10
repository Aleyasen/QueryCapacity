/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.retrieval;

/**
 *
 * @author aleyase2-admin
 */
public class PivotTable {

    String attrCore;
    String attrSide;
    String attrId;
    String tableName;

    public PivotTable(String attrCore, String attrSide, String attrId, String tableName) {
        this.attrCore = attrCore;
        this.attrSide = attrSide;
        this.attrId = attrId;
        this.tableName = tableName;
    }

    public String getAttrCore() {
        return attrCore;
    }

    public String getAttrSide() {
        return attrSide;
    }

    public String getAttrId() {
        return attrId;
    }

    public String getTableName() {
        return tableName;
    }

}

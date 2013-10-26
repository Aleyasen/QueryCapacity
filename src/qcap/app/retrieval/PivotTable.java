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

    public static PivotTable PERSON_CHILDREN_PIVOT = new PivotTable("person", "children", "id", "tbl_person_children");
    public static PivotTable PERSON_PARENT_PIVOT = new PivotTable("person", "parentt", "id", "tbl_person_parent");
    public static PivotTable PERSON_SIBLING_PIVOT = new PivotTable("person", "sibling", "id", "tbl_person_sibling");
    public static PivotTable PERSON_SPOUSE_PIVOT = new PivotTable("person", "spouse", "id", "tbl_person_spouse");
    public static PivotTable PERSON_GENDER_PIVOT = new PivotTable("person", "gender", "id", "tbl_person_gender");
    public static PivotTable PERSON_PROFESSION_PIVOT = new PivotTable("person", "profession", "id", "tbl_person_profession");
    public static PivotTable PERSON_PLACE_OF_BIRTH_PIVOT = new PivotTable("person", "place_of_birth", "id", "tbl_person_place_of_birth");
    public static PivotTable PERSON_PLACES_LIVED_PIVOT = new PivotTable("person", "places_lived", "id", "tbl_person_places_lived");
    public static PivotTable PERSON_NATIONALITY_PIVOT = new PivotTable("person", "nationality", "id", "tbl_person_nationality");
    public static PivotTable PERSON_RELIGION_PIVOT = new PivotTable("person", "religion", "id", "tbl_person_religion");
    public static PivotTable PERSON_ETHNICITY_PIVOT = new PivotTable("person", "ethnicity", "id", "tbl_person_ethnicity");
    public static PivotTable PERSON_QUOTATION_PIVOT = new PivotTable("person", "quotation", "id", "tbl_person_quotation");
    public static PivotTable PERSON_EDUCATION_PIVOT = new PivotTable("person", "education", "id", "tbl_person_education");
    public static PivotTable PERSON_EMPLOYMENT_PIVOT = new PivotTable("person", "employment", "id", "tbl_person_employment");
    
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

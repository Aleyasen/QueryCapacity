package qcap.app;

import java.util.HashMap;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author aleyase2-admin
 */
public class Constants {

    public static Map<String, String> attribute;
    public static Map<String, String> table;
    public static final String STYPE_PERSON = "person";
    public static final String STYPE_BOOK = "book";
    public static final String STYPE_TV = "tv_program";
    public static final String TBL_AUTHOR = "tbl_author";
    public static final String TBL_PROFESSION = "tbl_profession";
    public static final String TBL_PLACES_LIVED = "tbl_places_lived";
    public static final String TBL_NATIONALITY = "tbl_nationality";
    public static final String TBL_CHILDREN = "tbl_children";
    public static final String TBL_PROG_GENRE = "tbl_prog_genre";
    public static final String TBL_ETHNICITY = "tbl_ethnicity";
    public static final String TBL_SEASON = "tbl_season";
    public static final String TBL_SPOUSE = "tbl_spouse";
    public static final String TBL_QUOTATION = "tbl_quotation";
    public static final String TBL_BOOK = "tbl_book";
    public static final String TBL_SIBLING = "tbl_sibling";
    public static final String VI_PERSON_ONLY_DESC = "vi_person_only_desc";
    public static final String TBL_GENDER = "tbl_gender";
    public static final String VI_PERSON_NOT_USA = "vi_person_not_usa";
    public static final String TBL_PLACE_OF_BIRTH = "tbl_place_of_birth";
    public static final String TBL_CHARACTER = "tbl_character";
    public static final String TBL_EDUCATION = "tbl_education";
    public static final String TBL_RELIGION = "tbl_religion";
    public static final String TBL_SUBJECT = "tbl_subject";
    public static final String TBL_EMPLOYMENT = "tbl_employment";
    public static final String TBL_NETWORK = "tbl_network";
    public static final String VI_PERSON_USA = "vi_person_usa";
    public static final String TBL_PARENT = "tbl_parent";
    public static final String TBL_TV = "tbl_tv";
    public static final String VI_PERSON_ONLY_NAME = "vi_person_only_name";
    public static final String TBL_PERSON = "tbl_person";
    public static final String VI_TV_ANIMATION = "vi_tv_animation";
    public static final String VI_TV_COMEDY = "vi_tv_comedy";
    public static final String VI_TV_DOCUMENTARY = "vi_tv_documentary";
    public static final String VI_TV_DRAMA = "vi_tv_drama";
    public static final String VI_TV_OTHER = "vi_tv_other";
    public static final String VI_TV_REALITY = "vi_tv_reality";
    public static final String METHOD_IR_STYLE = "IR-Style";
    public static final String METHOD_PRMS = "PRMS";
    public static final String METHOD_LM = "LM";

    static {
        table = new HashMap<>();
        table.put("person_name", "tbl_person");
        table.put("person_description", "tbl_person");
        table.put("profession", "tbl_profession");
        table.put("spouse_s", "tbl_spouse");
        table.put("children", "tbl_children");
        table.put("parents", "tbl_parent");
        table.put("nationality", "tbl_nationality");
        table.put("education", "tbl_education");
        table.put("quotations", "tbl_quotation");
        table.put("employment_history", "tbl_employment");
        table.put("place_of_birth", "tbl_place_of_birth");
        table.put("ethnicity", "tbl_ethnicity");
        table.put("alias", "tbl_alias");
        table.put("sibling", "tbl_sibling");
        table.put("gender", "tbl_gender");
        table.put("religion", "tbl_religion");
        table.put("places_lived", "tbl_places_lived");

        //tv_program
        table.put("tv_program_name", "tbl_tv");
        table.put("tv_program_description", "tbl_tv");
        table.put("seasons", "tbl_season");
        table.put("original_network", "tbl_network");
        table.put("tv_program_genre", "tbl_prog_genre");

        //book
        table.put("book_name", "tbl_book");
        table.put("book_description", "tbl_book");
        table.put("book_author", "tbl_author");
        // instance2.put("semantic_type", "name");
        table.put("book_subjects", "tbl_subject");
        table.put("book_characters", "tbl_character");

        //person
        attribute = new HashMap<>();
        attribute.put("person_name", "name");
        attribute.put("person_description", "description");
        attribute.put("profession", "name");
        attribute.put("spouse_s", "name");
        attribute.put("children", "name");
        attribute.put("parents", "name");
        attribute.put("nationality", "name");
        attribute.put("education", "name");
        attribute.put("quotations", "name");
        attribute.put("employment_history", "name");
        attribute.put("place_of_birth", "name");
        attribute.put("ethnicity", "name");
        attribute.put("alias", "name");
        attribute.put("sibling", "name");
        attribute.put("gender", "name");
        attribute.put("religion", "name");
        attribute.put("places_lived", "name");

        //tv_program
        attribute.put("tv_program_name", "name");
        attribute.put("tv_program_description", "description");
        attribute.put("seasons", "name");
        attribute.put("original_network", "name");
        attribute.put("tv_program_genre", "name");

        //book
        attribute.put("book_name", "name");
        attribute.put("book_description", "description");
        attribute.put("book_author", "name");
        attribute.put("semantic_type", "name");
        attribute.put("book_subjects", "name");
        attribute.put("book_characters", "name");

    }
}

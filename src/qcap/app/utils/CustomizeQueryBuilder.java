/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author aleyase2-admin
 */
public class CustomizeQueryBuilder {

    public static List<StringList> profIds = new ArrayList<StringList>();
    public static List<StringList> eduIds = new ArrayList<StringList>();

    static {
        profIds.add(new StringList("m.066dv", "m.02p7w", "Prophet", "47483"));
        profIds.add(new StringList("m.066dv", "m.04s9n", "Prophet", "49396"));
        profIds.add(new StringList("m.066dv", "m.02p7w", "Prophet", "49721"));
        profIds.add(new StringList("m.066dv", "m.0crqz1", "Prophet", "50032"));
        profIds.add(new StringList("m.01c979", "m.019gmw", "Designer", "44883"));
        profIds.add(new StringList("m.02h6ncg", "m.03rc98", "Pirate", "50309"));
        profIds.add(new StringList("m.01tkqy", "m.0dynm", "Stunt Performer", "48511"));
        profIds.add(new StringList("m.01tkqy", "m.0hk8j", "Stunt Performer", "48558"));
        profIds.add(new StringList("m.0cbd2", "m.09q8n", "Writer", "48500"));
        profIds.add(new StringList("m.0hpcnh9", "m.05ddf", "Violinist", "45329"));
        profIds.add(new StringList("m.02hv44_", "m.03pm9", "Playwright", "43667"));
        profIds.add(new StringList("m.02h664x", "m.054c1", "Basketball player", "45967"));
        profIds.add(new StringList("m.02h664x", "m.02c5ls", "Basketball player", "48587"));
        profIds.add(new StringList("m.05vd69b", "m.01lx0r", "Trade unionist", "46045"));
        profIds.add(new StringList("m.018gz8", "m.01dgrr", "Comedian", "43709"));
        profIds.add(new StringList("m.02hqr80", "m.04wqr", "Nude Glamour Model", "49527"));
        profIds.add(new StringList("m.080ntlp", "m.01tjvg", "Diplomat", "46267"));
        profIds.add(new StringList("m.04gc2", "m.0153dp", "Lawyer", "49930"));
        profIds.add(new StringList("m.02kdt7j", "m.04xm_", "Sociologist", "45042"));
        profIds.add(new StringList("m.09l65", "m.01mgtn4", "Singer", "44601"));
        profIds.add(new StringList("m.09l65", "m.0152cw", "Singer", "47654"));
        profIds.add(new StringList("m.09l65", "m.012z8_", "Singer", "48129"));
        profIds.add(new StringList("m.09l65", "m.01nhkxp", "Singer", "49904"));
        profIds.add(new StringList("m.02h664g", "m.01n9hm", "Baseball player", "43304"));
        profIds.add(new StringList("m.02h664g", "m.037zj0", "Baseball player", "47326"));
        profIds.add(new StringList("m.02nslmx", "m.02vrqc", "Ice hockey player", "47893"));
        profIds.add(new StringList("m.05cyczs", "m.0bq1z", "Crown Princess", "43862"));
        profIds.add(new StringList("m.0hpcdn2", "m.01mxp8n", "Rapper", "45505"));
        profIds.add(new StringList("m.0hpcdn2", "m.01wcs15", "Rapper", "46333"));
        profIds.add(new StringList("m.02xhr2s", "m.0ph2w", "Talk show host", "47429"));
        profIds.add(new StringList("m.02xhr2s", "m.0ph2w", "Talk show host", "49471"));
        profIds.add(new StringList("m.01d_h8", "m.0g2lq", "Film Producer", "48492"));
        profIds.add(new StringList("m.059dw0p", "m.01msq1", "Opera composer", "43415"));
        profIds.add(new StringList("m.059dw0p", "m.01msq1", "Opera composer", "49469"));
        profIds.add(new StringList("m.02h665k", "m.019_p3", "American football player", "44444"));
        profIds.add(new StringList("m.02h665k", "m.02l6j1", "American football player", "47805"));
        profIds.add(new StringList("m.05z96", "m.0c1jh", "Poet", "47457"));
        profIds.add(new StringList("m.03sbb", "m.06h8s", "Inventor", "48686"));
        profIds.add(new StringList("m.04s2z", "m.07dl6", "Mathematician", "48505"));
        profIds.add(new StringList("m.0d8qb", "m.0bn010", "Journalist", "48726"));

        eduIds.add(new StringList("m.02vk5st", "m.0m56y", "Hong Kong College of Medicine for Chinese", "44390"));
        eduIds.add(new StringList("m.01ptt7", "m.01z6ls", "University of Kentucky", "44488"));
        eduIds.add(new StringList("m.0gpg1h", "m.01jz6d", "St Vincent_St Mary High School", "45150"));
        eduIds.add(new StringList("m.0clvpwh", "m.0bhsgq", "The Boston Conservatory", "45603"));
        eduIds.add(new StringList("m.01pl14", "m.0143k9", "Louisiana State University", "45652"));
        eduIds.add(new StringList("m.093t6q", "m.01kmd4", "Lower Merion High School", "45860"));
        eduIds.add(new StringList("m.01wdl3", "m.01mdh0", "University of Alabama", "47321"));
        eduIds.add(new StringList("m.0263cn7", "m.03n69x", "North Fort Myers High School", "48965"));
        eduIds.add(new StringList("m.09f2j", "m.01y6p4", "University of California Los Angeles", "49882"));
        eduIds.add(new StringList("m.028bbnc", "m.0sx5w", "South Side High School", "49978"));


    }

    public static void main(String[] args) {
        //genrerateScripts(profIds,"prof", "profession");
        genrerateScripts(eduIds, "edu", "education");

    }

    public static void genrerateScripts(List<StringList> ids, String abbreviation, String fullName) {
        String script = "";
        Set<String> doneSet = new HashSet<String>();
        for (StringList idList : ids) {
            if (doneSet.contains(idList.getFirst())) {
                continue;
            }
            String currentProf = idList.getFirst();
            List<String> currentPeople = new ArrayList<String>();
            String peopleStr = "";
            String queryStr = "";
            for (StringList currentIdList : ids) {
                if (currentIdList.getFirst().equals(currentProf)) {
                    currentPeople.add(currentIdList.getSecond());
                    peopleStr += "'" + currentIdList.getSecond() + "'";
                    queryStr += currentIdList.getFourth();
                    peopleStr += ",";
                    queryStr += ",";
                }
            }
            peopleStr = peopleStr.substring(0, peopleStr.length() - 1);
            queryStr = queryStr.substring(0, queryStr.length() - 1);
            doneSet.add(idList.getFirst());
            //System.out.println("public static final String VI_PERSON_" + abbreviation.toUpperCase() + "_" + toUpperJoinCase(idList.getThird()) + " = \"vi_person_" + abbreviation + "_" + toLowerJoinCase(idList.getThird()) + "\";");
            // System.out.println("buildTables(Constants.VI_PERSON_"+abbreviation.toUpperCase()+"_" + toUpperJoinCase(idList.getThird()) + ", true);");
            //     System.out.println("Collection<Query> " + toLowerJoinCase(idList.getThird()) + "_queries = Query.dropStatement(Query.findById(Arrays.asList(" + queryStr + ")), \"" + fullName + "\");");
//            System.out.println("BaseIndex " + toLowerJoinCase(idList.getThird()) + "_index = new BaseIndex(Constants.VI_PERSON_" + abbreviation.toUpperCase() + "_" + toUpperJoinCase(idList.getThird()) + ");");
//            System.out.println(toLowerJoinCase(idList.getThird()) + "_index.setAcceptList(Arrays.asList(\"person_name\", \"person_description\"));");
//            System.out.println("");
          //    System.out.println("execBatchQueries(" + toLowerJoinCase(idList.getThird()) + "_queries, " + toLowerJoinCase(idList.getThird()) + "_index, AppConfig.RESULT_DIR + resultFile);");

//            System.out.println("create view `vi_person_"+abbreviation+"_" + toLowerJoinCase(idList.getThird()) + "` as\n"
//                    + "select p.* from tbl_person p, tbl_person_"+fullName+"_distinct ppd, tbl_"+fullName+" "+abbreviation+"\n"
//                    + "where \n"
//                    + "ppd.person=p.id and ppd."+fullName+"="+abbreviation+".id and "+abbreviation+".fbid='" + idList.getFirst() + "'\n"
//                    + "union\n"
//                    + "select * from tbl_person where fbid in (" + peopleStr + ");\n\n");


        }
    }

    public static String toLowerJoinCase(String str) {
        return str.toLowerCase().replace(' ', '_');
    }

    public static String toUpperJoinCase(String str) {
        return str.toUpperCase().replace(' ', '_');
    }
}

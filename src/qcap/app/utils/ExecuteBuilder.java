/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.utils;

import qcap.app.AppConfig;
import qcap.app.Constants;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import qcap.app.query.Query;

/**
 *
 * @author aleyase2
 */
public class ExecuteBuilder {

    public static void main(String[] args) {
        genExecFileByFile(Constants.STYPE_PERSON, "continent-test", "person-400-1.txt");
    }

    public static void genExecFileByDB(String semanticType, String runFileName) {
        int count = Query.getQueriesCount(semanticType);
        genExecFile(semanticType, runFileName, count);
    }

    public static void genExecFileByFile(String semanticType, String runFileName, String queryFile) {
        int count = Query.loadQueryFromFile(AppConfig.QUERY_DIR + queryFile).size();
        genExecFile(semanticType, runFileName, count);
    }

    public static void genExecFile(String semanticType, String runFileName, int count) {
        try {
            String filePath = AppConfig.EXEC_DIR + semanticType + "-" + runFileName + ".sh";
            String logDir = AppConfig.EXEC_DIR + semanticType + "-" + runFileName;
            Runtime.getRuntime().exec("mkdir " + logDir);

            CSVWriter writer = new CSVWriter(filePath);
            int limit = AppConfig.LIMIT_QUERY;
            int max = count;
            for (int offset = 0; (offset - limit) < max; offset += limit) {
                String cmd = "java -Xms30000m  -Xmx60000m -jar \"" + AppConfig.JAR_FILE_LOCATION + "\" " + offset
                        + " >> \"" + logDir + "/output-" + offset + "-" + limit + ".txt\" 2>&1";
                System.out.println(cmd);
                writer.append(cmd);
            }
            writer.close();
            Runtime.getRuntime().exec("chmod 777 " + filePath);
            System.out.println("Create .sh File: " + filePath);
            System.out.println("Create DIR: " + logDir);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(ExecuteBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

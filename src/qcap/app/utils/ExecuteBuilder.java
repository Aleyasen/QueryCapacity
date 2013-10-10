/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app.utils;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import qcap.app.AppConfig;
import qcap.app.Constants;
import qcap.app.IndexTester;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aleyase2
 */
public class ExecuteBuilder {

    public static void main(String[] args) {
        genExecFile(Constants.STYPE_TV, "tv-genre2-lm.sh", "lm-genre2-10-3-2013");
    }

    public static void genExecFile(String semanticType, String runFileName, String logPostfix) {
        try {
            String filePath = AppConfig.EXEC_DIR + runFileName;
            String logDir = "/home/aleyase2/galago-project/execute/" + semanticType + "-" + logPostfix;
            Runtime.getRuntime().exec("mkdir " + logDir);
            CSVWriter writer = new CSVWriter(filePath);
            int limit = AppConfig.LIMIT_QUERY;
            int max = IndexTester.getQueriesCount(semanticType);
            for (int offset = 0; (offset - limit) < max; offset += limit) {
                String cmd = "java -Xms30000m  -Xmx60000m -jar \"/home/aleyase2/galago-project/galago-app/dist/galago-app.jar\" " + offset
                        + " >> \"" + logDir + "/output-" + offset + "-" + limit + ".txt\" 2>&1";
                System.out.println(cmd);
                writer.append(cmd);
            }
            writer.close();
            Runtime.getRuntime().exec("chmod 777 " + filePath);
        } catch (IOException ex) {
            Logger.getLogger(ExecuteBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

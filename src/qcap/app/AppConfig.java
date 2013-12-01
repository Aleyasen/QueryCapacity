/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app;

/**
 *
 * @author aleyase2-admin
 */
public class AppConfig {

    public static final Integer QUERY_MAX_RESULTS = 50000;
    public static final Integer FETCH_SIZE = 10000;
    public static final Double SMOOTHING_FACTOR = 0.5;
    public static final String DB_CONNECTION_STR = "jdbc:mysql://localhost/freebase3";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "";
    public static final int LIMIT_QUERY = 5;
    public static final String BASE_DIR = "/srv/data/projects/dataset/Freebase/experiment/";
    public static final String QUERY_DIR = BASE_DIR + "query/";
    public static final String TEMP_DIR = BASE_DIR + "temp/";
    public static final String RESULT_DIR = BASE_DIR + "result/";
    public static final String EXEC_DIR = BASE_DIR + "execute/";
    public static final int QUERY_SAMPLE_SIZE = 400;
    public static final String JAR_FILE_LOCATION = "/home/aleyase2/galago-project/QueryCapacity/dist/QueryCapacity.jar";
}

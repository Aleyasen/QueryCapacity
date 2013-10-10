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

    public static final Integer QUERY_MAX_RESULTS = 10000;
    public static final Integer FETCH_SIZE = 5000;
    public static final Double SMOOTHING_FACTOR = 0.5;
    public static final String DB_CONNECTION_STR = "jdbc:mysql://localhost/freebase";
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "";
    public static final int LIMIT_QUERY = 10;
    public static final String BASE_DIR = "/home/aleyase2/galago-project/datasets";
    public static final String TEMP_DIR = "/home/aleyase2/galago-project/datasets/temp";
    public static final String RESULT_DIR = "/home/aleyase2/galago-project/datasets/result/files/";
    public static final String EXEC_DIR = "/home/aleyase2/galago-project/execute/";
  
    }

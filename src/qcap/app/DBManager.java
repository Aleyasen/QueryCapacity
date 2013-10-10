/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qcap.app;

import qcap.app.retrieval.BaseIndex;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aale
 */
public class DBManager {

    public static final DBManager instance = new DBManager();
    public static Connection staticConnection = instance.getConnection();

    public Connection getConnection() {
        //return null;
        //TODO: uncomment
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection newConn = DriverManager.getConnection(AppConfig.DB_CONNECTION_STR, AppConfig.DB_USERNAME, AppConfig.DB_PASSWORD);
            return newConn;
        } catch (InstantiationException | IllegalAccessException | SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ResultSet execQuery(String sql) {
        try {
            Statement stmt = staticConnection.createStatement();
            staticConnection.setAutoCommit(false);
            stmt.setFetchSize(AppConfig.FETCH_SIZE);
            //stmt.setMaxRows(2000);
            ResultSet rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

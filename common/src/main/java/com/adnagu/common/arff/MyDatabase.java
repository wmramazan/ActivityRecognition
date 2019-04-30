package com.adnagu.common.arff;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * MyDatabase
 *
 * @author ramazan.vapurcu
 * Created on 4/30/2019
 */
public class MyDatabase {

    public static final String URL = "jdbc:sqlite:C:/Databases/upload/ActivityRecognition.db";

    public Connection conn;

    public final MyActivityRecordDao activityRecordDao;
    public final MySensorRecordDao sensorRecordDao;

    public MyDatabase() {
        connect();

        activityRecordDao = new MyActivityRecordDao(conn);
        sensorRecordDao = new MySensorRecordDao(conn);
    }

    public void connect() {
        try {
            conn = DriverManager.getConnection(URL);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

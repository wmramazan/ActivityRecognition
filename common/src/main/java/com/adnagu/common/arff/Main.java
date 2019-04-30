package com.adnagu.common.arff;

/**
 * Main
 * The class has been created in order to generate ARFF files without the Android platform.
 *
 * @author ramazan.vapurcu
 * Created on 4/29/2019
 */
public class Main {

    public static void main(String[] args) {
        MyDatabase database = new MyDatabase();

        MyArffFile arffFile = new MyArffFile(
                database.activityRecordDao,
                database.sensorRecordDao
        );
        arffFile.save(4, 50);

        database.disconnect();

        /*Connection conn = null;

        try {
            conn = DriverManager.getConnection(MyDatabase.URL);
            //conn.setAutoCommit(false);

            System.out.println("Connection to SQLite has been established.");

            String query = "SELECT id FROM sensor_record LIMIT 1";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                System.out.println("Result: " + rs.getInt("id"));
            }

            Statement stmt1 = conn.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT id FROM activity ORDER BY id DESC LIMIT 1");

            while(rs1.next()) {
                System.out.println("Result1: " + rs.getInt("id"));
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }*/
    }
}

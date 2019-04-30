package com.adnagu.common.arff;

import com.adnagu.common.database.converter.DateConverter;
import com.adnagu.common.database.dao.ActivityRecordDao;
import com.adnagu.common.database.entity.ActivityRecordEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * MyActivityRecordDao
 *
 * @author ramazan.vapurcu
 * Created on 4/29/2019
 */
public class MyActivityRecordDao implements ActivityRecordDao {

    Connection conn;

    public MyActivityRecordDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public ActivityRecordEntity get(int id) {
        return null;
    }

    @Override
    public List<ActivityRecordEntity> getAll() {
        return null;
    }

    @Override
    public List<ActivityRecordEntity> getRecords(int activityId) {
        List<ActivityRecordEntity> activityRecords = new ArrayList<>();
        String query = "SELECT * FROM activity_record WHERE activity_id = " + activityId;

        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(query)) {

            while (rs.next()) {
                activityRecords.add(
                        new ActivityRecordEntity(
                                rs.getInt("id"),
                                rs.getInt("activity_id"),
                                DateConverter.toDate(rs.getLong("date")),
                                rs.getBoolean("test")
                        )
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return activityRecords;
    }

    @Override
    public ActivityRecordEntity getLastRecordForTest() {
        return null;
    }

    @Override
    public long[] insert(ActivityRecordEntity... activityRecords) {
        return new long[0];
    }

    @Override
    public int deleteLastRecord() {
        return 0;
    }

    @Override
    public void deleteTestRecords() {

    }
}

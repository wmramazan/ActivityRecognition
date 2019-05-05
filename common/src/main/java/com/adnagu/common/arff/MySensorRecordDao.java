package com.adnagu.common.arff;

import com.adnagu.common.database.converter.DateConverter;
import com.adnagu.common.database.converter.JSONConverter;
import com.adnagu.common.database.dao.SensorRecordDao;
import com.adnagu.common.database.entity.SensorRecordEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MySensorRecordDao
 *
 * @author ramazan.vapurcu
 * Created on 4/29/2019
 */
public class MySensorRecordDao implements SensorRecordDao {

    Connection conn;

    public MySensorRecordDao(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean hasAny() {
        return false;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public SensorRecordEntity get(int id) {
        return null;
    }

    @Override
    public List<SensorRecordEntity> getAll() {
        return null;
    }

    @Override
    public List<SensorRecordEntity> getAll(int activityRecordId) {
        return null;
    }

    @Override
    public List<SensorRecordEntity> getAll(int activityRecordId, int sensorId) {
        List<SensorRecordEntity> sensorRecords = new ArrayList<>();
        String query = "SELECT * FROM sensor_record WHERE activity_record_id = " + activityRecordId + " AND sensor_id = " + sensorId;

        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(query)) {
            while (rs.next()) {
                sensorRecords.add(
                        new SensorRecordEntity(
                                JSONConverter.toList(rs.getString("values")),
                                DateConverter.toDate(rs.getLong("date")),
                                rs.getLong("timestamp"),
                                rs.getInt("sensor_id"),
                                rs.getInt("activity_record_id")
                        )
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return sensorRecords;
    }

    @Override
    public SensorRecordEntity getFirst(int activityRecordId) {
        SensorRecordEntity sensorRecord = null;
        String query = "SELECT * FROM sensor_record WHERE activity_record_id = " + activityRecordId + " LIMIT 1";

        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(query)){

            while (rs.next()) {
                sensorRecord = new SensorRecordEntity(
                        rs.getInt("id"),
                        JSONConverter.toList(rs.getString("values")),
                        DateConverter.toDate(rs.getLong("date")),
                        rs.getLong("timestamp"),
                        rs.getInt("sensor_id"),
                        rs.getInt("activity_record_id")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return sensorRecord;
    }

    @Override
    public SensorRecordEntity getLast(int activityRecordId) {
        SensorRecordEntity sensorRecord = null;
        String query = "SELECT * FROM sensor_record WHERE activity_record_id = " + activityRecordId + " ORDER BY id DESC LIMIT 1";

        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(query)) {
            while (rs.next()) {
                sensorRecord = new SensorRecordEntity(
                        JSONConverter.toList(rs.getString("values")),
                        DateConverter.toDate(rs.getLong("date")),
                        rs.getLong("timestamp"),
                        rs.getInt("sensor_id"),
                        rs.getInt("activity_record_id")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return sensorRecord;
    }

    @Override
    public List<SensorRecordEntity> getBetween(int activityRecordId, int sensorId, Date firstDate, Date lastDate) {
        List<SensorRecordEntity> sensorRecords = new ArrayList<>();
        String query = "SELECT * FROM sensor_record WHERE activity_record_id = " + activityRecordId + " AND sensor_id = " + sensorId + " AND date BETWEEN " + firstDate.getTime() + " AND " + lastDate.getTime();

        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(query)) {
            while (rs.next()) {
                sensorRecords.add(
                        new SensorRecordEntity(
                            JSONConverter.toList(rs.getString("values")),
                            DateConverter.toDate(rs.getLong("date")),
                            rs.getLong("timestamp"),
                            rs.getInt("sensor_id"),
                            rs.getInt("activity_record_id")
                        )
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return sensorRecords;
}

    @Override
    public long[] insert(SensorRecordEntity... sensorRecords) {
        return new long[0];
    }

    @Override
    public void insert(List<SensorRecordEntity> sensorRecords) {

    }

    @Override
    public void delete(SensorRecordEntity sensorRecord) {

    }

    @Override
    public int deleteAll() {
        return 0;
    }
}

package com.adnagu.common.database.dao;

import com.adnagu.common.database.entity.SensorRecordEntity;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * SensorRecordDao
 *
 * @author ramazan.vapurcu
 * Created on 10/2/2018
 */
@Dao
public interface SensorRecordDao {

    @Query("SELECT id FROM sensor_record")
    boolean hasAny();

    @Query("SELECT COUNT(*) FROM sensor_record")
    int getCount();

    @Query("SELECT * FROM sensor_record WHERE id == :id")
    SensorRecordEntity get(int id);

    @Query("SELECT * FROM sensor_record")
    List<SensorRecordEntity> getAll();

    @Query("SELECT * FROM sensor_record WHERE activity_record_id = :activityRecordId")
    List<SensorRecordEntity> getAll(int activityRecordId);

    @Query("SELECT * FROM sensor_record WHERE activity_record_id = :activityRecordId AND sensor_id = :sensorId")
    List<SensorRecordEntity> getAll(int activityRecordId, int sensorId);

    @Query("SELECT * FROM sensor_record WHERE activity_record_id = :activityRecordId LIMIT 1")
    SensorRecordEntity getFirst(int activityRecordId);

    @Query("SELECT * FROM sensor_record WHERE activity_record_id = :activityRecordId ORDER BY id DESC LIMIT 1")
    SensorRecordEntity getLast(int activityRecordId);

    @Query("SELECT * FROM sensor_record WHERE activity_record_id = :activityRecordId AND sensor_id = :sensorId AND date BETWEEN :firstDate AND :lastDate")
    List<SensorRecordEntity> getBetween(int activityRecordId, int sensorId, Date firstDate, Date lastDate);

    /*@Query("SELECT sensor_record.* FROM sensor_record, activity_record WHERE sensor_record.sensor_id = activity_record.id = sensor_record.activity_record_id AND sensor_record.sensor_id IN (1,2,4,9,10) AND activity_record.activity_id = :activityId ORDER BY sensor_record.sensor_id, sensor_record.timestamp")
    List<SensorRecordEntity> getAllInOrder(int activityId);

    @Query("SELECT sensor_record.* FROM sensor_record, activity_record WHERE sensor_record.sensor_id = activity_record.id = sensor_record.activity_record_id AND sensor_record.sensor_id = :sensorId AND activity_record.activity_id = :activityId ORDER BY sensor_record.sensor_id, sensor_record.timestamp")
    List<SensorRecordEntity> getAllInOrder(int sensorId, int activityId);*/

    @Insert
    long[] insert(SensorRecordEntity... sensorRecords);

    @Insert
    void insert(List<SensorRecordEntity> sensorRecords);

    @Delete
    void delete(SensorRecordEntity sensorRecord);

    @Query("DELETE FROM sensor_record")
    int deleteAll();

}

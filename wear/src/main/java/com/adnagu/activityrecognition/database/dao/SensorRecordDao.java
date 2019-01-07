package com.adnagu.activityrecognition.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.adnagu.activityrecognition.database.entity.SensorRecordEntity;

import java.util.List;

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

    @Query("SELECT * FROM sensor_record ORDER BY sensor_record.sensor_id, sensor_record.timestamp")
    List<SensorRecordEntity> getAllInOrder();

    @Query("SELECT * FROM sensor_record WHERE sensor_record.sensor_id = :sensorId AND sensor_record.activity_id = :activityId ORDER BY sensor_record.sensor_id, sensor_record.timestamp")
    List<SensorRecordEntity> getAllInOrder(int sensorId, int activityId);

    @Insert
    long[] insert(SensorRecordEntity... sensorRecords);

    @Delete
    void delete(SensorRecordEntity sensorRecord);

    @Query("DELETE FROM sensor_record")
    int deleteAll();

}

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

    @Query("SELECT * FROM sensor_record WHERE id == :id")
    SensorRecordEntity get(int id);

    @Query("SELECT * FROM sensor_record")
    List<SensorRecordEntity> getAll();

    @Insert
    void insert(SensorRecordEntity... sensorRecords);

    @Delete
    void delete(SensorRecordEntity sensorRecord);

}

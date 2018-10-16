package com.adnagu.activityrecognition.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.adnagu.activityrecognition.database.entity.SensorValueEntity;

import java.util.List;

/**
 * SensorValueDao
 *
 * @author ramazan.vapurcu
 * Created on 10/16/2018
 */
@Dao
public interface SensorValueDao {

    @Query("SELECT id FROM sensor_value")
    boolean hasAny();

    @Query("SELECT COUNT(*) FROM sensor_value")
    int getCount();

    @Query("SELECT * FROM sensor_value WHERE id == :id")
    SensorValueEntity get(int id);

    @Query("SELECT * FROM sensor_value")
    List<SensorValueEntity> getAll();

    @Insert
    void insert(SensorValueEntity... sensorValues);

    @Delete
    void delete(SensorValueEntity sensorValue);

}

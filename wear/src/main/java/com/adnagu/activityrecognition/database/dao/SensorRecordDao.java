package com.adnagu.activityrecognition.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.adnagu.activityrecognition.database.entity.SensorRecord;

import java.util.List;

/**
 * SensorRecordDao
 *
 * @author ramazan.vapurcu
 * Created on 10/2/2018
 */
@Dao
public interface SensorRecordDao {

    @Query("SELECT * FROM sensor_record")
    List<SensorRecord> getAll();

    @Query("SELECT * FROM sensor_record WHERE id == :id")
    SensorRecord findById(int id);

    @Insert
    void insertAll(SensorRecord... sensorRecords);

    @Delete
    void delete(SensorRecord sensorRecord);

}

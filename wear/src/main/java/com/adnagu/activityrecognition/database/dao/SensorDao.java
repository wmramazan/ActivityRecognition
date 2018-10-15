package com.adnagu.activityrecognition.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.adnagu.activityrecognition.database.entity.SensorEntity;

import java.util.List;

/**
 * SensorDao
 *
 * @author ramazan.vapurcu
 * Created on 10/15/2018
 */
@Dao
public interface SensorDao {

    @Query("SELECT id FROM sensor")
    boolean hasAnyRecords();

    @Query("SELECT COUNT(*) FROM sensor")
    int getCount();

    @Query("SELECT * FROM sensor WHERE id == :id")
    SensorEntity get(int id);

    @Query("SELECT * FROM sensor")
    List<SensorEntity> getAll();

    @Insert
    void insert(SensorEntity... sensors);

    @Delete
    void delete(SensorEntity sensor);

    @Query("DELETE FROM sensor")
    void deleteAll();

}

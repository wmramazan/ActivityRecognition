package com.adnagu.common.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.adnagu.common.database.entity.SensorEntity;

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
    boolean hasAny();

    @Query("SELECT COUNT(*) FROM sensor")
    int getCount();

    @Query("SELECT * FROM sensor WHERE id = :id")
    SensorEntity get(int id);

    @Query("SELECT * FROM sensor")
    List<SensorEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SensorEntity... sensors);

    @Delete
    void delete(SensorEntity sensor);

    @Query("DELETE FROM sensor")
    void deleteAll();

}

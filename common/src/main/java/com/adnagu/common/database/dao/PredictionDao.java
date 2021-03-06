package com.adnagu.common.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.adnagu.common.database.entity.PredictionEntity;

import java.util.Date;
import java.util.List;

/**
 * PredictionDao
 *
 * @author ramazan.vapurcu
 * Created on 4/24/2019
 */
@Dao
public interface PredictionDao {

    @Query("SELECT COUNT(*) FROM prediction")
    int getCount();

    @Query("SELECT id FROM prediction")
    boolean hasAny();

    @Query("SELECT * FROM prediction WHERE id == :id")
    PredictionEntity get(int id);

    @Query("SELECT * FROM prediction")
    List<PredictionEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insert(PredictionEntity... prediction);

    @Query("UPDATE prediction SET end_date = :date WHERE id = :predictionId")
    void setEndDate(int predictionId, Date date);

    @Query("DELETE FROM prediction")
    int deleteAll();

}

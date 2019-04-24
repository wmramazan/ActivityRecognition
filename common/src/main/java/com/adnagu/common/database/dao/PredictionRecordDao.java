package com.adnagu.common.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.adnagu.common.database.entity.PredictionRecordEntity;

import java.util.List;

/**
 * PredictionRecordDao
 *
 * @author ramazan.vapurcu
 * Created on 4/24/2019
 */
@Dao
public interface PredictionRecordDao {

    @Query("SELECT id FROM prediction_record")
    boolean hasAny();

    @Query("SELECT * FROM prediction_record WHERE id == :id")
    PredictionRecordEntity get(int id);

    @Query("SELECT * FROM prediction_record")
    List<PredictionRecordEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(PredictionRecordEntity... predictionRecords);

}

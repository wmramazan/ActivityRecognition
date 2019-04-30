package com.adnagu.common.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.adnagu.common.database.entity.ActivityRecordEntity;

import java.util.List;

/**
 * ActivityRecordDao
 *
 * @author ramazan.vapurcu
 * Created on 03/04/19
 */
@Dao
public interface ActivityRecordDao {

    @Query("SELECT COUNT(*) FROM activity_record")
    int getCount();

    @Query("SELECT * FROM activity_record WHERE id = :id")
    ActivityRecordEntity get(int id);

    @Query("SELECT * FROM activity_record")
    List<ActivityRecordEntity> getAll();

    @Query("SELECT * FROM activity_record WHERE activity_id = :activityId")
    List<ActivityRecordEntity> getRecords(int activityId);

    @Query("SELECT * FROM activity_record WHERE test = 1 ORDER BY id DESC LIMIT 1")
    ActivityRecordEntity getLastRecordForTest();

    @Insert
    long[] insert(ActivityRecordEntity... activityRecords);

    @Query("DELETE FROM activity_record WHERE id = (SELECT id FROM activity_record ORDER BY id DESC)")
    int deleteLastRecord();

    @Query("DELETE FROM activity_record WHERE test = 1")
    void deleteTestRecords();

}

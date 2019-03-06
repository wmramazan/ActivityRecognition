package com.adnagu.activityrecognition.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.adnagu.activityrecognition.database.entity.ActivityRecordEntity;

import java.util.List;

/**
 * ActivityRecordDao
 *
 * @author ramazan.vapurcu
 * Created on 03/04/19
 */
@Dao
public interface ActivityRecordDao {

    @Query("SELECT * FROM activity_record WHERE id == :id")
    ActivityRecordEntity get(int id);

    @Query("SELECT * FROM activity_record")
    List<ActivityRecordEntity> getAll();

    @Insert
    long[] insert(ActivityRecordEntity... activityRecords);

}

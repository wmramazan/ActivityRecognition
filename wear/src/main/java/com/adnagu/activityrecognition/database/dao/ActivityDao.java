package com.adnagu.activityrecognition.database.dao;

import com.adnagu.activityrecognition.database.entity.ActivityEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * ActivityDao
 *
 * @author ramazan.vapurcu
 * Created on 03/04/19
 */
@Dao
public interface ActivityDao {

    @Query("SELECT id FROM activity")
    boolean hasAny();

    @Query("SELECT * FROM activity WHERE id == :id")
    ActivityEntity get(int id);

    @Query("SELECT * FROM activity")
    List<ActivityEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ActivityEntity... activities);

}

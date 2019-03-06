package com.adnagu.activityrecognition.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.adnagu.activityrecognition.database.entity.ActivityEntity;

import java.util.List;

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

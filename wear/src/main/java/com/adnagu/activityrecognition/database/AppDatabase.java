package com.adnagu.activityrecognition.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.adnagu.activityrecognition.database.dao.SensorRecordDao;
import com.adnagu.activityrecognition.database.entity.SensorRecord;

/**
 * AppDatabase
 *
 * @author ramazan.vapurcu
 * Created on 10/2/2018
 */
@Database(entities = {SensorRecord.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SensorRecordDao sensorRecordDao();

}

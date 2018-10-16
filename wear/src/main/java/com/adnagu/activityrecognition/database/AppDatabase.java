package com.adnagu.activityrecognition.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.adnagu.activityrecognition.database.converter.DateConverter;
import com.adnagu.activityrecognition.database.dao.SensorDao;
import com.adnagu.activityrecognition.database.dao.SensorRecordDao;
import com.adnagu.activityrecognition.database.dao.SensorValueDao;
import com.adnagu.activityrecognition.database.entity.SensorEntity;
import com.adnagu.activityrecognition.database.entity.SensorRecordEntity;
import com.adnagu.activityrecognition.database.entity.SensorValueEntity;
import com.adnagu.activityrecognition.utils.Utils;

/**
 * AppDatabase
 *
 * @author ramazan.vapurcu
 * Created on 10/2/2018
 */
@Database(entities = {SensorEntity.class, SensorRecordEntity.class, SensorValueEntity.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    // TODO: Export Schema

    private static volatile AppDatabase INSTANCE;

    public abstract SensorDao sensorDao();
    public abstract SensorRecordDao sensorRecordDao();
    public abstract SensorValueDao sensorValueDao();

    public static AppDatabase getInstance(Context context) {
        if (null == INSTANCE)
            synchronized (AppDatabase.class) {
                if (null == INSTANCE)
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, Utils.DATABASE_NAME).allowMainThreadQueries().build();
            }

            // TODO: Use another threads instead of main thread.

        return INSTANCE;
    }

}

package com.adnagu.activityrecognition.database;

import android.content.Context;

import com.adnagu.activityrecognition.database.converter.DateConverter;
import com.adnagu.activityrecognition.database.converter.JSONConverter;
import com.adnagu.activityrecognition.database.dao.ActivityDao;
import com.adnagu.activityrecognition.database.dao.ActivityRecordDao;
import com.adnagu.activityrecognition.database.dao.SensorDao;
import com.adnagu.activityrecognition.database.dao.SensorRecordDao;
import com.adnagu.activityrecognition.database.entity.ActivityEntity;
import com.adnagu.activityrecognition.database.entity.ActivityRecordEntity;
import com.adnagu.activityrecognition.database.entity.SensorEntity;
import com.adnagu.activityrecognition.database.entity.SensorRecordEntity;
import com.adnagu.activityrecognition.database.view.Frequency;
import com.adnagu.activityrecognition.utils.Utils;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * AppDatabase
 *
 * @author ramazan.vapurcu
 * Created on 10/2/2018
 */
@Database(entities = {ActivityEntity.class, ActivityRecordEntity.class, SensorEntity.class, SensorRecordEntity.class}, views = {Frequency.class}, version = 2, exportSchema = false)
@TypeConverters({DateConverter.class, JSONConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    // TODO: Export Schema

    private static volatile AppDatabase INSTANCE;

    public abstract ActivityDao activityDao();
    public abstract ActivityRecordDao activityRecordDao();
    public abstract SensorDao sensorDao();
    public abstract SensorRecordDao sensorRecordDao();

    public static AppDatabase getInstance(Context context) {
        if (null == INSTANCE)
            synchronized (AppDatabase.class) {
                if (null == INSTANCE)
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, Utils.DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)
                            .allowMainThreadQueries()
                            .build();
            }

            // TODO: Use another threads instead of main thread.

        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE VIEW `Frequency` AS SELECT AVG(number_of_records) / number_of_sensors as frequency FROM (SELECT COUNT(id) AS number_of_records, COUNT(DISTINCT(sensor_id)) AS number_of_sensors, datetime(sensor_record.date / 1000, 'unixepoch') as date_string FROM sensor_record GROUP BY date_string)");
        }
    };

}

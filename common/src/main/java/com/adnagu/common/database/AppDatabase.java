package com.adnagu.common.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.adnagu.common.database.converter.DateConverter;
import com.adnagu.common.database.converter.JSONConverter;
import com.adnagu.common.database.dao.ActivityDao;
import com.adnagu.common.database.dao.ActivityRecordDao;
import com.adnagu.common.database.dao.PredictionDao;
import com.adnagu.common.database.dao.PredictionRecordDao;
import com.adnagu.common.database.dao.SensorDao;
import com.adnagu.common.database.dao.SensorRecordDao;
import com.adnagu.common.database.entity.ActivityEntity;
import com.adnagu.common.database.entity.ActivityRecordEntity;
import com.adnagu.common.database.entity.PredictionEntity;
import com.adnagu.common.database.entity.PredictionRecordEntity;
import com.adnagu.common.database.entity.SensorEntity;
import com.adnagu.common.database.entity.SensorRecordEntity;
import com.adnagu.common.database.view.Frequency;
import com.adnagu.common.utils.DatabaseUtils;

/**
 * AppDatabase
 *
 * @author ramazan.vapurcu
 * Created on 10/2/2018
 */
@Database(entities = {ActivityEntity.class, ActivityRecordEntity.class, SensorEntity.class, SensorRecordEntity.class, PredictionEntity.class, PredictionRecordEntity.class}, views = {Frequency.class}, version = 4, exportSchema = false)
@TypeConverters({DateConverter.class, JSONConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    // TODO: Export Schema

    private static volatile AppDatabase INSTANCE;

    public abstract ActivityDao activityDao();
    public abstract ActivityRecordDao activityRecordDao();
    public abstract SensorDao sensorDao();
    public abstract SensorRecordDao sensorRecordDao();
    public abstract PredictionDao predictionDao();
    public abstract PredictionRecordDao predictionRecordDao();

    public static AppDatabase getInstance(Context context) {
        if (null == INSTANCE)
            synchronized (AppDatabase.class) {
                if (null == INSTANCE)
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DatabaseUtils.DATABASE_NAME)
                            .addMigrations(
                                    MIGRATION_1_2,
                                    MIGRATION_2_3,
                                    MIGRATION_3_4
                            )
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

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE activity_record ADD COLUMN test INTEGER NOT NULL DEFAULT 0");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS prediction (id INTEGER NOT NULL, start_date INTEGER, end_date INTEGER, correctness INTEGER NOT NULL, PRIMARY KEY(id))");
            database.execSQL("CREATE TABLE IF NOT EXISTS prediction_record (id INTEGER NOT NULL, activity_id INTEGER NOT NULL, prediction_id INTEGER NOT NULL, date INTEGER, correct INTEGER NOT NULL, PRIMARY KEY(id), FOREIGN KEY('activity_id') REFERENCES activity(id) ON DELETE CASCADE, FOREIGN KEY('prediction_id') REFERENCES prediction(id) ON DELETE CASCADE)");
            database.execSQL("CREATE INDEX index_prediction_record_activity_id_prediction_id ON prediction_record('activity_id', 'prediction_id')");
        }
    };

}

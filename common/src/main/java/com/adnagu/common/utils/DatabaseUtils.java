package com.adnagu.common.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;

import com.adnagu.common.database.AppDatabase;
import com.adnagu.common.database.dao.ActivityDao;
import com.adnagu.common.database.dao.SensorDao;
import com.adnagu.common.database.entity.ActivityEntity;
import com.adnagu.common.database.entity.SensorEntity;
import com.adnagu.common.model.Activity;

import java.util.List;

/**
 * DatabaseUtils
 *
 * @author ramazan.vapurcu
 * Created on 03/06/19
 */
public class DatabaseUtils {

    public static final String DATABASE_NAME = "ActivityRecognition.db";

    public static void prepareDatabase(Context context) {

        AppDatabase appDatabase = AppDatabase.getInstance(context);

        // Save activities
        ActivityDao activityDao = appDatabase.activityDao();
        if (!activityDao.hasAny()) {
            Activity[] activities = Activity.values();
            ActivityEntity[] activityEntities = new ActivityEntity[activities.length];

            for (Activity activity : activities)
                activityEntities[activity.ordinal()] = new ActivityEntity(
                        activity.ordinal(),
                        context.getString(activity.title_res)
                );

            activityDao.insert(activityEntities);
        }

        // Save sensors
        SensorDao sensorDao = appDatabase.sensorDao();
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (null != sensorManager && !sensorDao.hasAny()) {
            List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
            SensorEntity[] sensorEntities = new SensorEntity[sensors.size()];

            for (int i = 0; i < sensors.size(); i++) {
                Sensor sensor = sensors.get(i);
                sensorEntities[i] = new SensorEntity(
                        sensor.getType(),
                        sensor.getName(),
                        sensor.getVendor(),
                        sensor.getMinDelay(),
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? sensor.getMaxDelay() : -1,
                        sensor.getMaximumRange(),
                        sensor.getResolution(),
                        sensor.getPower()
                );
            }

            sensorDao.insert(sensorEntities);
        }
    }

}

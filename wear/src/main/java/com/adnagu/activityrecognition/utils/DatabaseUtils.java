package com.adnagu.activityrecognition.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.adnagu.activityrecognition.database.AppDatabase;
import com.adnagu.activityrecognition.database.dao.ActivityDao;
import com.adnagu.activityrecognition.database.dao.SensorDao;
import com.adnagu.activityrecognition.database.entity.ActivityEntity;
import com.adnagu.activityrecognition.database.entity.SensorEntity;
import com.adnagu.activityrecognition.model.Activity;

import java.util.List;

/**
 * DatabaseUtils
 *
 * @author ramazan.vapurcu
 * Created on 03/06/19
 */
public class DatabaseUtils {

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
                        sensor.getMaxDelay(),
                        sensor.getMaximumRange(),
                        sensor.getResolution(),
                        sensor.getPower()
                );
            }

            sensorDao.insert(sensorEntities);
        }
    }

}

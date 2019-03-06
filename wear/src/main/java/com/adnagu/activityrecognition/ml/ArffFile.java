package com.adnagu.activityrecognition.ml;

import android.content.Context;
import android.hardware.Sensor;
import android.util.Log;

import com.adnagu.activityrecognition.database.dao.SensorRecordDao;
import com.adnagu.activityrecognition.database.entity.SensorRecordEntity;
import com.adnagu.activityrecognition.model.Activity;
import com.adnagu.activityrecognition.model.Feature;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * ArffFile
 *
 * @author ramazan.vapurcu
 * Created on 12/5/2018
 */
public class ArffFile {

    private static final String     DEBUG_TAG = "ArffFile";

    public static final String      FILE_NAME = "ActivityRecords.arff";
    public static final int         NANO_SECONDS = 1000000000;
    public static final int         TIME_OUT = 3;
    public static final int[]       SENSOR_TYPES = {
            Sensor.TYPE_ACCELEROMETER,
            Sensor.TYPE_MAGNETIC_FIELD,
            Sensor.TYPE_GYROSCOPE,
            Sensor.TYPE_GRAVITY,
            Sensor.TYPE_LINEAR_ACCELERATION,
            /*Sensor.TYPE_ROTATION_VECTOR,
            Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED,
            Sensor.TYPE_GAME_ROTATION_VECTOR,
            Sensor.TYPE_GYROSCOPE_UNCALIBRATED,
            Sensor.TYPE_SIGNIFICANT_MOTION,
            Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR*/
    };
    public static final String[]    SENSOR_TYPE_NAMES = {
            "acc",
            "mag",
            "gyro",
            "gra",
            "lacc"
    };
    public static final int[]       SENSOR_VALUE_TYPES = {
            5,
            4,
            4,
            3,
            3
    };
    public static final char[]      VALUE_TYPES = {
            'x',
            'y',
            'z',
            'a',
            'b'
    };

    public static void saveAsArff(Context context, SensorRecordDao sensorRecordDao, int windowLength, int overlapping) {
        try {
            // Prepare file
            OutputStreamWriter writer = new OutputStreamWriter(
                    context.openFileOutput(
                            FILE_NAME,
                            Context.MODE_PRIVATE
                    )
            );
            writer.write("@relation activity_recognition\n\n");

            // Write attributes
            for (int i = 0; i < SENSOR_TYPE_NAMES.length; i++)
                for (Feature feature : Feature.values())
                    for (int j = 0; j < SENSOR_VALUE_TYPES[i]; j++)
                        writer.write("@attribute " + SENSOR_TYPE_NAMES[i] + "_" + feature.name() + "_" + VALUE_TYPES[j] + " numeric\n");

            StringBuilder activities = new StringBuilder();
            for (Activity activity : Activity.values())
                activities.append("'").append(activity.name()).append("',");

            activities.deleteCharAt(activities.length() - 1);

            writer.write("@attribute 'Class' {" + activities.toString() +"}\n");

            // Write records
            writer.write("@data\n");

            for (Activity activity : Activity.values()) {
                Log.d(DEBUG_TAG, "Activity: " + activity.ordinal());

                List<List<SensorRecordEntity>> sensorRecords = new ArrayList<>();
                for (int SENSOR_TYPE : SENSOR_TYPES)
                    sensorRecords.add(
                            sensorRecordDao.getAllInOrder(SENSOR_TYPE, activity.ordinal())
                    );

                int minValue = sensorRecords.get(0).size();
                int i = 1;
                while (i < SENSOR_TYPES.length) {
                    if (sensorRecords.get(i).size() < minValue)
                        minValue = sensorRecords.get(i).size();
                    i++;
                }

                windowLength = minValue / 100;
                Log.d(DEBUG_TAG, "Window Length: " + windowLength);

                overlapping = windowLength / 2;

                i = 0;
                int limit = minValue - windowLength - overlapping + 1;
                while (i <= limit) {

                    // Window
                    //FeatureExtraction.extractAllFeatures();


                    i += overlapping;
                }

                Log.d(DEBUG_TAG, "Min. Value: " + minValue);

                /*long lastTimestamp;
                boolean loop;

                for (int i = 0; i < minValue; i++) {
                    lastTimestamp = 0;
                    loop = true;

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(activity.ordinal()).append(',');
                    stringBuilder.append('\'');

                    while (i < minValue && loop) {
                        SequentialArffRecord firstRecord = sensorValues.get(0).get(i);
                        Log.d(DEBUG_TAG, "Record Timestamp: " + firstRecord.getTimestamp());

                        if (lastTimestamp == 0)
                            lastTimestamp = firstRecord.getTimestamp();
                        else if (firstRecord.getTimestamp() - lastTimestamp > limit)
                            loop = false;

                        for (int j = 0; j < SENSOR_TYPES.length; j++)
                            stringBuilder.append(sensorValues.get(j).get(i).getValue()).append(',');

                        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                        stringBuilder.append("\\n");

                        i++;

                        Log.d(DEBUG_TAG, "Loop: " + loop);
                        Log.d(DEBUG_TAG, "Last Timestamp: " + lastTimestamp);
                    }

                    stringBuilder.append('\'');
                    writer.write(stringBuilder.toString());
                    writer.write("\n");
                }*/
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isValidSensorType(int id) {
        for (int SENSOR_TYPE : SENSOR_TYPES)
            if (id == SENSOR_TYPE)
                return true;
        return false;
    }

}

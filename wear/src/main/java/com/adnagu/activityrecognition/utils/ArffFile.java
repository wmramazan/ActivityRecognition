package com.adnagu.activityrecognition.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.util.Log;
import android.util.StringBuilderPrinter;

import com.adnagu.activityrecognition.database.converter.JSONConverter;
import com.adnagu.activityrecognition.database.dao.SensorRecordDao;
import com.adnagu.activityrecognition.database.entity.SensorRecordEntity;
import com.adnagu.activityrecognition.model.Activity;

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

    private static final String DEBUG_TAG = "ArffFile";

    public static final String  FILE_NAME = "ActivityRecords.arff";
    public static final int     NANO_SECONDS = 1000000000;
    public static final int     TIME_OUT = 3;
    public static final int[]   SENSOR_TYPES = {
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
    public static final char[]  VALUE_TYPES = {
            'x',
            'y',
            'z',
            'a',
            'b',
            'c',
            'd',
            'e'
    };

    public static void saveAsArff(Context context, SensorRecordDao sensorRecordDao, int windowLength) {
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
            StringBuilder activityIds = new StringBuilder();
            for (int i = 0; i < Activity.values().length; i++)
                activityIds.append(i).append(",");

            activityIds.deleteCharAt(activityIds.length() - 1);

            writer.write("@attribute activity {" + activityIds.toString() +"}\n");
            writer.write("@attribute sequence relational\n");

            for (int sensorType : SENSOR_TYPES)
                for (int i = 0; i < 3; i++)
                    writer.write("\t@attribute sensor" + sensorType + "_" + VALUE_TYPES[i] + " numeric\n");

            writer.write("@end sequence\n");

            // Write records
            writer.write("@data\n");

            long limit = windowLength * NANO_SECONDS;

            for (Activity activity : Activity.values()) {
                Log.d(DEBUG_TAG, "Activity: " + activity.ordinal());

                ArrayList<ArrayList<ArffRecord>> sensorValues = new ArrayList<>();
                for (int SENSOR_TYPE : SENSOR_TYPES) {
                    ArrayList<ArffRecord> values = new ArrayList<>();
                    Log.d(DEBUG_TAG, "Sensor Type: " + SENSOR_TYPE);
                    List<SensorRecordEntity> sensorRecords = sensorRecordDao.getAllInOrder(SENSOR_TYPE, activity.ordinal());
                    Log.d(DEBUG_TAG, "Record Size: " + sensorRecords.size());

                    for (SensorRecordEntity sensorRecord : sensorRecords) {
                        String value = sensorRecord.getValues();
                        values.add(
                                new ArffRecord(
                                        value.substring(1, value.length() - 1),
                                        sensorRecord.getTimestamp()
                                )
                        );
                    }

                    sensorValues.add(values);
                }

                int min_value = sensorValues.get(0).size();
                for (int i = 1; i < sensorValues.size(); i++)
                    if (min_value > sensorValues.get(i).size())
                        min_value = sensorValues.get(i).size();

                Log.d(DEBUG_TAG, "Min. Value: " + min_value);

                long lastTimestamp;
                boolean loop;

                for (int i = 0; i < min_value; i++) {
                    lastTimestamp = 0;
                    loop = true;

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(activity.ordinal()).append(',');
                    stringBuilder.append('\'');

                    while (i < min_value && loop) {
                        ArffRecord firstRecord = sensorValues.get(0).get(i);
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
                }
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

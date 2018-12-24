package com.adnagu.activityrecognition.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.util.Log;

import com.adnagu.activityrecognition.database.entity.SensorRecordEntity;
import com.adnagu.activityrecognition.model.Activity;

import java.io.IOException;
import java.io.OutputStreamWriter;
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

    public static void saveAsArff(Context context, List<SensorRecordEntity> sensorRecords, int windowLength) {
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

            Log.d(DEBUG_TAG, activityIds.toString());

            writer.write("@attribute activity {" + activityIds.toString() +"}\n");
            writer.write("@attribute sequence sensor_records");

            for (int sensorType : SENSOR_TYPES)
                for (int i = 0; i < 3; i++)
                    writer.write("\t@attribute sensor" + sensorType +"_" + VALUE_TYPES[i] + " numeric");

            writer.write("@end sequence");

            // Write records
            writer.write("@data\n");

            int index;
            long lastTimestamp;

            for (index = 0; index < sensorRecords.size(); index++) {
                SensorRecordEntity sensorRecord = sensorRecords.get(index);
                if (isValidSensorType(sensorRecord.getId())) {
                    lastTimestamp = sensorRecord.getTimestamp();
                }
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidSensorType(int id) {
        for (int SENSOR_TYPE : SENSOR_TYPES)
            if (id == SENSOR_TYPE)
                return true;
        return false;
    }

}

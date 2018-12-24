package com.adnagu.activityrecognition.utils;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.support.wearable.activity.ConfirmationActivity;

/**
 * Utils
 *
 * @author ramazan.vapurcu
 * Created on 10/14/2018
 */
public class Utils {

    public static final String DATABASE_NAME = "ActivityRecognition.db";
    public static final String DATABASE_HANDLER_THREAD = "database";

    public static final String ACTIVITY_INDEX = "index";

    public static final int[] SENSOR_TYPES = {
            Sensor.TYPE_ACCELEROMETER,
            Sensor.TYPE_MAGNETIC_FIELD,
            Sensor.TYPE_GYROSCOPE,
            Sensor.TYPE_GRAVITY,
            Sensor.TYPE_LINEAR_ACCELERATION,
            Sensor.TYPE_ROTATION_VECTOR,
            Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED,
            Sensor.TYPE_GAME_ROTATION_VECTOR,
            Sensor.TYPE_GYROSCOPE_UNCALIBRATED,
            Sensor.TYPE_SIGNIFICANT_MOTION,
            Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR
    };

    public class RequestCode {
        public static final int CHOOSE_ACTIVITY = 1;
    }

    public static void showMessage(int animationType, Context context, String message) {
        Intent intent = new Intent(context, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, animationType);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, message);
        context.startActivity(intent);
    }

}

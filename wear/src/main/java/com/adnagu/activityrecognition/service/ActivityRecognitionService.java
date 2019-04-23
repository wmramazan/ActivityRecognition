package com.adnagu.activityrecognition.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.adnagu.common.database.entity.SensorRecordEntity;
import com.adnagu.common.model.SensorType;

import java.util.ArrayList;

/**
 * ActivityRecognitionService
 *
 * @author ramazan.vapurcu
 * Created on 04/21/2019
 */
public class ActivityRecognitionService extends Service implements SensorEventListener {

    private final String DEBUG_TAG = getClass().getName();

    SensorManager sensorManager;
    ArrayList<SensorRecordEntity> sensorRecords;

    long timeMillis;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void init() {
        sensorRecords = new ArrayList<>();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (null != sensorManager)
            startRecognition();
    }

    protected void startRecognition() {
        for (SensorType sensorType : SensorType.values()) {
            Sensor sensor = sensorManager.getDefaultSensor(sensorType.id);
            if (null == sensor)
                Log.d(DEBUG_TAG, "Unsupported sensor: " + sensorType);
            else
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    protected void stopRecognition() {
        sensorManager.unregisterListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (timeMillis == 0) {
            timeMillis = System.currentTimeMillis();
        } else {
            // Check timeMillis for new instance
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

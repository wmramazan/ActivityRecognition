package com.adnagu.activityrecognition.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import com.adnagu.activityrecognition.database.AppDatabase;
import com.adnagu.activityrecognition.database.converter.JSONConverter;
import com.adnagu.activityrecognition.database.dao.ActivityRecordDao;
import com.adnagu.activityrecognition.database.dao.SensorRecordDao;
import com.adnagu.activityrecognition.database.entity.ActivityRecordEntity;
import com.adnagu.activityrecognition.database.entity.SensorRecordEntity;
import com.adnagu.activityrecognition.utils.Utils;

import java.util.ArrayList;
import java.util.Date;

public class SensorRecordService extends Service implements SensorEventListener {

    private final String DEBUG_TAG = getClass().getName();
    private final int    CACHE_SIZE = 200;

    AppDatabase appDatabase;
    ActivityRecordDao activityRecordDao;
    SensorRecordDao sensorRecordDao;

    SensorManager sensorManager;

    ArrayList<SensorRecordEntity> sensorRecords;

    int activityId;
    int activityRecordId;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(DEBUG_TAG, "onStartCommand");

        activityId = intent.getIntExtra(Utils.ACTIVITY_ID, 0);
        init();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(DEBUG_TAG, "onDestroy");

        stopRecording();
        super.onDestroy();
    }

    protected void init() {
        Log.d(DEBUG_TAG, "init");

        appDatabase = AppDatabase.getInstance(this);
        activityRecordDao = appDatabase.activityRecordDao();
        sensorRecordDao = appDatabase.sensorRecordDao();

        sensorRecords = new ArrayList<>();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (null != sensorManager)
            startRecording();
    }

    protected void startRecording() {
        for (int sensorType : Utils.SENSOR_TYPES) {
            Sensor sensor = sensorManager.getDefaultSensor(sensorType);
            if (null == sensor)
                Log.d(DEBUG_TAG, "Unsupported sensor: " + sensorType);
            else
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }

        long[] results = activityRecordDao.insert(
                new ActivityRecordEntity(
                        activityId,
                        new Date()
                )
        );
        activityRecordId = (int) results[0];
    }

    protected void stopRecording() {
        sensorManager.unregisterListener(this);

        saveRecords();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        /*
        if (sensorEvent.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
            return;
        */

        sensorRecords.add(new SensorRecordEntity(
                JSONConverter.fromArray(sensorEvent.values),
                new Date(),
                sensorEvent.timestamp,
                sensorEvent.sensor.getType(),
                activityRecordId
        ));

        if (sensorRecords.size() == CACHE_SIZE)
            saveRecords();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void saveRecords() {
        sensorRecordDao.insert(sensorRecords);
        sensorRecords.clear();
    }
}

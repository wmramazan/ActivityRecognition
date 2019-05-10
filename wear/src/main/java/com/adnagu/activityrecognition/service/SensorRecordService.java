package com.adnagu.activityrecognition.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import com.adnagu.activityrecognition.utils.Utils;
import com.adnagu.common.database.AppDatabase;
import com.adnagu.common.database.dao.ActivityRecordDao;
import com.adnagu.common.database.dao.SensorRecordDao;
import com.adnagu.common.database.entity.ActivityRecordEntity;
import com.adnagu.common.database.entity.SensorRecordEntity;
import com.adnagu.common.model.SensorType;

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

    boolean test;

    int activityId;
    int activityRecordId;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(DEBUG_TAG, "onStartCommand");

        activityId = intent.getIntExtra(Utils.ACTIVITY_ID, 0);
        test = intent.getBooleanExtra(Utils.TEST, false);
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
        for (SensorType sensorType : SensorType.values()) {
            Sensor sensor = sensorManager.getDefaultSensor(sensorType.type);
            if (null == sensor)
                Log.d(DEBUG_TAG, "Unsupported sensor: " + sensorType);
            else
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }

        if (test)
            activityRecordDao.deleteTestRecords();

        long[] results = activityRecordDao.insert(
                new ActivityRecordEntity(
                        activityId,
                        new Date(),
                        test
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
                Utils.toList(sensorEvent.values),
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

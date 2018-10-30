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
import com.adnagu.activityrecognition.database.dao.SensorRecordDao;
import com.adnagu.activityrecognition.database.entity.SensorRecordEntity;
import com.adnagu.activityrecognition.utils.Utils;

public class SensorRecordService extends Service implements SensorEventListener {

    private final String DEBUG_TAG = getClass().getName();
    private final int    SENSOR_RECORD_CACHE = 1000;

    AppDatabase appDatabase;
    SensorRecordDao sensorRecordDao;

    HandlerThread handlerThread;
    Handler handler;

    SensorManager sensorManager;

    SensorRecordEntity[] sensorRecordEntities;

    int activityTypeId;
    int sensorRecordIndex;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(DEBUG_TAG, "onStartCommand");

        activityTypeId = intent.getIntExtra(Utils.ACTIVITY_INDEX, 0);
        init();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(DEBUG_TAG, "onDestroy");
        Log.d(DEBUG_TAG, "sensorRecordIndex: " + sensorRecordIndex);

        stopRecording();
        handlerThread.quitSafely();
        super.onDestroy();
    }

    protected void init() {
        Log.d(DEBUG_TAG, "init");
        handlerThread = new HandlerThread(Utils.DATABASE_HANDLER_THREAD, Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());

        appDatabase = AppDatabase.getInstance(this);
        sensorRecordDao = appDatabase.sensorRecordDao();

        sensorRecordEntities = new SensorRecordEntity[SENSOR_RECORD_CACHE];

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
    }

    protected void stopRecording() {
        sensorManager.unregisterListener(this);

        if (sensorRecordIndex != 0) {
            SensorRecordEntity[] remainingRecords = new SensorRecordEntity[sensorRecordIndex];
            System.arraycopy(sensorRecordEntities, 0, remainingRecords, 0, sensorRecordIndex);

            handler.post(() -> sensorRecordDao.insert(remainingRecords));
            sensorRecordIndex = 0;
        }
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

        if (sensorRecordIndex >= SENSOR_RECORD_CACHE) {
            handler.post(() -> sensorRecordDao.insert(sensorRecordEntities));
            sensorRecordIndex = 0;
        }

        sensorRecordEntities[sensorRecordIndex++] = new SensorRecordEntity(
                sensorEvent.timestamp,
                sensorEvent.sensor.getType(),
                activityTypeId,
                JSONConverter.fromArray(sensorEvent.values)
        );
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

package com.adnagu.activityrecognition.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import com.adnagu.activityrecognition.database.AppDatabase;
import com.adnagu.activityrecognition.database.dao.SensorRecordDao;
import com.adnagu.activityrecognition.database.dao.SensorValueDao;
import com.adnagu.activityrecognition.database.entity.SensorRecordEntity;
import com.adnagu.activityrecognition.database.entity.SensorValueEntity;
import com.adnagu.activityrecognition.utils.Utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SensorRecordService extends Service implements SensorEventListener {

    private final String DEBUG_TAG = getClass().getName();
    private static final int KEEP_ALIVE = 10;

    ThreadPoolExecutor threadPoolExecutor;
    BlockingQueue<Runnable> workQueue;

    SensorManager sensorManager;
    Sensor sensor;

    AppDatabase appDatabase;
    SensorRecordDao sensorRecordDao;
    SensorValueDao sensorValueDao;

    Intent intent;

    int activityTypeId;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(DEBUG_TAG, "onStartCommand");

        activityTypeId = intent.getIntExtra(Utils.ACTIVITY_INDEX, 0);
        init();

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        workQueue = new LinkedBlockingQueue<>();

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        threadPoolExecutor = new ThreadPoolExecutor(
                availableProcessors,
                availableProcessors * 2,
                KEEP_ALIVE,
                TimeUnit.SECONDS,
                workQueue
        );
    }

    @Override
    public void onDestroy() {
        Log.d(DEBUG_TAG, "onDestroy");
        threadPoolExecutor.shutdown();
        sensorManager.unregisterListener(this);

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(DEBUG_TAG, "onSensorChanged");

        /*
        if (sensorEvent.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
            return;
        */

        threadPoolExecutor.execute(() -> {
            Log.d(DEBUG_TAG, "HANDLER");
            SensorRecordEntity sensorRecord = new SensorRecordEntity(
                    sensorEvent.timestamp,
                    sensorEvent.sensor.getType(),
                    activityTypeId
            );

            long recordId = sensorRecordDao.insert(sensorRecord)[0];

            for (float value : sensorEvent.values)
                sensorValueDao.insert(
                        new SensorValueEntity(
                                value,
                                (int) recordId
                        )
                );
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected void init() {
        Log.d(DEBUG_TAG, "init");

        intent = new Intent();

        appDatabase = AppDatabase.getInstance(this);
        sensorRecordDao = appDatabase.sensorRecordDao();
        sensorValueDao = appDatabase.sensorValueDao();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (null != sensorManager) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ALL);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }
}

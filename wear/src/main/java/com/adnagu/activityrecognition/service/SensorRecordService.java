package com.adnagu.activityrecognition.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.adnagu.activityrecognition.database.AppDatabase;
import com.adnagu.activityrecognition.database.dao.SensorRecordDao;
import com.adnagu.activityrecognition.database.dao.SensorValueDao;
import com.adnagu.activityrecognition.database.entity.SensorRecordEntity;
import com.adnagu.activityrecognition.database.entity.SensorValueEntity;
import com.adnagu.activityrecognition.utils.Utils;

public class SensorRecordService extends Service implements SensorEventListener {

    private final String DEBUG_TAG = getClass().getName();

    SensorManager sensorManager;
    Sensor sensor;

    int activityTypeId;

    public SensorRecordService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(DEBUG_TAG, "onStartCommand");

        activityTypeId = intent.getIntExtra(Utils.ACTIVITY_INDEX, 0);
        init();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(DEBUG_TAG, "onCreate");
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(DEBUG_TAG, "onDestroy");
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(DEBUG_TAG, "onSensorChanged");
        Log.d(DEBUG_TAG, "SensorEntity Accuracy: " + sensorEvent.accuracy);

        /*
        if (sensorEvent.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
            return;
        */

        new SensorRecordTask().execute(sensorEvent);

        Log.d(DEBUG_TAG, sensorEvent.sensor.getName() + ": " + sensorEvent.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected void init() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (null != sensorManager) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ALL);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    private class SensorRecordTask extends AsyncTask<SensorEvent, Void, Void> {

        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
        SensorRecordDao sensorRecordDao = appDatabase.sensorRecordDao();
        SensorValueDao sensorValueDao = appDatabase.sensorValueDao();

        @Override
        protected Void doInBackground(SensorEvent... sensorEvents) {
            SensorEvent sensorEvent = sensorEvents[0];
            SensorRecordEntity sensorRecord = new SensorRecordEntity(
                    sensorEvent.timestamp,
                    sensorEvent.sensor.getType(),
                    activityTypeId
            );

            long recordId = sensorRecordDao.insert(sensorRecord)[0];

            for (float value : sensorEvent.values) {
                SensorValueEntity sensorValue = new SensorValueEntity(
                        value,
                        (int) recordId
                );
                sensorValueDao.insert(sensorValue);
            }

            return null;
        }
    }
}

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
import com.adnagu.activityrecognition.database.entity.SensorRecordEntity;

public class SensorRecordService extends Service implements SensorEventListener {

    private final String DEBUG_TAG = getClass().getName();

    SensorRecordDao sensorRecordDao;
    SensorManager sensorManager;
    Sensor sensor;

    public SensorRecordService() {
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(DEBUG_TAG, "onStartCommand");

        //intent.getAction
        init();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(DEBUG_TAG, "onSensorChanged");
        Log.d(DEBUG_TAG, "SensorEntity Accuracy: " + sensorEvent.accuracy);

        /*
        if (sensorEvent.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
            return;
        */

        SensorRecordEntity sensorRecord = new SensorRecordEntity();
        sensorRecord.setSensorId(sensorEvent.sensor.getType());
        sensorRecord.setTimestamp(sensorEvent.timestamp);
        sensorRecord.setValue(String.valueOf(sensorEvent.values[0]));
        sensorRecordDao.insert(sensorRecord);

        Log.d(DEBUG_TAG, sensorEvent.sensor.getName() + ": " + sensorEvent.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected void init() {
        sensorRecordDao = AppDatabase.getInstance(this).sensorRecordDao();
        Log.d(DEBUG_TAG, "Number of sensor records: " + sensorRecordDao.getCount());

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (null != sensorManager) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ALL);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }
}

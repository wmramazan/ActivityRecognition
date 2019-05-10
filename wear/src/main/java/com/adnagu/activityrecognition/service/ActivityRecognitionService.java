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

import com.adnagu.common.database.AppDatabase;
import com.adnagu.common.database.dao.PredictionDao;
import com.adnagu.common.database.dao.PredictionRecordDao;
import com.adnagu.common.database.entity.PredictionEntity;
import com.adnagu.common.database.entity.PredictionRecordEntity;
import com.adnagu.common.ml.ActivityPrediction;
import com.adnagu.common.ml.FeatureExtraction;
import com.adnagu.common.ml.SlidingWindow;
import com.adnagu.common.model.SensorType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ActivityRecognitionService
 *
 * @author ramazan.vapurcu
 * Created on 04/21/2019
 */
public class ActivityRecognitionService extends Service implements SensorEventListener {

    private final String DEBUG_TAG = getClass().getName();

    PredictionDao predictionDao;
    PredictionRecordDao predictionRecordDao;

    SensorManager sensorManager;
    List<List<Float>> sensorValues;
    ActivityPrediction activityPrediction;
    FeatureExtraction featureExtraction;

    SensorType[] sensorTypes;
    long lastTimeMillis;

    int windowLength; // ms
    int windowDiffLength;
    int windowIndexAddition; // number of records

    int predictionId;

    boolean firstWindow;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void init() {
        AppDatabase appDatabase = AppDatabase.getInstance(this);
        predictionDao = appDatabase.predictionDao();
        predictionRecordDao = appDatabase.predictionRecordDao();

        sensorValues = new ArrayList<>();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (null != sensorManager)
            startRecognition();

        activityPrediction = new ActivityPrediction(this);
        featureExtraction = new FeatureExtraction();

        sensorTypes = SensorType.values();

        windowLength = SlidingWindow.WINDOW_LENGTH * 1000;
        windowDiffLength = windowLength * (100 - SlidingWindow.OVERLAPPING) / 100;
        windowIndexAddition = SlidingWindow.FREQUENCY * SlidingWindow.WINDOW_LENGTH * (100 - SlidingWindow.OVERLAPPING) / 100;

        firstWindow = true;
    }

    protected void startRecognition() {
        lastTimeMillis = 0;

        for (SensorType sensorType : sensorTypes) {
            sensorValues.add(new ArrayList<>());

            Sensor sensor = sensorManager.getDefaultSensor(sensorType.type);
            if (null == sensor)
                Log.d(DEBUG_TAG, "Unsupported sensor: " + sensorType);
            else
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }

        long[] results = predictionDao.insert(
                new PredictionEntity(
                        new Date(),
                        0
                )
        );
        predictionId = (int) results[0];
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
        if (lastTimeMillis != 0) {
            if (firstWindow) {
                if (System.currentTimeMillis() - lastTimeMillis >= windowLength) {
                    processWindow();
                    firstWindow = false;
                }
            } else {
                if (System.currentTimeMillis() - lastTimeMillis >= windowDiffLength)
                    processWindow();
            }
        }

        for (float value : event.values)
            sensorValues.get(getSensorId(event.sensor.getType())).add(value);

        lastTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void processWindow() {
        new Thread(() -> {
            List<Float> values = new ArrayList<>();

            for (List<Float> valueList : sensorValues)
                values.addAll(valueList);

            // Feature Extraction

            predictionRecordDao.insert(
                    new PredictionRecordEntity(
                            activityPrediction.predict(values),
                            predictionId,
                            new Date(),
                            false
                    )
            );

            clearWindow();
        }).start();
    }

    private void clearWindow() {
        for (int i = 0; i < sensorTypes.length; i++)
            sensorValues.set(i, sensorValues.get(i).subList(0, windowIndexAddition));
    }

    private int getSensorId(int sensorType) {
        for (int i = 0; i < sensorTypes.length; i++)
            if (sensorType == sensorTypes[i].type)
                return i;

        return -1;
    }
}

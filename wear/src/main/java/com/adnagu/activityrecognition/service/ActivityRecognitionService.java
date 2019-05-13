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

import com.adnagu.activityrecognition.utils.Utils;
import com.adnagu.common.database.AppDatabase;
import com.adnagu.common.database.dao.PredictionDao;
import com.adnagu.common.database.dao.PredictionRecordDao;
import com.adnagu.common.database.entity.PredictionEntity;
import com.adnagu.common.database.entity.SensorRecordEntity;
import com.adnagu.common.ml.ActivityPrediction;
import com.adnagu.common.ml.FeatureExtraction;
import com.adnagu.common.ml.FeatureFilter;
import com.adnagu.common.ml.SlidingWindow;
import com.adnagu.common.model.Activity;
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
    List<List<SensorRecordEntity>> sensorValues;
    ActivityPrediction activityPrediction;
    FeatureFilter featureFilter;
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
        stopRecognition();

        super.onDestroy();
    }

    protected void init() {
        AppDatabase appDatabase = AppDatabase.getInstance(this);
        predictionDao = appDatabase.predictionDao();
        predictionRecordDao = appDatabase.predictionRecordDao();

        sensorTypes = SensorType.values();
        sensorValues = new ArrayList<>();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (null != sensorManager)
            startRecognition();

        activityPrediction = new ActivityPrediction(this);
        featureFilter = new FeatureFilter();
        featureExtraction = new FeatureExtraction();

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
        if (lastTimeMillis == 0) {
            lastTimeMillis = System.currentTimeMillis();
        } else {
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

        sensorValues.get(getSensorId(event.sensor.getType())).add(
                new SensorRecordEntity(
                        Utils.toList(event.values)
                )
        );
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void processWindow() {
        lastTimeMillis = System.currentTimeMillis();
        List<Float> featureValues = new ArrayList<>();

        for (SensorType sensorType : sensorTypes) {
            List<List<Float>> segments = SlidingWindow.generateSegments(sensorValues.get(0), sensorType);

            for (List<Float> segment : segments) {
                featureExtraction.setFeatures(featureFilter.getFeatureArray());
                featureValues.addAll(featureExtraction.getFeatureValues(segment));
            }
        }

        int prediction = activityPrediction.predict(featureValues);
        Log.d(DEBUG_TAG, "Prediction: " + Activity.values()[prediction].name());

            /*predictionRecordDao.insert(
                    new PredictionRecordEntity(
                            prediction,
                            predictionId,
                            new Date(),
                            false
                    )
            );*/

        clearWindow();
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

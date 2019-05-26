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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.adnagu.activityrecognition.utils.Utils;
import com.adnagu.common.database.AppDatabase;
import com.adnagu.common.database.dao.PredictionDao;
import com.adnagu.common.database.dao.PredictionRecordDao;
import com.adnagu.common.database.entity.PredictionEntity;
import com.adnagu.common.database.entity.PredictionRecordEntity;
import com.adnagu.common.database.entity.SensorRecordEntity;
import com.adnagu.common.ml.ActivityPrediction;
import com.adnagu.common.ml.FeatureExtraction;
import com.adnagu.common.ml.FeatureFilter;
import com.adnagu.common.ml.SlidingWindow;
import com.adnagu.common.model.Activity;
import com.adnagu.common.model.SensorType;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

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
    private final double THRESHOLD_IDLE_ACTIVITY = .2;

    LocalBroadcastManager broadcastManager;
    Intent intent;

    PredictionDao predictionDao;
    PredictionRecordDao predictionRecordDao;

    SensorManager sensorManager;
    List<List<SensorRecordEntity>> sensorValues;
    FeatureFilter featureFilter;
    FeatureExtraction featureExtraction;
    ActivityPrediction activityPrediction;
    StandardDeviation std;

    SensorType[] sensorTypes;
    long lastTimeMillis;

    int windowLength; // ms
    int windowDiffLength;
    int windowIndexAddition; // number of records

    int predictionId;

    boolean firstWindow;
    boolean idleActivity;

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
        broadcastManager = LocalBroadcastManager.getInstance(getBaseContext());
        intent = new Intent(Utils.FILTER_ACTIVITY);

        AppDatabase appDatabase = AppDatabase.getInstance(this);
        predictionDao = appDatabase.predictionDao();
        predictionRecordDao = appDatabase.predictionRecordDao();

        sensorTypes = SensorType.values();
        sensorValues = new ArrayList<>();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (null != sensorManager)
            startRecognition();

        featureFilter = new FeatureFilter();
        featureExtraction = new FeatureExtraction();
        activityPrediction = new ActivityPrediction(this, featureFilter);
        std = new StandardDeviation(true);

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
        if (sensorManager != null)
            sensorManager.unregisterListener(this);

        predictionDao.setEndDate(predictionId, new Date());
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
        // TODO: 5/24/2019 Handle idle activity.

        featureFilter.init();
        lastTimeMillis = System.currentTimeMillis();
        List<Float> featureValues = new ArrayList<>();

        idleActivity = false;

        for (SensorType sensorType : sensorTypes) {
            List<List<Float>> segments = SlidingWindow.generateSegments(sensorValues.get(sensorType.ordinal()), sensorType);

            if (sensorType == SensorType.Accelerometer && isIdleActivity(segments)) {
                idleActivity = true;
                break;
            }

            for (List<Float> segment : segments) {
                featureExtraction.setFeatures(featureFilter.getFeatureArray());
                List<Float> values = featureExtraction.getFeatureValues(segment);
                featureValues.addAll(values);
            }
        }

        int prediction = idleActivity ? -1 : activityPrediction.predict(featureValues);

        if (prediction != -1) {
            Log.d(DEBUG_TAG, "Prediction: " + Activity.values()[prediction].name());

            predictionRecordDao.insert(
                    new PredictionRecordEntity(
                            prediction,
                            predictionId,
                            new Date(),
                            false
                    )
            );
        }

        intent.putExtra(Utils.ACTIVITY_ID, prediction);
        broadcastManager.sendBroadcast(intent);

        clearWindow();
    }

    private boolean isIdleActivity(List<List<Float>> segments) {
        double total = 0;
        for (int i = 0; i < 3; i++) {
            total += evaluateStd(segments.get(i));
        }

        return  total / 3 < THRESHOLD_IDLE_ACTIVITY;
    }

    private double evaluateStd(List<Float> segment) {
        double[] array = new double[segment.size()];

        for (int i = 0; i < array.length; i++)
            array[i] = segment.get(i);

        return std.evaluate(array);
    }

    private void clearWindow() {
        try {
            for (int i = 0; i < sensorTypes.length; i++)
                sensorValues.set(i, sensorValues.get(i).subList(windowIndexAddition, sensorValues.get(i).size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getSensorId(int sensorType) {
        for (int i = 0; i < sensorTypes.length; i++)
            if (sensorType == sensorTypes[i].type)
                return i;

        return -1;
    }
}

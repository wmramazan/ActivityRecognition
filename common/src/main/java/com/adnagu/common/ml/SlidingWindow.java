package com.adnagu.common.ml;

import android.util.Log;

import com.adnagu.common.database.dao.SensorRecordDao;
import com.adnagu.common.database.entity.ActivityRecordEntity;
import com.adnagu.common.database.entity.SensorRecordEntity;
import com.adnagu.common.model.Activity;
import com.adnagu.common.model.SensorType;
import com.adnagu.common.utils.listener.OnWindowListener;

import java.util.ArrayList;
import java.util.List;

/**
 * ArffFile
 *
 * @author ramazan.vapurcu
 * Created on 03/29/2019
 */
public class SlidingWindow {

    public static final int WINDOW_LENGTH = 6;
    public static final int OVERLAPPING = 50;
    public static final int LIMIT = 0;
    public static final int FREQUENCY = 100;

    private final String DEBUG_TAG = getClass().getName();

    private SensorRecordDao sensorRecordDao;
    private OnWindowListener onWindowListener;

    private ActivityRecordEntity activityRecord;
    private List<List<SensorRecordEntity>> sensorRecords;

    private SensorType[] sensorTypes;

    private int windowIndexLength;
    private int windowIndexAddition;
    private float range;
    private float limit;

    private int windowStartIndex;
    private int windowEndIndex;
    private int windowLastIndex;

    private float[] durations;

    public SlidingWindow(SensorRecordDao sensorRecordDao, OnWindowListener onWindowListener) {
        this.sensorRecordDao = sensorRecordDao;
        this.onWindowListener = onWindowListener;

        durations = new float[Activity.values().length];
        setParameters(WINDOW_LENGTH, OVERLAPPING, LIMIT);

        sensorTypes = SensorType.values();
        sensorRecords = new ArrayList<>();
    }

    /**
     * Set parameters for sliding window.
     * @param windowLength integer value (second)
     * @param overlapping integer value (percent)
     * @param limit integer value (minute)
     */
    public void setParameters(int windowLength, int overlapping, int limit) {
        this.windowIndexLength = windowLength * FREQUENCY;
        this.windowIndexAddition = (windowIndexLength * (100 - overlapping)) / 100;
        this.range = (float) windowIndexAddition / FREQUENCY;
        this.limit = limit * 60;
    }

    public void processRecord(ActivityRecordEntity activityRecord) {
        this.activityRecord = activityRecord;

        if (!prepareSensorRecords())
            return;

        resetWindow();

        String activityName = Activity.values()[activityRecord.getActivityId()].name();
        Log.d(DEBUG_TAG, "Activity Record: " + activityName);
        Log.d(DEBUG_TAG, "Window Index Length: " + windowIndexLength);
        Log.d(DEBUG_TAG, "Window Index Addition: " + windowIndexAddition);

        while (nextWindow()) {
            onWindowListener.onWindowStart();

            for (SensorType sensorType : sensorTypes) {
                List<List<Float>> segments = generateSegments(sensorType);

                for (List<Float> segment : segments)
                    onWindowListener.onWindowSegment(segment);
            }

            onWindowListener.onWindowFinish();
        }
    }

    private boolean prepareSensorRecords() {
        clearRecords();

        for (SensorType sensor : sensorTypes) {
            List<SensorRecordEntity> sensorRecordEntities = sensorRecordDao.getAll(activityRecord.getId(), sensor.type);
            if (sensorRecordEntities.size() == 0)
                return false;

            sensorRecords.add(sensorRecordEntities);
        }


        windowLastIndex = sensorRecords.get(0).size();

        for (int i = 1; i < sensorRecords.size(); i++)
            if (sensorRecords.get(i).size() < windowLastIndex)
                windowLastIndex = sensorRecords.get(i).size();

        return true;
    }

    private void clearRecords() {
        for (List<SensorRecordEntity> list : sensorRecords)
            list.clear();

        sensorRecords.clear();
    }

    private void resetWindow() {
        windowStartIndex = 0;
        windowEndIndex = 0;
    }

    private List<List<Float>> generateSegments(SensorType sensorType) {
        List<List<Float>> segments = new ArrayList<>();
        for (char ignored : sensorType.values)
            segments.add(new ArrayList<>());

        List<SensorRecordEntity> window = getWindow(sensorType.ordinal());

        for (SensorRecordEntity sensorRecordEntity : window) {
            List<Float> values = sensorRecordEntity.getValues();

            if (sensorType.values.length > 3) {
                float x_square = values.get(0) * values.get(0);
                float y_square = values.get(1) * values.get(1);
                float z_square = values.get(2) * values.get(2);
                values.add(
                        (float) Math.sqrt(x_square + y_square + z_square)
                );

                if (sensorType.values.length == 5)
                    values.add(
                            (float) Math.sqrt(x_square + z_square)
                    );
            }

            for (int i = 0; i < sensorType.values.length; i++)
                segments.get(i).add(
                        values.get(i)
                );
        }

        return segments;
    }

    private boolean nextWindow() {
        increaseWindowIndices();

        durations[activityRecord.getActivityId()] += range;

        Log.d(DEBUG_TAG, "Window: " + windowStartIndex + ", " + windowEndIndex);

        if (windowStartIndex >= windowEndIndex || (limit != 0 && durations[activityRecord.getActivityId()] > limit)) {
            resetWindow();
            return false;
        }

        return true;
    }

    private void increaseWindowIndices() {
        if (windowEndIndex == 0) {
            windowStartIndex = 0;
            windowEndIndex = windowIndexLength;
        } else {
            windowStartIndex += windowIndexAddition;
            windowEndIndex += windowIndexAddition;
        }

        if (windowEndIndex > windowLastIndex)
            windowEndIndex = windowLastIndex;
    }

    private List<SensorRecordEntity> getWindow(int sensorIndex) {
        return sensorRecords.get(sensorIndex).subList(windowStartIndex, windowEndIndex);
    }
}

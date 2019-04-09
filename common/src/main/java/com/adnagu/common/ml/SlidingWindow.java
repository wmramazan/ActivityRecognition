package com.adnagu.common.ml;

import android.util.Log;

import com.adnagu.common.database.dao.SensorRecordDao;
import com.adnagu.common.database.entity.ActivityRecordEntity;
import com.adnagu.common.database.entity.SensorRecordEntity;
import com.adnagu.common.model.Activity;
import com.adnagu.common.model.SensorType;
import com.adnagu.common.utils.listener.OnWindowListener;

import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ArffFile
 *
 * @author ramazan.vapurcu
 * Created on 03/29/2019
 */
public class SlidingWindow {

    public static final int WINDOW_LENGTH = 4;
    public static final int OVERLAPPING = 50;
    public static final int LIMIT = 0;

    private final String DEBUG_TAG = getClass().getName();

    private SensorRecordDao sensorRecordDao;
    private OnWindowListener onWindowListener;

    private ActivityRecordEntity activityRecord;

    private int windowLength;
    private int overlapping;
    private int range;
    private int limit;

    private Date windowStartDate;
    private Date windowEndDate;
    private Date lastDate;

    private long[] durations;

    public SlidingWindow(SensorRecordDao sensorRecordDao, OnWindowListener onWindowListener) {
        this.sensorRecordDao = sensorRecordDao;
        this.onWindowListener = onWindowListener;

        durations = new long[Activity.values().length];
        setParameters(WINDOW_LENGTH, OVERLAPPING, LIMIT);
    }

    /**
     * Set parameters for sliding window.
     * @param windowLength integer value (second)
     * @param overlapping integer value (percent)
     * @param limit integer value (minute)
     */
    public void setParameters(int windowLength, int overlapping, int limit) {
        this.windowLength = windowLength * 1000; // ms
        this.overlapping = (windowLength * overlapping) * 10; // ms
        this.range = this.windowLength - this.overlapping;
        this.limit = limit * 60 * 1000;
    }

    public void processRecord(ActivityRecordEntity activityRecord) {
        this.activityRecord = activityRecord;

        String activityName = Activity.values()[activityRecord.getActivityId()].name();
        Log.d(DEBUG_TAG, "Activity Record: " + activityName);
        Log.d(DEBUG_TAG, "Window Length: " + windowLength);
        Log.d(DEBUG_TAG, "Overlapping: " + overlapping);

        while (nextWindow()) {
            onWindowListener.onWindowStart();

            for (SensorType sensorType : SensorType.values()) {
                List<List<Float>> segments = generateSegments(sensorType);

                for (List<Float> segment : segments)
                    onWindowListener.onWindowSegment(segment);
            }

            onWindowListener.onWindowFinish();
        }
    }

    private List<List<Float>> generateSegments(SensorType sensorType) {
        List<List<Float>> segments = new ArrayList<>();
        for (char ignored : sensorType.values)
            segments.add(new ArrayList<>());

        List<SensorRecordEntity> window = getWindow(sensorType.id);

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
        if (windowStartDate == null) {
            SensorRecordEntity firstRecord = sensorRecordDao.getFirst(activityRecord.getId());
            if (firstRecord == null)
                return false;

            windowStartDate = firstRecord.getDate();
            windowEndDate = DateUtils.addMilliseconds(windowStartDate, windowLength);
            lastDate = sensorRecordDao.getLast(activityRecord.getId()).getDate();
        } else {
            windowStartDate = DateUtils.addMilliseconds(windowStartDate, overlapping);
            windowEndDate = DateUtils.addMilliseconds(windowEndDate, overlapping);
        }

        durations[activityRecord.getActivityId()] += range;

        Log.d(DEBUG_TAG, "WindowStartDate: " + windowStartDate);
        Log.d(DEBUG_TAG, "WindowEndDate: " + windowEndDate);

        if (windowStartDate.after(lastDate) || (limit != 0 && durations[activityRecord.getActivityId()] > limit)) {
            windowStartDate = null;
            return false;
        }

        return true;
    }

    private List<SensorRecordEntity> getWindow(int sensorId) {
        return sensorRecordDao.getBetween(activityRecord.getId(), sensorId, windowStartDate, windowEndDate);
    }
}

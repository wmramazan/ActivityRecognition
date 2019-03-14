package com.adnagu.activityrecognition.ml;

import android.content.Context;
import android.util.Log;

import com.adnagu.activityrecognition.database.AppDatabase;
import com.adnagu.activityrecognition.database.dao.ActivityRecordDao;
import com.adnagu.activityrecognition.database.dao.SensorRecordDao;
import com.adnagu.activityrecognition.database.entity.ActivityRecordEntity;
import com.adnagu.activityrecognition.database.entity.SensorRecordEntity;
import com.adnagu.activityrecognition.model.Activity;
import com.adnagu.activityrecognition.model.Feature;
import com.adnagu.activityrecognition.model.SensorType;
import com.adnagu.activityrecognition.utils.listener.OnProgressListener;

import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ArffFile
 *
 * @author ramazan.vapurcu
 * Created on 12/5/2018
 */
public class ArffFile {

    private final String DEBUG_TAG = "ArffFile";
    private final String FILE_NAME = "ActivityRecords.arff";

    private Context context;
    private OnProgressListener onProgressListener;

    private OutputStreamWriter writer;
    private ActivityRecordDao activityRecordDao;
    private SensorRecordDao sensorRecordDao;

    private FeatureExtraction featureExtraction;

    private Date windowStartDate;
    private Date windowEndDate;
    private Date lastDate;

    private int windowLength;
    private int overlapping;

    public ArffFile(Context context) {
        this.context = context;

        init();
    }

    public ArffFile(Context context, OnProgressListener onProgressListener) {
        this.context = context;
        this.onProgressListener = onProgressListener;

        init();
    }

    private void init() {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        activityRecordDao = appDatabase.activityRecordDao();
        sensorRecordDao = appDatabase.sensorRecordDao();
    }

    private void createFile() throws IOException {
        writer = new OutputStreamWriter(
                context.openFileOutput(
                        FILE_NAME,
                        Context.MODE_PRIVATE
                )
        );
        write("@relation activity_recognition\n\n");
    }

    private void write(String str) throws IOException {
        writer.write(str);
    }

    private void writeAttributes() throws IOException {
        for (SensorType sensorType : SensorType.values())
            for (char value : sensorType.values)
                for (Feature feature : Feature.values())
                    writer.write("@attribute " + sensorType.prefix + "_" + feature.name() + "_" + value + " numeric\n");

        StringBuilder activities = new StringBuilder();
        for (Activity activity : Activity.values())
            activities.append("'").append(activity.name()).append("',");

        activities.deleteCharAt(activities.length() - 1);

        write("@attribute 'Class' {" + activities.toString() + "}\n");
    }

    private void writeRecords() throws IOException {
        write("@data\n");

        featureExtraction = new FeatureExtraction();

        List<ActivityRecordEntity> activityRecordEntities = activityRecordDao.getAll();
        for (int index = 0; index < activityRecordEntities.size(); index++) {
            ActivityRecordEntity activityRecordEntity = activityRecordEntities.get(index);

            if (onProgressListener != null)
                onProgressListener.onProgress((index * 100) / activityRecordEntities.size());

            String activityName = Activity.values()[activityRecordEntity.getActivityId()].name();
            Log.d(DEBUG_TAG, "Activity Record: " + activityName);

            while (nextWindow(activityRecordEntity.getId())) {
                for (SensorType sensorType : SensorType.values()) {
                    List<List<Float>> segments = new ArrayList<>();
                    for (char ignored : sensorType.values)
                        segments.add(new ArrayList<>());

                    List<SensorRecordEntity> window = getWindow(activityRecordEntity.getId(), sensorType.id);

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

                    for (List<Float> segment : segments) {
                        List<Float> featureValues = featureExtraction.getFeatureValues(segment);
                        for (Float value : featureValues) {
                            //writer.write(String.valueOf(value) + ",");
                            writer.write(String.valueOf(value.isNaN() ? 0.0 : value) + ",");
                        }
                    }
                }

                write("'" + activityName + "'\n");
            }

            /*for (SensorType sensorType : SensorType.values()) {
                List<SensorRecordEntity> sensorRecordEntities = sensorRecordDao.getAll(activityRecordEntity.getId(), sensorType.id);

                windowStartIndex = 0;
                windowEndIndex = 1;

                Log.d(DEBUG_TAG, "SensorRecordEntities Size: " + sensorRecordEntities.size());
                while (windowStartIndex < sensorRecordEntities.size()) {
                    Log.d(DEBUG_TAG, "WindowStartIndex: " + windowStartIndex);
                    Date date = sensorRecordEntities.get(windowStartIndex).getDate();
                    date = DateUtils.addSeconds(date, windowLength);

                    while (windowEndIndex < sensorRecordEntities.size() && date.after(sensorRecordEntities.get(windowEndIndex).getDate()))
                        windowEndIndex++;

                    Log.d(DEBUG_TAG, "WindowEndIndex: " + windowEndIndex);

                    List<List<Float>> segments = new ArrayList<>();
                    for (char ignored : sensorType.values)
                        segments.add(new ArrayList<>());

                    List<SensorRecordEntity> window = sensorRecordEntities.subList(windowStartIndex, windowEndIndex);
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

                    for (List<Float> segment : segments) {
                        List<Float> featureValues = featureExtraction.getFeatureValues(segment);
                        for (Float value : featureValues) {
                            //writer.write(String.valueOf(value) + ",");
                            writer.write(String.valueOf(value.isNaN() ? 0.0 : value) + ",");
                        }
                    }

                    write("'" + activityName + "'\n");

                    if (windowEndIndex == sensorRecordEntities.size())
                        break;

                    int value = (windowEndIndex - windowStartIndex) * overlapping;
                    if (value > 100)
                        windowStartIndex += value / 100;
                    else
                        windowStartIndex = windowEndIndex;
                }
            }*/
        }
    }

    private void close() throws IOException {
        writer.close();
    }

    private boolean nextWindow(int activityRecordId) {
        if (windowStartDate == null) {
            SensorRecordEntity firstRecord = sensorRecordDao.getFirst(activityRecordId);
            if (firstRecord == null)
                return false;

            windowStartDate = firstRecord.getDate();
            windowEndDate = DateUtils.addSeconds(windowStartDate, windowLength);
            lastDate = sensorRecordDao.getLast(activityRecordId).getDate();
        } else {
            windowStartDate = DateUtils.addMilliseconds(windowStartDate, overlapping);
            windowEndDate = DateUtils.addMilliseconds(windowEndDate, overlapping);
        }

        Log.d(DEBUG_TAG, "WindowStartDate: " + windowStartDate);
        Log.d(DEBUG_TAG, "WindowEndDate: " + windowEndDate);

        if (windowStartDate.after(lastDate)) {
            windowStartDate = null;
            return false;
        }

        return true;
    }

    private List<SensorRecordEntity> getWindow(int activityRecordId, int sensorRecordId) {
        return sensorRecordDao.getBetween(activityRecordId, sensorRecordId, windowStartDate, windowEndDate);
    }

    /**
     * Save as ARFF file for Weka.
     * @param windowLength integer value (second)
     * @param overlapping integer value (percent)
     */
    public void save(int windowLength, int overlapping) {
        this.windowLength = windowLength;
        this.overlapping = (windowLength * overlapping) * 10; // ms

        Log.d(DEBUG_TAG, "Saving ARFF file.");
        Log.d(DEBUG_TAG, "Window Length: " + windowLength);
        Log.d(DEBUG_TAG, "Overlapping: " + overlapping);

        try {
            createFile();
            writeAttributes();
            writeRecords();
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

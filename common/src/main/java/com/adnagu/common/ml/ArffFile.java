package com.adnagu.common.ml;

import android.content.Context;

import com.adnagu.common.database.AppDatabase;
import com.adnagu.common.database.dao.ActivityRecordDao;
import com.adnagu.common.database.dao.SensorRecordDao;
import com.adnagu.common.database.entity.ActivityRecordEntity;
import com.adnagu.common.model.Activity;
import com.adnagu.common.model.Feature;
import com.adnagu.common.model.SensorType;
import com.adnagu.common.utils.listener.OnProgressListener;
import com.adnagu.common.utils.listener.OnWindowListener;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * ArffFile
 *
 * @author ramazan.vapurcu
 * Created on 12/5/2018
 */
public class ArffFile {

    public static final String TRAINING_FILE_NAME = "TrainingDataSet.arff";
    public static final String TEST_FILE_NAME     = "TestDataSet.arff";
    public static final int    PERCENTAGE_SPLIT   = 70;

    private enum State {
        ALL,
        TRAINING,
        TEST
    }

    private final String DEBUG_TAG = getClass().getName();

    private Context context;
    private OnProgressListener onProgressListener;

    protected OutputStreamWriter trainingWriter;
    protected OutputStreamWriter testWriter;

    private ActivityRecordDao activityRecordDao;
    private SensorRecordDao sensorRecordDao;

    private FeatureFilter featureFilter;
    private FeatureExtraction featureExtraction;
    private SlidingWindow slidingWindow;

    private String filter;
    private String activityName;
    private State state;

    public ArffFile(Context context) {
        this.context = context;

        init();
    }

    public ArffFile(Context context, OnProgressListener onProgressListener) {
        this.context = context;
        this.onProgressListener = onProgressListener;

        init();
    }

    public ArffFile(ActivityRecordDao activityRecordDao, SensorRecordDao sensorRecordDao) {
        this.activityRecordDao = activityRecordDao;
        this.sensorRecordDao = sensorRecordDao;

        init();
    }

    public ArffFile(ActivityRecordDao activityRecordDao, SensorRecordDao sensorRecordDao, OnProgressListener onProgressListener) {
        this.activityRecordDao = activityRecordDao;
        this.sensorRecordDao = sensorRecordDao;
        this.onProgressListener = onProgressListener;

        init();
    }

    private void init() {
        if (context != null) {
            AppDatabase appDatabase = AppDatabase.getInstance(context);
            activityRecordDao = appDatabase.activityRecordDao();
            sensorRecordDao = appDatabase.sensorRecordDao();
        }

        featureFilter = filter == null ? new FeatureFilter() : new FeatureFilter(filter);
        featureExtraction = new FeatureExtraction();
        slidingWindow = new SlidingWindow(sensorRecordDao, new OnWindowListener() {
            @Override
            public void onWindowStart() {
                featureFilter.init();
            }

            @Override
            public void onWindowSegment(List<Float> segment) {
                featureExtraction.setFeatures(featureFilter.getFeatureArray());

                List<Float> featureValues = featureExtraction.getFeatureValues(segment);
                for (Float value : featureValues)
                    write((value.isNaN() ? 0.0 : value) + ",");
            }

            @Override
            public void onWindowFinish() {
                write("'" + activityName + "'\n");
            }
        });
    }

    protected void write(String str) {
        switch (state) {
            case ALL:
                write(trainingWriter, str);
                write(testWriter, str);
                break;
            case TRAINING:
                write(trainingWriter, str);
                break;
            case TEST:
                write(testWriter, str);
                break;
        }
    }

    private void write(OutputStreamWriter writer, String str) {
        try {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void createFiles() throws IOException {
        trainingWriter = new OutputStreamWriter(
                context.openFileOutput(
                        TRAINING_FILE_NAME,
                        Context.MODE_PRIVATE
                )
        );
        testWriter = new OutputStreamWriter(
                context.openFileOutput(
                        TEST_FILE_NAME,
                        Context.MODE_PRIVATE
                )
        );
        write("@relation activity_recognition\n\n");
    }

    private void writeAttributes() {
        for (SensorType sensorType : SensorType.values())
            for (char value : sensorType.values)
                for (Feature feature : Feature.values())
                    if (featureFilter.isValidFeature())
                        write("@attribute " + sensorType.prefix + "_" + feature.name() + "_" + value + " numeric\n");

        StringBuilder activities = new StringBuilder();
        for (Activity activity : Activity.values())
            activities.append("'").append(activity.name()).append("',");

        activities.deleteCharAt(activities.length() - 1);

        write("@attribute 'Class' {" + activities.toString() + "}\n");
    }

    private void writeRecords() {
        write("@data\n");

        int trainingRecords = 0;
        int testRecords = 0;

        for (Activity activity : Activity.values()) {
            List<ActivityRecordEntity> activityRecords = activityRecordDao.getRecords(activity.ordinal());

            if (onProgressListener != null)
                onProgressListener.onProgress(((activity.ordinal() + 1) * 100) / Activity.values().length);

            activityName = activity.name();

            if (activityRecords.size() > 0) {
                int splitIndex = activityRecords.size() * PERCENTAGE_SPLIT / 100;

                //Log.d(DEBUG_TAG, "Activity Record Size: " + activityRecords.size());
                //Log.d(DEBUG_TAG, "Split Index: " + splitIndex);

                state = State.TRAINING;

                int i;
                for (i = 0; i < splitIndex; i++) {
                    trainingRecords++;
                    slidingWindow.processRecord(activityRecords.get(i));
                }

                state = State.TEST;

                for (i = splitIndex; i < activityRecords.size(); i++) {
                    testRecords++;
                    slidingWindow.processRecord(activityRecords.get(i));
                }
            }
        }

        System.out.println("Training Records: " + trainingRecords);
        System.out.println("Test Records: " + testRecords);
        System.out.println("Training Rate: " + (float) trainingRecords / (trainingRecords + testRecords));

        /*List<ActivityRecordEntity> activityRecordEntities = activityRecordDao.getTrainingRecords();
        for (int index = 0; index < activityRecordEntities.size(); index++) {
            ActivityRecordEntity activityRecord = activityRecordEntities.get(index);

            *//*if (activityRecordEntity.getId() < 26)
                continue;*//*

            if (onProgressListener != null)
                onProgressListener.onProgress((index * 100) / (activityRecordEntities.size() - 1));

            activityName = Activity.values()[activityRecord.getActivityId()].name();

            slidingWindow.processRecord(activityRecord);
        }*/
    }

    private void close() throws IOException {
        trainingWriter.close();
        testWriter.close();
    }

    /**
     * Save as ARFF file for Weka.
     */
    public void save() {
        //Log.d(DEBUG_TAG, "Saving ARFF file.");
        state = State.ALL;

        try {
            createFiles();
            writeAttributes();
            writeRecords();
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save as ARFF file for Weka.
     * @param windowLength integer value (second)
     * @param overlapping integer value (percent)
     */
    public void save(int windowLength, int overlapping) {
        slidingWindow.setParameters(windowLength, overlapping, SlidingWindow.LIMIT);
        save();
    }

    public void save(int windowLength, int overlapping, String filter) {
        save(windowLength, overlapping);
        this.filter = filter;
    }

    /**
     * Save as ARFF file for Weka.
     * @param windowLength integer value (second)
     * @param overlapping integer value (percent)
     * @param limit integer value (minute)
     */
    public void save(int windowLength, int overlapping, int limit) {
        slidingWindow.setParameters(windowLength, overlapping, limit);
        save();
    }
}

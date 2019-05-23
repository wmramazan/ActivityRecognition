package com.adnagu.common.arff;

import android.content.Context;

import com.adnagu.common.database.dao.ActivityRecordDao;
import com.adnagu.common.database.dao.SensorRecordDao;
import com.adnagu.common.ml.ArffFile;
import com.adnagu.common.utils.listener.OnProgressListener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * MyArffFile
 *
 * @author ramazan.vapurcu
 * Created on 4/30/2019
 */
public class MyArffFile extends ArffFile {

    String tag;

    public MyArffFile(Context context) {
        super(context);
    }

    public MyArffFile(Context context, OnProgressListener onProgressListener) {
        super(context, onProgressListener);
    }

    public MyArffFile(ActivityRecordDao activityRecordDao, SensorRecordDao sensorRecordDao) {
        super(activityRecordDao, sensorRecordDao);
    }

    public MyArffFile(ActivityRecordDao activityRecordDao, SensorRecordDao sensorRecordDao, OnProgressListener onProgressListener) {
        super(activityRecordDao, sensorRecordDao, onProgressListener);
    }

    @Override
    protected void createFiles() throws IOException {
        trainingWriter = new OutputStreamWriter(
                new FileOutputStream(
                        "c://arff//training" + tag + ".arff"
                )
        );
        testWriter = new OutputStreamWriter(
                new FileOutputStream(
                        "c://arff//test" + tag + ".arff"
                )
        );
        write("@relation activity_recognition\n\n");
    }

    @Override
    public void save(int windowLength, int overlapping) {
        if (tag == null)
            tag = "_w" + windowLength + "o" + overlapping;
        System.out.println("ArffFile" + tag);
        super.save(windowLength, overlapping);
    }

    public void save(int windowLength, int overlapping, String filter) {
        tag = "_w" + windowLength + "o" + overlapping + "_filtered";
        super.save(windowLength, overlapping, filter);
    }

    public void save(int windowLength, int overlapping, String filter, String filterTag) {
        tag = "_w" + windowLength + "o" + overlapping + "_" + filterTag;
        super.save(windowLength, overlapping, filter);
    }
}

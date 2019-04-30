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
    public MyArffFile(Context context) {
        super(context);
    }

    public MyArffFile(Context context, OnProgressListener onProgressListener) {
        super(context, onProgressListener);
    }

    public MyArffFile(ActivityRecordDao activityRecordDao, SensorRecordDao sensorRecordDao) {
        super(activityRecordDao, sensorRecordDao);
    }

    @Override
    protected void createFiles() throws IOException {
        trainingWriter = new OutputStreamWriter(
                new FileOutputStream(
                        "c://arff//" + TRAINING_FILE_NAME
                )
        );
        testWriter = new OutputStreamWriter(
                new FileOutputStream(
                        "c://arff//" + TEST_FILE_NAME
                )
        );
        write("@relation activity_recognition\n\n");
    }
}

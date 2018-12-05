package com.adnagu.activityrecognition.utils;

import com.adnagu.activityrecognition.database.entity.SensorRecordEntity;

import java.util.List;

/**
 * ArffFile
 *
 * @author ramazan.vapurcu
 * Created on 12/5/2018
 */
public class ArffFile {

    public static final String  FILE_NAME = "ActivityRecords.arff";
    public static final int     NANO_SECONDS = 1000000000;

    public static void saveAsArff(List<SensorRecordEntity> sensorRecords, int windowLength) {

        int index;
        long lastTimestamp;

        for (index = 0; index < sensorRecords.size(); index++) {
            SensorRecordEntity sensorRecord = sensorRecords.get(index);

            lastTimestamp = sensorRecord.getTimestamp();
        }

    }

}

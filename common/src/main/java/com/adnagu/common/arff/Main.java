package com.adnagu.common.arff;

import com.adnagu.common.ml.FeatureFilter;

/**
 * Main
 * The class has been created in order to generate ARFF files without the Android platform.
 *
 * @author ramazan.vapurcu
 * Created on 4/29/2019
 */
public class Main {

    public static void main(String[] args) {
        MyDatabase database = new MyDatabase();

        MyArffFile arffFile = new MyArffFile(
                database.activityRecordDao,
                database.sensorRecordDao,
                progress -> System.out.println("Progress: " + progress)
        );

        //arffFile.save(6, 50);

        arffFile.save(6, 50, FeatureFilter.FEATURE_FILTER_TOP_60, "top60");
        arffFile.save(6, 50, FeatureFilter.FEATURE_FILTER_TOP_70, "top70");
        arffFile.save(6, 50, FeatureFilter.FEATURE_FILTER_TOP_80, "top80");
        arffFile.save(6, 50, FeatureFilter.FEATURE_FILTER_TOP_90, "top90");
        arffFile.save(6, 50, FeatureFilter.FEATURE_FILTER_TOP_100, "top100");

        database.disconnect();
    }
}

package com.adnagu.common.arff;

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

        arffFile.save(6, 50);

        /*arffFile.save(6, 40);
        arffFile.save(6, 45);
        arffFile.save(6, 55);
        arffFile.save(6, 60);*/

        database.disconnect();
    }
}

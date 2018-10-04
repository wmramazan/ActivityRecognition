package com.adnagu.activityrecognition.model;

import com.adnagu.activityrecognition.R;

public enum Section {
    ActivityRecognition(R.string.section_activity_recognition, R.drawable.ic_activity_recognition_black_24dp),
    SensorRecord(R.string.section_sensor_record, R.drawable.ic_record_black_24dp),
    Statistic(R.string.section_statistic, R.drawable.ic_statistic_black_24dp);

    public final int titleRes;
    public final int drawableRes;

    Section(final int titleRes, final int drawableRes) {
        this.titleRes = titleRes;
        this.drawableRes = drawableRes;
    }
}

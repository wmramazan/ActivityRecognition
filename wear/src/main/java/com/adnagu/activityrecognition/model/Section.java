package com.adnagu.activityrecognition.model;

import com.adnagu.activityrecognition.R;

public enum Section {
    ActivityRecognition(R.string.section_activity_recognition, R.drawable.ic_activity_recognition),
    SensorRecord(R.string.section_sensor_record, R.drawable.ic_record),
    Statistic(R.string.section_statistic, R.drawable.ic_statistic);

    public final int title_res;
    public final int drawable_res;

    Section(final int title_res, final int drawable_res) {
        this.title_res = title_res;
        this.drawable_res = drawable_res;
    }
}

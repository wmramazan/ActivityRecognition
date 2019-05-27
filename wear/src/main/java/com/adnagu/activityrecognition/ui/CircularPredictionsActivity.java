package com.adnagu.activityrecognition.ui;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

import com.adnagu.activityrecognition.R;

import butterknife.ButterKnife;

public class CircularPredictionsActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_predictions);

        ButterKnife.bind(this);
    }
}

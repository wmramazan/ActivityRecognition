package com.adnagu.activityrecognition.ui;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import com.adnagu.activityrecognition.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ButterKnife.bind(this);

        // Enables Always-on
        setAmbientEnabled();
    }
}

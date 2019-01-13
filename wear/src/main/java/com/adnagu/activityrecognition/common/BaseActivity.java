package com.adnagu.activityrecognition.common;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.adnagu.activityrecognition.R;

/**
 * BaseActivity
 *
 * @author ramazan.vapurcu
 * Created on 10/10/2018
 */
public abstract class BaseActivity extends WearableActivity {

    ProgressBar progressBar;
    View contentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressBar = findViewById(R.id.progress_bar);
        contentFrame = findViewById(R.id.content_frame);
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        contentFrame.setVisibility(View.GONE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        contentFrame.setVisibility(View.VISIBLE);
    }

}

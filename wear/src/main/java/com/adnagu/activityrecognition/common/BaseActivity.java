package com.adnagu.activityrecognition.common;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;

import com.adnagu.activityrecognition.R;

/**
 * BaseActivity
 *
 * @author ramazan.vapurcu
 * Created on 10/10/2018
 */
public abstract class BaseActivity extends WearableActivity {

    View progress;
    View contentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        progress = findViewById(R.id.llProgress);
        contentFrame = findViewById(R.id.content_frame);
    }

    public void showProgress() {
        Log.d("BaseActivity", "showProgress");
        progress.setVisibility(View.VISIBLE);
        contentFrame.setVisibility(View.GONE);
    }

    public void hideProgress() {
        Log.d("BaseActivity", "hideProgress");
        progress.setVisibility(View.GONE);
        contentFrame.setVisibility(View.VISIBLE);
    }

}

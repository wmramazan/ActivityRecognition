package com.adnagu.activityrecognition.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;

import com.adnagu.activityrecognition.R;

public class SplashScreenActivity extends WearableActivity {

    protected final int DELAY_MILLIS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(
                () -> {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                },
                DELAY_MILLIS
        );
    }
}

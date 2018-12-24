package com.adnagu.activityrecognition.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.common.BaseActivity;

public class SplashScreenActivity extends BaseActivity {

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

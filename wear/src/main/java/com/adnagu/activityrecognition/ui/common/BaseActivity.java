package com.adnagu.activityrecognition.ui.common;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

import ticwear.design.utils.WindowUtils;

/**
 * BaseActivity
 *
 * @author ramazan.vapurcu
 * Created on 10/10/2018
 */
public abstract class BaseActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowUtils.clipToScreenShape(getWindow());
    }

}

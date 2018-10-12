package com.adnagu.activityrecognition.ui.section;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.adapter.ActivityAdapter;
import com.adnagu.activityrecognition.model.Activity;
import com.adnagu.activityrecognition.ui.common.BaseFragment;

import ticwear.design.drawable.CircularProgressDrawable;
import ticwear.design.widget.FloatingActionButton;

/**
 * SensorRecordFragment
 *
 * @author ramazan.vapurcu
 * Created on 10/3/2018
 */
public class SensorRecordFragment extends BaseFragment implements AmbientMode {

    private final String DEBUG_TAG = getClass().getName();

    FloatingActionButton recordButton;

    public SensorRecordFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor_record, container, false);

        recordButton = view.findViewById(R.id.button_record);
        recordButton.setProgressMode(CircularProgressDrawable.MODE_INDETERMINATE);
        forceRippleAnimation(recordButton);
        //recordButton.setShowProgress(true);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recordButton.callOnClick();
    }

    @Override
    public void onEnterAmbient() {

    }

    @Override
    public void onExitAmbient() {

    }

    protected void forceRippleAnimation(View view) {
        Drawable background = view.getBackground();
        if (background instanceof RippleDrawable)
            background.setHotspot(0, 0);

        view.setPressed(true);
        view.postDelayed(() -> view.setPressed(false), 500);
    }
}
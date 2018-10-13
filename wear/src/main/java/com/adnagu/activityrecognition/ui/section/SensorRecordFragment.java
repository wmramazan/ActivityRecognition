package com.adnagu.activityrecognition.ui.section;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.common.AmbientMode;
import com.adnagu.activityrecognition.common.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @BindView(R.id.button_record)
    FloatingActionButton recordButton;

    public SensorRecordFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor_record, container, false);
        ButterKnife.bind(this, view);

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
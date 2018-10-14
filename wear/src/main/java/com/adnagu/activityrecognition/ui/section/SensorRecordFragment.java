package com.adnagu.activityrecognition.ui.section;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.common.AmbientMode;
import com.adnagu.activityrecognition.common.BaseFragment;
import com.adnagu.activityrecognition.model.Activity;
import com.adnagu.activityrecognition.service.SensorRecordService;
import com.adnagu.activityrecognition.ui.ListActivity;
import com.adnagu.activityrecognition.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    private final Activity DEFAULT_ACTIVITY = Activity.UsingComputer;

    Intent serviceIntent;

    int selected_activity_index;
    boolean isRecording;

    @BindView(R.id.button_record)
    FloatingActionButton recordButton;

    @BindView(R.id.activity_name)
    TextView activityName;

    @BindView(R.id.record_text)
    TextView recordText;

    @OnClick(R.id.activity_name) void chooseActivity() {
        Intent intent = new Intent(getActivity(), ListActivity.class);
        startActivityForResult(intent, Utils.RequestCode.CHOOSE_ACTIVITY);
    }

    public SensorRecordFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor_record, container, false);
        ButterKnife.bind(this, view);

        recordButton.setProgressMode(CircularProgressDrawable.MODE_INDETERMINATE);
        recordButton.setOnClickListener(v -> toggleRecording());
        forceRippleAnimation(recordButton);

        selected_activity_index = DEFAULT_ACTIVITY.ordinal();
        activityName.setText(getString(DEFAULT_ACTIVITY.title_res));

        serviceIntent = new Intent(getActivity(), SensorRecordService.class);

        return view;
    }

    public void toggleRecording() {
        if (isRecording)
            stopRecording();
        else
            startRecording();
    }

    public void startRecording() {
        isRecording = true;

        getActivity().startService(serviceIntent);
        recordButton.setShowProgress(true);
        recordText.setText(R.string.recording);
    }

    public void stopRecording() {
        isRecording = false;

        getActivity().stopService(serviceIntent);
        recordButton.setShowProgress(false);
        recordText.setText(R.string.click_to_record);
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

    public void setActivity(int index) {
        Log.d(DEBUG_TAG, "Selected Activity: " + index);
        selected_activity_index = index;
        activityName.setText(getString(Activity.values()[index].title_res));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == android.app.Activity.RESULT_OK) {
            switch (requestCode) {
                case Utils.RequestCode.CHOOSE_ACTIVITY:
                    setActivity(data.getIntExtra(Utils.ACTIVITY_INDEX, 0));
                    break;
            }
        }
    }
}
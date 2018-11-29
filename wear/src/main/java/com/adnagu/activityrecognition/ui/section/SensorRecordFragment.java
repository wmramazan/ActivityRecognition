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

    private final String    DEBUG_TAG = getClass().getName();
    private final Activity  DEFAULT_ACTIVITY = Activity.UsingComputer;

    Intent serviceIntent;

    int selectedActivityIndex;
    boolean isRecording;

    @BindView(R.id.button_record)
    FloatingActionButton recordButton;

    @BindView(R.id.text_activity_name)
    TextView activityName;

    @BindView(R.id.text_record)
    TextView recordText;

    @OnClick(R.id.text_activity_name) void chooseActivity() {
        activityName.setEnabled(false);
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

        serviceIntent = new Intent(getContext(), SensorRecordService.class);

        setActivity(DEFAULT_ACTIVITY);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        activityName.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopRecording();
    }

    public void toggleRecording() {
        if (isRecording)
            stopRecording();
        else
            startRecording();
    }

    public void startRecording() {
        if (!isRecording) {
            isRecording = true;

            getContext().startService(serviceIntent);
            recordButton.setShowProgress(true);
            recordText.setText(R.string.recording);
        }
    }

    public void stopRecording() {
        if (isRecording) {
            isRecording = false;

            getContext().stopService(serviceIntent);
            recordButton.setShowProgress(false);
            recordText.setText(R.string.click_to_record);
        }
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

    public void setActivity(Activity activity) {
        setActivity(activity.ordinal());
    }

    public void setActivity(int index) {
        Log.d(DEBUG_TAG, "Selected Activity: " + index);
        selectedActivityIndex = index;
        activityName.setText(getString(Activity.values()[index].title_res));
        serviceIntent.putExtra(Utils.ACTIVITY_INDEX, selectedActivityIndex);
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
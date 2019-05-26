package com.adnagu.activityrecognition.ui.section;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.common.BaseFragment;
import com.adnagu.activityrecognition.service.ActivityRecognitionService;
import com.adnagu.common.model.Activity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ticwear.design.widget.FloatingActionButton;

/**
 * ActivityRecognitionFragment
 *
 * @author ramazan.vapurcu
 * Created on 10/3/2018
 */
public class ActivityRecognitionFragment extends BaseFragment {

    private final String DEBUG_TAG = getClass().getName();

    Intent serviceIntent;

    boolean predicting;

    @BindView(R.id.button_activity)
    FloatingActionButton activityButton;

    @OnClick(R.id.button_activity) void toggleRecognition() {
        if (isPredicting())
            stopRecognition();
        else
            startRecognition();
    }

    @BindView(R.id.text_activity)
    TextView activityText;

    public ActivityRecognitionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_recognition, container, false);
        ButterKnife.bind(this, view);

        serviceIntent = new Intent(getContext(), ActivityRecognitionService.class);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopRecognition();
    }

    public void startRecognition() {
        if (!isPredicting()) {
            vibrate();
            setIdleActivity();

            getContext().startService(serviceIntent);
            activityButton.setShowProgress(true);
            activityText.setText(R.string.perform_activity);

            acquireWakeLock();
            predicting = true;
        }
    }

    public void stopRecognition() {
        if (isPredicting()) {
            vibrate();
            setStart();

            getContext().stopService(serviceIntent);
            activityButton.setShowProgress(false);
            activityText.setText(R.string.click_to_recognize);

            predict();

            releaseWakeLock();
            predicting = false;
        }
    }

    @Override
    public void onEnterAmbient() {
        activityButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), android.R.color.black)));
    }

    @Override
    public void onExitAmbient() {
        activityButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.wear_primary)));
    }

    private void predict() {
        /*activityText.setText(R.string.predicting);
        activityPrediction.clear();

        new Thread(() -> {
            featureValues = new ArrayList<>();
            activityRecord = activityRecordDao.getLastRecordForTest();

            slidingWindow.processRecord(activityRecord);

            // Update activity record.
            Activity activity = Activity.values()[activityPrediction.getPrediction()];

            handler.post(() -> {
                activityText.setText(activity.title_res);
                activityButton.setImageResource(activity.drawable_res);
            });
            vibrate();
        }).start();*/
    }

    public void setStart() {
        activityText.setText(R.string.click_to_recognize);
        activityButton.setImageResource(R.drawable.ic_start);
    }

    public void setIdleActivity() {
        activityText.setText(R.string.activity_idle);
        activityButton.setImageResource(R.drawable.ic_idle);
    }

    public void setActivity(Activity activity) {
        activityText.setText(activity.title_res);
        activityButton.setImageResource(activity.drawable_res);
    }

    public boolean isPredicting() {
        return predicting;
    }
}

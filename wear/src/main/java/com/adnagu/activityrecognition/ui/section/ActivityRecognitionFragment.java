package com.adnagu.activityrecognition.ui.section;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.common.BaseFragment;
import com.adnagu.activityrecognition.service.ActivityRecognitionService;

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
            getContext().startService(serviceIntent);
            activityButton.setShowProgress(true);
            activityText.setText(R.string.perform_activity);

            acquireWakeLock();
            predicting = true;
        }
    }

    public void stopRecognition() {
        if (isPredicting()) {
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

    }

    @Override
    public void onExitAmbient() {

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

    public boolean isPredicting() {
        return predicting;
    }
}

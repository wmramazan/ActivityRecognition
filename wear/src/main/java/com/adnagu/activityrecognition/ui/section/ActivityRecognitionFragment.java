package com.adnagu.activityrecognition.ui.section;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.common.BaseFragment;
import com.adnagu.activityrecognition.service.SensorRecordService;
import com.adnagu.activityrecognition.utils.Utils;
import com.adnagu.common.database.AppDatabase;
import com.adnagu.common.database.dao.ActivityRecordDao;
import com.adnagu.common.database.dao.SensorRecordDao;
import com.adnagu.common.database.entity.ActivityRecordEntity;
import com.adnagu.common.ml.ActivityPrediction;
import com.adnagu.common.ml.FeatureExtraction;
import com.adnagu.common.ml.FeatureFilter;
import com.adnagu.common.ml.SlidingWindow;
import com.adnagu.common.model.Activity;
import com.adnagu.common.utils.listener.OnWindowListener;

import java.util.ArrayList;
import java.util.List;

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

    ActivityRecordDao activityRecordDao;
    SensorRecordDao sensorRecordDao;

    ActivityRecordEntity activityRecord;

    ActivityPrediction activityPrediction;
    SlidingWindow slidingWindow;
    FeatureExtraction featureExtraction;
    FeatureFilter featureFilter;

    Vibrator vibrator;

    List<Float> featureValues;

    boolean recording;

    @BindView(R.id.button_activity)
    FloatingActionButton activityButton;

    @OnClick(R.id.button_activity) void toggleRecording() {
        if (isRecording())
            stopRecording();
        else
            startRecording();
    }

    @BindView(R.id.text_activity)
    TextView activityText;

    public ActivityRecognitionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_recognition, container, false);
        ButterKnife.bind(this, view);

        serviceIntent = new Intent(getContext(), SensorRecordService.class);
        serviceIntent.putExtra(Utils.TEST, true);

        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        AppDatabase appDatabase = AppDatabase.getInstance(getContext());
        activityRecordDao = appDatabase.activityRecordDao();
        sensorRecordDao = appDatabase.sensorRecordDao();

        activityPrediction = new ActivityPrediction(getContext());
        featureExtraction = new FeatureExtraction();
        featureFilter = new FeatureFilter(false);

        slidingWindow = new SlidingWindow(sensorRecordDao, new OnWindowListener() {
            @Override
            public void onWindowStart() {
                featureFilter.init();
            }

            @Override
            public void onWindowSegment(List<Float> segment) {
                featureExtraction.setFeatures(featureFilter.getFeatureArray());
                featureValues.addAll(featureExtraction.getFeatureValues(segment));
            }

            @Override
            public void onWindowFinish() {
                activityPrediction.addInstance(featureValues);
                featureValues.clear();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopRecording();
    }

    public void startRecording() {
        if (!isRecording()) {
            getContext().startService(serviceIntent);
            activityButton.setShowProgress(true);
            activityText.setText(R.string.perform_activity);

            acquireWakeLock();
            recording = true;
        }
    }

    public void stopRecording() {
        if (isRecording()) {
            getContext().stopService(serviceIntent);
            activityButton.setShowProgress(false);
            activityText.setText(R.string.click_to_recognize);

            predict();

            releaseWakeLock();
            recording = false;
        }
    }

    @Override
    public void onEnterAmbient() {

    }

    @Override
    public void onExitAmbient() {

    }

    private void predict() {
        activityText.setText(R.string.predicting);
        activityPrediction.clear();

        new Thread(() -> {
            featureValues = new ArrayList<>();
            activityRecord = activityRecordDao.getLastRecordForTest();

            slidingWindow.processRecord(activityRecord);

            // Update activity record.
            activityText.setText(
                    getString(Activity.values()[activityPrediction.getPrediction()].title_res)
            );
            Log.d(DEBUG_TAG, activityText.getText().toString());
            vibrate();
        }).start();
    }

    public void vibrate() {
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(200);
        }
    }

    public boolean isRecording() {
        return recording;
    }
}

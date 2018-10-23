package com.adnagu.activityrecognition.ui.section;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.common.AmbientMode;
import com.adnagu.activityrecognition.common.BaseFragment;
import com.adnagu.activityrecognition.database.AppDatabase;
import com.adnagu.activityrecognition.database.dao.SensorRecordDao;
import com.adnagu.activityrecognition.database.dao.SensorValueDao;
import com.adnagu.activityrecognition.database.entity.SensorRecordEntity;
import com.adnagu.activityrecognition.database.entity.SensorValueEntity;
import com.adnagu.activityrecognition.model.Activity;
import com.adnagu.activityrecognition.service.SensorRecordService;
import com.adnagu.activityrecognition.ui.ListActivity;
import com.adnagu.activityrecognition.utils.Utils;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ticwear.design.drawable.CircularProgressDrawable;
import ticwear.design.widget.FloatingActionButton;

import static android.content.Context.SENSOR_SERVICE;

/**
 * SensorRecordFragment
 *
 * @author ramazan.vapurcu
 * Created on 10/3/2018
 */
public class SensorRecordFragment extends BaseFragment implements AmbientMode, SensorEventListener {

    private final String DEBUG_TAG = getClass().getName();
    private final Activity DEFAULT_ACTIVITY = Activity.UsingComputer;

    Intent serviceIntent;
    AppDatabase appDatabase;
    SensorManager sensorManager;
    Sensor sensor;

    int selectedActivityIndex;
    boolean isRecording;

    @BindView(R.id.button_record)
    FloatingActionButton recordButton;

    @BindView(R.id.activity_name)
    TextView activityName;

    @BindView(R.id.record_text)
    TextView recordText;

    @OnClick(R.id.activity_name) void chooseActivity() {
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

        selectedActivityIndex = DEFAULT_ACTIVITY.ordinal();
        activityName.setText(getString(DEFAULT_ACTIVITY.title_res));

        serviceIntent = new Intent(getActivity(), SensorRecordService.class);

        appDatabase = AppDatabase.getInstance(getContext());

        sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);

        if (null != sensorManager)
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ALL);

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
        isRecording = true;

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        //serviceIntent.putExtra(Utils.ACTIVITY_INDEX, selectedActivityIndex);
        //getActivity().startService(serviceIntent);

        recordButton.setShowProgress(true);
        recordText.setText(R.string.recording);
    }

    public void stopRecording() {
        isRecording = false;

        sensorManager.unregisterListener(this);
        //getActivity().stopService(serviceIntent);
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
        selectedActivityIndex = index;
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        new SensorRecordTask(getContext(), selectedActivityIndex).execute(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    static class SensorRecordTask extends AsyncTask<SensorEvent, Void, Void> {

        private AppDatabase appDatabase;
        private int activityType;

        private WeakReference<Context> contextReference;
        private SensorRecordDao sensorRecordDao;
        private SensorValueDao sensorValueDao;

        SensorRecordTask(Context context, int activityType) {
            Log.d("SensorRecordTask", "constructor");

            this.contextReference = new WeakReference<>(context);
            this.activityType = activityType;

            appDatabase = AppDatabase.getInstance(contextReference.get());
            sensorRecordDao = appDatabase.sensorRecordDao();
            sensorValueDao = appDatabase.sensorValueDao();
        }

        @Override
        protected Void doInBackground(SensorEvent... sensorEvents) {
            Log.d("SensorRecordTask", "doInBackground");
            SensorEvent sensorEvent = sensorEvents[0];
            SensorRecordEntity sensorRecord = new SensorRecordEntity(
                    sensorEvent.timestamp,
                    sensorEvent.sensor.getType(),
                    activityType
            );

            long recordId = sensorRecordDao.insert(sensorRecord)[0];

            for (float value : sensorEvent.values) {
                SensorValueEntity sensorValue = new SensorValueEntity(
                        value,
                        (int) recordId
                );
                sensorValueDao.insert(sensorValue);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
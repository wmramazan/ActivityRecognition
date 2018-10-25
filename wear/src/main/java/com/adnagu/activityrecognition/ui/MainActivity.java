package com.adnagu.activityrecognition.ui;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wear.ambient.AmbientModeSupport;
import android.support.wear.widget.drawer.WearableActionDrawerView;
import android.support.wear.widget.drawer.WearableNavigationDrawerView;
import android.support.wearable.activity.ConfirmationActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.adapter.NavigationAdapter;
import com.adnagu.activityrecognition.database.AppDatabase;
import com.adnagu.activityrecognition.database.dao.SensorDao;
import com.adnagu.activityrecognition.database.entity.SensorEntity;
import com.adnagu.activityrecognition.model.Section;
import com.adnagu.activityrecognition.common.BaseActivity;
import com.adnagu.activityrecognition.ui.section.ActivityRecognitionFragment;
import com.adnagu.activityrecognition.ui.section.SensorRecordFragment;
import com.adnagu.activityrecognition.ui.section.StatisticFragment;
import com.adnagu.activityrecognition.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ticwear.design.app.AlertDialog;
import ticwear.design.utils.WindowUtils;

public class MainActivity extends BaseActivity implements
        AmbientModeSupport.AmbientCallbackProvider,
        MenuItem.OnMenuItemClickListener,
        WearableNavigationDrawerView.OnItemSelectedListener {

    private final String DEBUG_TAG = getClass().getName();

    private NavigationAdapter navigationAdapter;
    private FragmentManager fragmentManager;

    private ActivityRecognitionFragment activityRecognitionFragment;
    private SensorRecordFragment sensorRecordFragment;
    private StatisticFragment statisticFragment;

    @BindView(R.id.top_navigation_drawer)
    WearableNavigationDrawerView navigationDrawerView;

    @BindView(R.id.bottom_action_drawer)
    WearableActionDrawerView actionDrawerView;

    @OnClick(R.id.content_frame) void hideActionDrawer() {
        actionDrawerView.getController().closeDrawer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Enables Always-on
        setAmbientEnabled();

        fragmentManager = getFragmentManager();
        navigationAdapter = new NavigationAdapter(this, Section.values());

        navigationDrawerView = findViewById(R.id.top_navigation_drawer);
        navigationDrawerView.setAdapter(navigationAdapter);
        navigationDrawerView.addOnItemSelectedListener(this);
        navigationDrawerView.getController().peekDrawer();

        actionDrawerView.setPeekOnScrollDownEnabled(true);
        actionDrawerView.setOnMenuItemClickListener(this);

        activityRecognitionFragment = new ActivityRecognitionFragment();
        replaceFragment(activityRecognitionFragment);

        saveSensors();

        navigationDrawerView.setCurrentItem(Section.SensorRecord.ordinal(), true);
    }

    @Override
    protected void onDestroy() {
        if (null != sensorRecordFragment)
            sensorRecordFragment.stopRecording();
        super.onDestroy();
    }

    protected void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    protected void saveSensors() {
        AppDatabase appDatabase = AppDatabase.getInstance(this);
        SensorDao sensorDao = appDatabase.sensorDao();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Log.d(DEBUG_TAG, "Number of sensors: " + sensorDao.getCount());

        if (null != sensorManager && !sensorDao.hasAny()) {
            List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

            for (Sensor deviceSensor : deviceSensors) {
                Log.d(DEBUG_TAG, deviceSensor.getName());

                SensorEntity sensor = new SensorEntity(
                        deviceSensor.getType(),
                        deviceSensor.getName(),
                        deviceSensor.getVendor(),
                        deviceSensor.getMinDelay(),
                        deviceSensor.getMaxDelay(),
                        deviceSensor.getMaximumRange(),
                        deviceSensor.getResolution(),
                        deviceSensor.getPower()
                );

                sensorDao.insert(sensor);
            }
        }
    }

    @Override
    public AmbientModeSupport.AmbientCallback getAmbientCallback() {
        return null;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_reset:
                showDialog(new AlertDialog.Builder(this)
                        .setTitle(R.string.reset_database)
                        .setMessage(R.string.reset_database_warning)
                        .setPositiveButtonIcon(ticwear.design.R.drawable.tic_ic_btn_ok, (dialogInterface, which) -> {
                            dialogInterface.dismiss();

                            if (deleteDatabase(Utils.DATABASE_NAME)) {
                                navigationDrawerView.setCurrentItem(Section.Statistic.ordinal(), true);
                                Utils.showMessage(ConfirmationActivity.SUCCESS_ANIMATION, MainActivity.this, R.string.reset_database_success);
                            } else
                                Utils.showMessage(ConfirmationActivity.FAILURE_ANIMATION, MainActivity.this, R.string.reset_database_error);
                        })
                        .setNegativeButtonIcon(ticwear.design.R.drawable.tic_ic_btn_cancel, (dialogInterface, which) -> dialogInterface.dismiss())
                        .setDelayConfirmAction(DialogInterface.BUTTON_NEGATIVE, 5000)
                        .create()
                );
                break;
        }
        return false;
    }

    public void showDialog(Dialog dialog) {
        WindowUtils.clipToScreenShape(dialog.getWindow());
        dialog.show();
    }

    @Override
    public void onItemSelected(int i) {
        switch (Section.values()[i]) {
            case ActivityRecognition:
                replaceFragment(activityRecognitionFragment);
                break;
            case SensorRecord:
                if (null == sensorRecordFragment)
                    sensorRecordFragment = new SensorRecordFragment();
                replaceFragment(sensorRecordFragment);
                break;
            case Statistic:
                if (null == statisticFragment)
                    statisticFragment = new StatisticFragment();
                replaceFragment(statisticFragment);
                break;
        }

        hideActionDrawer();
    }
}

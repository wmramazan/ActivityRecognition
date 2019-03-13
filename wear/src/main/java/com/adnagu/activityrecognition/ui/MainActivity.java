package com.adnagu.activityrecognition.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.ConfirmationActivity;
import android.view.MenuItem;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.adapter.NavigationAdapter;
import com.adnagu.activityrecognition.common.BaseActivity;
import com.adnagu.activityrecognition.database.AppDatabase;
import com.adnagu.activityrecognition.database.dao.ActivityRecordDao;
import com.adnagu.activityrecognition.database.dao.SensorRecordDao;
import com.adnagu.activityrecognition.ml.ArffFile;
import com.adnagu.activityrecognition.model.Section;
import com.adnagu.activityrecognition.ui.section.ActivityRecognitionFragment;
import com.adnagu.activityrecognition.ui.section.SensorRecordFragment;
import com.adnagu.activityrecognition.ui.section.StatisticFragment;
import com.adnagu.activityrecognition.utils.DatabaseUtils;
import com.adnagu.activityrecognition.utils.Utils;

import androidx.wear.widget.drawer.WearableActionDrawerView;
import androidx.wear.widget.drawer.WearableNavigationDrawerView;
import butterknife.BindView;
import butterknife.OnClick;
import ticwear.design.app.AlertDialog;
import ticwear.design.utils.WindowUtils;

public class MainActivity extends BaseActivity implements
        MenuItem.OnMenuItemClickListener,
        WearableNavigationDrawerView.OnItemSelectedListener {

    private final String DEBUG_TAG = getClass().getName();

    NavigationAdapter navigationAdapter;

    ActivityRecognitionFragment activityRecognitionFragment;
    SensorRecordFragment sensorRecordFragment;
    StatisticFragment statisticFragment;

    AppDatabase appDatabase;
    ActivityRecordDao activityRecordDao;
    SensorRecordDao sensorRecordDao;

    Handler handler;

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

        navigationDrawerView.setCurrentItem(Section.SensorRecord.ordinal(), true);

        appDatabase = AppDatabase.getInstance(this);
        activityRecordDao = appDatabase.activityRecordDao();
        sensorRecordDao = appDatabase.sensorRecordDao();

        DatabaseUtils.prepareDatabase(this);
        handler = new Handler();
    }

    @Override
    protected void onDestroy() {
        if (null != sensorRecordFragment)
            sensorRecordFragment.stopRecording();
        super.onDestroy();
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

                            int numberOfRecords = sensorRecordDao.deleteAll();
                            if (numberOfRecords > 0) {
                                navigationDrawerView.setCurrentItem(Section.Statistic.ordinal(), true);
                                Utils.showMessage(
                                        ConfirmationActivity.SUCCESS_ANIMATION,
                                        MainActivity.this,
                                        String.format(
                                                getString(R.string.reset_database_success),
                                                String.valueOf(numberOfRecords)
                                        )
                                );
                            } else
                                Utils.showMessage(
                                        ConfirmationActivity.FAILURE_ANIMATION,
                                        MainActivity.this,
                                        getString(R.string.reset_database_error)
                                );
                        })
                        .setNegativeButtonIcon(ticwear.design.R.drawable.tic_ic_btn_cancel, (dialogInterface, which) -> dialogInterface.dismiss())
                        .setDelayConfirmAction(DialogInterface.BUTTON_NEGATIVE, 5000)
                        .create()
                );
                break;
            case R.id.menu_save_arff:
                hideActionDrawer();
                showProgress();
                new Thread(() -> {
                    ArffFile arffFile = new ArffFile(this, progress -> handler.post(() -> setProgressBar(progress)));
                    arffFile.save(4, 50);
                    Utils.showMessage(
                            ConfirmationActivity.SUCCESS_ANIMATION,
                            MainActivity.this,
                            getString(R.string.save_as_arff_success)
                    );
                    handler.post(this::hideProgress);
                }).start();
                break;
            case R.id.menu_delete_last_record:
                showDialog(new AlertDialog.Builder(this)
                        .setTitle(R.string.delete_last_record)
                        .setMessage(R.string.delete_last_record_warning)
                        .setPositiveButtonIcon(ticwear.design.R.drawable.tic_ic_btn_ok, (dialogInterface, which) -> {
                            dialogInterface.dismiss();

                            int numberOfRecords = activityRecordDao.deleteLastRecord();
                            if (numberOfRecords > 0) {
                                navigationDrawerView.setCurrentItem(Section.Statistic.ordinal(), true);
                                Utils.showMessage(
                                        ConfirmationActivity.SUCCESS_ANIMATION,
                                        MainActivity.this,
                                        getString(R.string.delete_last_record_success)
                                );
                            } else
                                Utils.showMessage(
                                        ConfirmationActivity.FAILURE_ANIMATION,
                                        MainActivity.this,
                                        getString(R.string.delete_last_record_error)
                                );
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

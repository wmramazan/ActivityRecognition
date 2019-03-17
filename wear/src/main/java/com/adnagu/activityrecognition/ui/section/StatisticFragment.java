package com.adnagu.activityrecognition.ui.section;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.common.BaseFragment;
import com.adnagu.common.database.AppDatabase;
import com.adnagu.common.database.dao.ActivityRecordDao;
import com.adnagu.common.database.dao.SensorRecordDao;
import com.adnagu.common.utils.DatabaseUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * StatisticFragment
 *
 * @author ramazan.vapurcu
 * Created on 10/3/2018
 */
public class StatisticFragment extends BaseFragment {

    private final String DEBUG_TAG = getClass().getName();

    AppDatabase appDatabase;
    ActivityRecordDao activityRecordDao;
    SensorRecordDao sensorRecordDao;

    @BindView(R.id.text_activity_records)
    TextView tvActivityRecords;

    @BindView(R.id.text_sensor_records)
    TextView tvSensorRecords;

    @BindView(R.id.text_database_size)
    TextView tvDatabaseSize;

    @OnClick(R.id.text_sensor_records) public void setDatabaseInformation() {
        tvActivityRecords.setText(String.valueOf(activityRecordDao.getCount()));
        tvSensorRecords.setText(String.valueOf(sensorRecordDao.getCount()));

        File file = getContext().getDatabasePath(DatabaseUtils.DATABASE_NAME);
        tvDatabaseSize.setText(
                String.format(
                        getString(R.string.database_size_value),
                        String.valueOf((int) Math.floor(file.length() >> 10))
                )
        );
    }

    public StatisticFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        ButterKnife.bind(this, view);

        appDatabase = AppDatabase.getInstance(getContext());
        activityRecordDao = appDatabase.activityRecordDao();
        sensorRecordDao = appDatabase.sensorRecordDao();

        setDatabaseInformation();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        setDatabaseInformation();
    }

    @Override
    public void onEnterAmbient() {

    }

    @Override
    public void onExitAmbient() {

    }
}
package com.adnagu.activityrecognition.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.wear.ambient.AmbientModeSupport;
import android.support.wear.widget.drawer.WearableActionDrawerView;
import android.support.wear.widget.drawer.WearableNavigationDrawerView;
import android.view.MenuItem;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.adapter.NavigationAdapter;
import com.adnagu.activityrecognition.model.Section;
import com.adnagu.activityrecognition.common.BaseActivity;
import com.adnagu.activityrecognition.ui.section.ActivityRecognitionFragment;
import com.adnagu.activityrecognition.ui.section.SensorRecordFragment;
import com.adnagu.activityrecognition.ui.section.StatisticFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements
        AmbientModeSupport.AmbientCallbackProvider,
        MenuItem.OnMenuItemClickListener,
        WearableNavigationDrawerView.OnItemSelectedListener {

    private final String DEBUG_TAG = getClass().getName();

    private static final Section DEFAULT_SECTION = Section.ActivityRecognition;

    private NavigationAdapter navigationAdapter;
    private FragmentManager fragmentManager;

    private ActivityRecognitionFragment activityRecognitionFragment;
    private SensorRecordFragment sensorRecordFragment;
    private StatisticFragment statisticFragment;

    @BindView(R.id.top_navigation_drawer)
    WearableNavigationDrawerView navigationDrawerView;

    @BindView(R.id.bottom_action_drawer)
    WearableActionDrawerView actionDrawerView;

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

        //activityRecognitionFragment = new ActivityRecognitionFragment();
        //replaceFragment(activityRecognitionFragment);
        SensorRecordFragment sensorRecordFragment = new SensorRecordFragment();
        replaceFragment(sensorRecordFragment);

        startActivity(new Intent(this, ListActivity.class));
    }

    protected void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public AmbientModeSupport.AmbientCallback getAmbientCallback() {
        return null;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
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
    }
}

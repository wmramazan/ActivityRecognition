package com.adnagu.activityrecognition.ui.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.wear.ambient.AmbientModeSupport;
import android.support.wear.widget.drawer.WearableActionDrawerView;
import android.support.wear.widget.drawer.WearableNavigationDrawerView;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.drawer.WearableNavigationDrawer;
import android.view.MenuItem;
import android.widget.TextView;

import com.adnagu.activityrecognition.R;

public class MainActivity extends WearableActivity implements
        AmbientModeSupport.AmbientCallbackProvider,
        MenuItem.OnMenuItemClickListener,
        WearableNavigationDrawerView.OnItemSelectedListener {

    public enum Section {
        ActivityRecognition(R.string.section_activity_recognition, R.drawable.ic_activity_recognition_black_24dp),
        SensorRecord(R.string.section_sensor_record, R.drawable.ic_record_black_24dp),
        Statistic(R.string.section_statistic, R.drawable.ic_statistic_black_24dp);

        final int titleRes;
        final int drawableRes;

        Section(final int titleRes, final int drawableRes) {
            this.titleRes = titleRes;
            this.drawableRes = drawableRes;
        }
    }

    private static final Section DEFAULT_SECTION = Section.ActivityRecognition;

    private WearableNavigationDrawerView navigationDrawerView;
    private WearableActionDrawerView actionDrawerView;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enables Always-on
        setAmbientEnabled();

        fragmentManager = getFragmentManager();

        navigationDrawerView = findViewById(R.id.top_navigation_drawer);
        //navigationDrawerView.setAdapter(new NavigationAdap);
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

    }
}

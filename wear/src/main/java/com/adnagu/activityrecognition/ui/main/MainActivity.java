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
import com.adnagu.activityrecognition.adapter.NavigationAdapter;
import com.adnagu.activityrecognition.model.Section;

public class MainActivity extends WearableActivity implements
        AmbientModeSupport.AmbientCallbackProvider,
        MenuItem.OnMenuItemClickListener,
        WearableNavigationDrawerView.OnItemSelectedListener {

    private static final Section DEFAULT_SECTION = Section.ActivityRecognition;

    private NavigationAdapter navigationAdapter;
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
        navigationAdapter = new NavigationAdapter(this, Section.values());

        navigationDrawerView = findViewById(R.id.top_navigation_drawer);
        navigationDrawerView.setAdapter(navigationAdapter);
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

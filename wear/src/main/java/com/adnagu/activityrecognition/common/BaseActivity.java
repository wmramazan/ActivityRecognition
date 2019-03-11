package com.adnagu.activityrecognition.common;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;

import com.adnagu.activityrecognition.R;

import androidx.core.content.ContextCompat;
import androidx.wear.widget.drawer.WearableDrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * BaseActivity
 *
 * @author ramazan.vapurcu
 * Created on 10/10/2018
 */
public abstract class BaseActivity extends WearableActivity {

    protected FragmentManager fragmentManager;
    protected BaseFragment fragment;

    @BindView(R.id.drawer_layout)
    WearableDrawerLayout drawerLayout;

    @BindView(R.id.llProgress)
    View progress;

    @BindView(R.id.content_frame)
    View contentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);

        fragmentManager = getFragmentManager();
    }

    public void showProgress() {
        Log.d("BaseActivity", "showProgress");
        progress.setVisibility(View.VISIBLE);
        contentFrame.setVisibility(View.GONE);
    }

    public void hideProgress() {
        Log.d("BaseActivity", "hideProgress");
        progress.setVisibility(View.GONE);
        contentFrame.setVisibility(View.VISIBLE);
    }

    public void replaceFragment(BaseFragment fragment) {
        this.fragment = fragment;
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        drawerLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black));

        fragment.onEnterAmbient();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        drawerLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.wear_primary_darken));

        fragment.onExitAmbient();
    }
}

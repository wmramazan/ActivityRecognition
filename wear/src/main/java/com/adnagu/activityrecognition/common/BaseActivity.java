package com.adnagu.activityrecognition.common;

import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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
public abstract class BaseActivity extends WearableActivity implements BaseView {
    
    protected String DEBUG_TAG = getClass().getName();
    protected long   WAKELOCK_TIMEOUT = 30 * 60 * 1000L;

    protected FragmentManager fragmentManager;
    protected BaseFragment fragment;

    protected PowerManager.WakeLock wakeLock;

    @BindView(R.id.drawer_layout)
    WearableDrawerLayout drawerLayout;

    @BindView(R.id.llProgress)
    View progress;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.progress_circle)
    ProgressBar progressCircle;

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

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ActivityRecognition::WakeLock");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        releaseWakeLock();
    }

    public void showProgress() {
        Log.d(DEBUG_TAG, "showProgress");
        progress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        progressCircle.setVisibility(View.VISIBLE);
        contentFrame.setVisibility(View.GONE);
    }

    public void setProgressBar(int progress) {
        Log.d(DEBUG_TAG, "Progress: " + progress);
        progressBar.setVisibility(View.VISIBLE);
        progressCircle.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress(progress, true);
        } else {
            progressBar.setProgress(progress);
        }
    }

    public void hideProgress() {
        Log.d(DEBUG_TAG, "hideProgress");
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

    @Override
    public void acquireWakeLock() {
        Log.d(DEBUG_TAG, "acquireWakeLock");
        wakeLock.acquire(WAKELOCK_TIMEOUT);
    }

    @Override
    public void releaseWakeLock() {
        Log.d(DEBUG_TAG, "releaseWakeLock");
        if (wakeLock.isHeld())
            wakeLock.release();
    }
}

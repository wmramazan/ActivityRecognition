package com.adnagu.activityrecognition.common;

import android.app.Fragment;
import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * BaseFragment
 *
 * @author ramazan.vapurcu
 * Created on 10/10/2018
 */
public abstract class BaseFragment extends Fragment implements BaseView, AmbientMode {

    BaseActivity baseActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseActivity = (BaseActivity) getActivity();
    }

    @Override
    public void acquireWakeLock() {
        baseActivity.acquireWakeLock();
    }

    @Override
    public void releaseWakeLock() {
        baseActivity.releaseWakeLock();
    }

    @Override
    public void vibrate() {
        baseActivity.vibrate();
    }
}

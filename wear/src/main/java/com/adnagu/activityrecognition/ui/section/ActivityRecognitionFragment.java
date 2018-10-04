package com.adnagu.activityrecognition.ui.section;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adnagu.activityrecognition.R;

/**
 * ActivityRecognitionFragment
 *
 * @author ramazan.vapurcu
 * Created on 10/3/2018
 */
public class ActivityRecognitionFragment extends Fragment implements AmbientMode {

    private final String DEBUG_TAG = getClass().getName();

    public ActivityRecognitionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity_recognition, container, false);
    }

    @Override
    public void onEnterAmbient() {

    }

    @Override
    public void onExitAmbient() {

    }
}

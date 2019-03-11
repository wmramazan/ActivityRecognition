package com.adnagu.activityrecognition.ui.section;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.common.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ticwear.design.widget.FloatingActionButton;

/**
 * ActivityRecognitionFragment
 *
 * @author ramazan.vapurcu
 * Created on 10/3/2018
 */
public class ActivityRecognitionFragment extends BaseFragment {

    private final String DEBUG_TAG = getClass().getName();

    @BindView(R.id.button_activity)
    FloatingActionButton activityButton;

    @OnClick(R.id.button_activity) void startRecognition() {
        activityButton.setShowProgress(true);
    }

    @BindView(R.id.text_activity)
    TextView activityText;

    public ActivityRecognitionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_recognition, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onEnterAmbient() {

    }

    @Override
    public void onExitAmbient() {

    }
}

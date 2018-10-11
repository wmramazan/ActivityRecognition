package com.adnagu.activityrecognition.ui.section;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.adapter.ActivityAdapter;
import com.adnagu.activityrecognition.model.Activity;
import com.adnagu.activityrecognition.ui.common.BaseFragment;

/**
 * SensorRecordFragment
 *
 * @author ramazan.vapurcu
 * Created on 10/3/2018
 */
public class SensorRecordFragment extends BaseFragment implements AmbientMode {

    private final String DEBUG_TAG = getClass().getName();

    protected RecyclerView activityList;
    protected ActivityAdapter activityAdapter;

    public SensorRecordFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor_record, container, false);

        //activityList = view.findViewById(R.id.activityList);
        //activityAdapter = new ActivityAdapter(getContext(), Activity.values());

        //activityList.setEdgeItemsCenteringEnabled(true);

        //activityList.setLayoutManager(new LinearLayoutManager(getContext()));
        //activityList.setAdapter(activityAdapter);

        return view;
    }

    @Override
    public void onEnterAmbient() {

    }

    @Override
    public void onExitAmbient() {

    }
}
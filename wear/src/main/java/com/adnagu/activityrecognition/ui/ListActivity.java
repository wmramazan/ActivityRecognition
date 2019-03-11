package com.adnagu.activityrecognition.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.adapter.ActivityAdapter;
import com.adnagu.activityrecognition.model.Activity;
import com.adnagu.activityrecognition.utils.RecyclerItemClickListener;
import com.adnagu.activityrecognition.utils.Utils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListActivity extends WearableActivity {

    ActivityAdapter activityAdapter;

    @BindView(R.id.list)
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ButterKnife.bind(this);

        list.setLayoutManager(new LinearLayoutManager(this));

        activityAdapter = new ActivityAdapter(this, Activity.values());
        list.setAdapter(activityAdapter);

        list.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
        list.addOnItemTouchListener(new RecyclerItemClickListener(this, list, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra(Utils.ACTIVITY_ID, position);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }
}

package com.adnagu.activityrecognition.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.utils.Utils;
import com.adnagu.common.model.Activity;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import ticwear.design.widget.PrimaryButton;

public class CircularPredictionsActivity extends WearableActivity {

    private final int MAX_ACTIVITY = 4;

    @BindView(R.id.deco_view)
    DecoView decoView;

    @BindView(R.id.grid_layout)
    GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_predictions);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        int[] activityIds = intent.getIntArrayExtra(Utils.ITEM_ACTIVITY_IDS);
        float[] percentages = intent.getFloatArrayExtra(Utils.ITEM_PERCENTAGES);
        int[] colorIds = new int[] {
                R.color.first_activity,
                R.color.second_activity,
                R.color.third_activity,
                R.color.fourth_activity
        };

        int numberOfSeries = activityIds.length < MAX_ACTIVITY ? activityIds.length : MAX_ACTIVITY;

        int [] seriesIndices = new int[numberOfSeries];

        Activity[] activities = Activity.values();

        float point = 0f;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(4, 4, 4, 4);

        for (int i = 0; i < numberOfSeries; i++) {
            Activity activity = activities[activityIds[i]];

            PrimaryButton activityIcon = new PrimaryButton(this);
            activityIcon.setLayoutParams(params);
            activityIcon.setPadding(12, 12, 12, 12);
            activityIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
            activityIcon.setBackgroundColor(ContextCompat.getColor(this, colorIds[i]));
            activityIcon.setImageResource(activity.drawable_res);
            gridLayout.addView(activityIcon);

            seriesIndices[i] = decoView.addSeries(
                    new SeriesItem.Builder(ContextCompat.getColor(this, colorIds[i]))
                            .setRange(0, 100, 0)
                            .setLineWidth(22f)
                            .setSpinDuration(3000)
                            .setInset(new PointF(point, point))
                            .build()
            );

            point += 22f;
        }

        for (int i = 0; i < numberOfSeries; i++) {
            decoView.addEvent(new DecoEvent.Builder(percentages[activities[activityIds[i]].ordinal()])
                    .setIndex(seriesIndices[i])
                    .setDelay(0)
                    .build());
        }
    }
}

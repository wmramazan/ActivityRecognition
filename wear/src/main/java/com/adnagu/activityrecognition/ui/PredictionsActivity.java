package com.adnagu.activityrecognition.ui;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.GridLayout;
import android.widget.TextView;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.ui.component.ActivityIcon;
import com.adnagu.common.database.AppDatabase;
import com.adnagu.common.database.dao.PredictionDao;
import com.adnagu.common.database.dao.PredictionRecordDao;
import com.adnagu.common.model.Activity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * PredictionsActivity
 *
 * @author ramazan.vapurcu
 * Created on 5/9/2019
 */
public class PredictionsActivity extends WearableActivity {

    @BindView(R.id.text_prediction_day)
    TextView predictionDay;

    @BindView(R.id.grid_layout)
    GridLayout gridLayout;

    @OnClick(R.id.image_back)
    public void goToPreviousDay() {

    }

    @OnClick(R.id.image_forward)
    public void goToNextDay() {

    }

    PredictionDao predictionDao;
    PredictionRecordDao predictionRecordDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictions);

        ButterKnife.bind(this);

        AppDatabase appDatabase = AppDatabase.getInstance(this);
        predictionDao = appDatabase.predictionDao();
        predictionRecordDao = appDatabase.predictionRecordDao();

        Activity[] activities = Activity.values();
        for (Activity activity : activities) {
            ActivityIcon icon = new ActivityIcon(this, activity.drawable_res);
            icon.setText("33%");
            gridLayout.addView(icon);
        }
    }
}

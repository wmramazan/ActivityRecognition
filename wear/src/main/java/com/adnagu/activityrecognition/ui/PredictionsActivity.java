package com.adnagu.activityrecognition.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.wear.activity.ConfirmationActivity;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.ui.component.ActivityIcon;
import com.adnagu.activityrecognition.utils.Utils;
import com.adnagu.common.database.AppDatabase;
import com.adnagu.common.database.dao.PredictionDao;
import com.adnagu.common.database.dao.PredictionRecordDao;
import com.adnagu.common.model.Activity;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ticwear.design.app.AlertDialog;
import ticwear.design.utils.WindowUtils;

/**
 * PredictionsActivity
 *
 * @author ramazan.vapurcu
 * Created on 5/9/2019
 */
public class PredictionsActivity extends WearableActivity {

    PredictionDao predictionDao;
    PredictionRecordDao predictionRecordDao;

    Date date;
    SimpleDateFormat formatter;

    @BindView(R.id.text_prediction_day)
    TextView predictionDay;

    @BindView(R.id.grid_layout)
    GridLayout gridLayout;

    @BindView(R.id.text_no_predictions)
    TextView noPredictions;

    @OnClick(R.id.image_back)
    public void goToPreviousDay() {
        date = DateUtils.addDays(date, -1);
        getPredictions();
    }

    @OnClick(R.id.image_forward)
    public void goToNextDay() {
        date = DateUtils.addDays(date, 1);
        getPredictions();
    }

    @OnClick(R.id.button_delete_all)
    public void deleteAll() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.clear_predictions)
                .setMessage(R.string.clear_predictions_warning)
                .setPositiveButtonIcon(ticwear.design.R.drawable.tic_ic_btn_ok, (dialogInterface, which) -> {
                    dialogInterface.dismiss();

                    int numberOfRecords = predictionDao.deleteAll();
                    if (numberOfRecords > 0) {
                        Utils.showMessage(
                                ConfirmationActivity.SUCCESS_ANIMATION,
                                this,
                                String.format(
                                        getString(R.string.clear_predictions_success),
                                        String.valueOf(numberOfRecords)
                                )
                        );
                    } else
                        Utils.showMessage(
                                ConfirmationActivity.FAILURE_ANIMATION,
                                this,
                                getString(R.string.clear_predictions_error)
                        );

                    finish();
                })
                .setNegativeButtonIcon(ticwear.design.R.drawable.tic_ic_btn_cancel, (dialogInterface, which) -> dialogInterface.dismiss())
                .setDelayConfirmAction(DialogInterface.BUTTON_NEGATIVE, 5000)
                .create();

        WindowUtils.clipToScreenShape(dialog.getWindow());
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictions);

        ButterKnife.bind(this);

        AppDatabase appDatabase = AppDatabase.getInstance(this);
        predictionDao = appDatabase.predictionDao();
        predictionRecordDao = appDatabase.predictionRecordDao();

        date = new Date();
        formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

        getPredictions();
    }

    public void getPredictions() {
        predictionDay.setText(formatter.format(date));
        int[] predictions = predictionRecordDao.getPredictions(date);

        int total = 0;
        for (int prediction : predictions)
            total += prediction;

        if (total == 0) {
            noPredictions.setVisibility(View.VISIBLE);
        } else {
            noPredictions.setVisibility(View.GONE);
            float value = (float) 100 / total;

            float[] percentages = new float[predictions.length];
            for (int i = 0; i < predictions.length; i++)
                percentages[i] = (value * predictions[i]);

            Activity[] activities = Activity.values();
            for (Activity activity : activities) {
                int index = activity.ordinal();

                if (percentages[index] != 0) {
                    ActivityIcon icon = new ActivityIcon(this, activity.drawable_res);
                    icon.setText(String.format(Locale.getDefault(), "%.2f", percentages[index]) + "%");
                    gridLayout.addView(icon);
                }
            }
        }
    }
}

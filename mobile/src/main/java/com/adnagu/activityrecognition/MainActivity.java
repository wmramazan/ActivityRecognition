package com.adnagu.activityrecognition;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adnagu.common.model.Activity;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class MainActivity extends AppCompatActivity implements DataClient.OnDataChangedListener {

    private final String TAG = getClass().getName();
    private final String DATE_FORMAT = "MM/dd/yyyy";

    SimpleDateFormat formatter;
    Date date;

    @BindView(R.id.view_progress)
    ProgressBar progress;

    @BindView(R.id.layout_error)
    View errorLayout;

    @BindView(R.id.view_chart)
    PieChartView pieChart;

    @BindView(R.id.image_arff)
    View arffView;

    @BindView(R.id.layout_date)
    View dateLayout;

    @BindView(R.id.text_date)
    TextView dateText;

    @OnClick(R.id.image_arff)
    public void goToArffActivity() {
        Intent intent = new Intent(this, ArffActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.fab_back)
    public void goToPreviousDay() {
        date = DateUtils.addDays(date, -1);
        getPredictions();
    }

    @OnClick(R.id.fab_forward)
    public void goToNextDay() {
        if (!DateUtils.isSameDay(date, new Date())) {
            date = DateUtils.addDays(date, 1);
            getPredictions();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        arffView.bringToFront();
        progress.bringToFront();

        date = new Date();
        formatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

        getPredictions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.getDataClient(this).removeListener(this);
    }

    public void initPieChart(List<SliceValue> values) {
        PieChartData data = new PieChartData(values);
        data.setHasLabels(true);
        data.setHasLabelsOnlyForSelected(false);
        data.setHasLabelsOutside(false);
        data.setHasCenterCircle(true);

        //data.setSlicesSpacing(24);
        data.setCenterText1("Predictions");
        data.setCenterText1FontSize(16);

        pieChart.setPieChartData(data);
    }

    public void sendMessageForPredictions(String message) {
        progress.setVisibility(View.VISIBLE);

        Task<List<Node>> wearableList = Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
        wearableList.addOnSuccessListener(nodes -> {
            for (Node node : nodes) {
                Task<Integer> sendMessageTask = Wearable.getMessageClient(MainActivity.this).sendMessage(node.getId(), "/predictions", message.getBytes());
                sendMessageTask.addOnSuccessListener(result -> Log.d(TAG, "Sent message."));
                sendMessageTask.addOnFailureListener(e -> {
                    e.printStackTrace();
                    showError();
                });
            }
        });
        wearableList.addOnFailureListener(e -> {
            e.printStackTrace();
            showError();
        });
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged");
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (Objects.requireNonNull(item.getUri().getPath()).compareTo("/predictions") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    setData(dataMap.getIntegerArrayList("predictions"));
                }
            }
        }
    }

    public void setData(List<Integer> predictions) {
        progress.setVisibility(View.GONE);

        List<SliceValue> values = new ArrayList<>();

        int total = 0;
        for (int prediction : predictions)
            total += prediction;

        if (total != 0) {
            float value = (float) 100 / total;

            float[] percentages = new float[predictions.size()];
            for (int i = 0; i < predictions.size(); i++)
                percentages[i] = (value * predictions.get(i));

            Activity[] activities = Activity.values();
            for (Activity activity : activities) {
                int index = activity.ordinal();

                if (percentages[index] != 0) {
                    SliceValue sliceValue = new SliceValue(percentages[index], ChartUtils.pickColor());
                    sliceValue.setLabel(getString(activity.title_res));
                    values.add(sliceValue);
                }
            }

            initPieChart(values);
        }
    }

    public void getPredictions() {
        pieChart.setPieChartData(null);

        String message = formatter.format(date);
        dateText.setText(message);
        sendMessageForPredictions(
                message
        );
    }

    public void showError() {
        errorLayout.setVisibility(View.VISIBLE);
        dateLayout.setVisibility(View.GONE);
        pieChart.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
    }
}

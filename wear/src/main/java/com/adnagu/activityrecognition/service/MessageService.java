package com.adnagu.activityrecognition.service;

import android.util.Log;

import com.adnagu.activityrecognition.utils.Utils;
import com.adnagu.common.database.AppDatabase;
import com.adnagu.common.database.dao.PredictionRecordDao;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MessageService extends WearableListenerService {

    private final String TAG = getClass().getName();

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals("/predictions")) {
            String message = new String(messageEvent.getData());

            DataClient dataClient = Wearable.getDataClient(this);
            PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/predictions");
            putDataMapReq.getDataMap().putLong("date", System.currentTimeMillis());

            AppDatabase appDatabase = AppDatabase.getInstance(this);
            PredictionRecordDao predictionRecordDao = appDatabase.predictionRecordDao();
            SimpleDateFormat formatter = new SimpleDateFormat(Utils.DATE_FORMAT, Locale.getDefault());
            //formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                Log.d(TAG, formatter.parse(message).toString());
                putDataMapReq.getDataMap().putIntegerArrayList(
                        "predictions",
                        (ArrayList<Integer>) predictionRecordDao.getPredictionsForService(
                                formatter.parse(message)
                        )
                );
            } catch (ParseException e) {
                e.printStackTrace();
            }
            PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
            putDataReq.setUrgent();
            Task<DataItem> putDataTask = dataClient.putDataItem(putDataReq);
            putDataTask.addOnSuccessListener(dataItem -> Log.d(TAG, "Sent predictions."));
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }

}

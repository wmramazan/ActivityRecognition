package com.adnagu.activityrecognition.utils;

import android.content.Context;
import android.content.Intent;
import android.support.wearable.activity.ConfirmationActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Utils
 *
 * @author ramazan.vapurcu
 * Created on 10/14/2018
 */
public class Utils {

    public static final String ACTIVITY_ID = "ACTIVITY_ID";
    public static final String TEST = "TEST";

    public static final String FILTER_ACTIVITY = "ACTIVITY";

    public static final String ITEM_ACTIVITY_IDS = "ACTIVITY_IDS";
    public static final String ITEM_PERCENTAGES = "PERCENTAGES";

    public class RequestCode {
        public static final int CHOOSE_ACTIVITY = 1;
    }

    public static void showMessage(int animationType, Context context, String message) {
        Intent intent = new Intent(context, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, animationType);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, message);
        context.startActivity(intent);
    }

    public static List<Float> toList(float[] array) {
        List<Float> list = new ArrayList<>();

        for (float value : array)
            list.add(value);

        return list;
    }

}

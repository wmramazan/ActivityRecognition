package com.adnagu.activityrecognition.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.wearable.activity.ConfirmationActivity;

import com.adnagu.activityrecognition.R;

/**
 * Utils
 *
 * @author ramazan.vapurcu
 * Created on 10/14/2018
 */
public class Utils {

    public static final String DATABASE_NAME = "ActivityRecognition.db";

    public static final String ACTIVITY_INDEX = "index";

    public class RequestCode {
        public static final int CHOOSE_ACTIVITY = 1;
    }

    public static void showMessage(int animationType, Context context, @StringRes int resId) {
        Intent intent = new Intent(context, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                animationType);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE,
                context.getString(resId));
        context.startActivity(intent);
    }

}

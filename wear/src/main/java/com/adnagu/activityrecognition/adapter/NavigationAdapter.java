package com.adnagu.activityrecognition.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.wear.widget.drawer.WearableNavigationDrawerView;

/**
 * NavigationAdapter
 *
 * @author ramazan.vapurcu
 * Created on 10/3/2018
 */
public class NavigationAdapter extends WearableNavigationDrawerView.WearableNavigationDrawerAdapter {

    private Context context;

    public NavigationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CharSequence getItemText(int i) {
        return null;
    }

    @Override
    public Drawable getItemDrawable(int i) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}

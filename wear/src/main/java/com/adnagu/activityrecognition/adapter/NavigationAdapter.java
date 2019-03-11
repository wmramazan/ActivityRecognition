package com.adnagu.activityrecognition.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.adnagu.activityrecognition.model.Section;

import androidx.wear.widget.drawer.WearableNavigationDrawerView;

/**
 * NavigationAdapter
 *
 * @author ramazan.vapurcu
 * Created on 10/3/2018
 */
public class NavigationAdapter extends WearableNavigationDrawerView.WearableNavigationDrawerAdapter {

    private Context context;
    private Section[] sections;

    public NavigationAdapter(Context context, Section[] sections) {
        this.context = context;
        this.sections = sections;
    }

    @Override
    public CharSequence getItemText(int i) {
        return context.getString(sections[i].title_res);
    }

    @Override
    public Drawable getItemDrawable(int i) {
        return context.getDrawable(sections[i].drawable_res);
    }

    @Override
    public int getCount() {
        return sections.length;
    }
}

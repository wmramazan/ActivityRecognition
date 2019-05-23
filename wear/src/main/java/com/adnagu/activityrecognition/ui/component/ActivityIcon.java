package com.adnagu.activityrecognition.ui.component;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.adnagu.activityrecognition.R;

public class ActivityIcon extends LinearLayout {

    ImageView icon;
    TextView text;

    int resourceId;

    public ActivityIcon(Context context) {
        super(context);

        init();
    }

    public ActivityIcon(Context context, int resourceId) {
        super(context);

        this.resourceId = resourceId;
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setBackground(ContextCompat.getDrawable(getContext(), R.drawable.view_activity));
        inflate(getContext(), R.layout.component_activity_prediction, this);

        icon = findViewById(R.id.activity_icon);
        if (resourceId != 0)
            icon.setImageResource(resourceId);

        text = findViewById(R.id.activity_text);
    }

    public void setIcon(int resourceId) {
        this.icon.setImageResource(resourceId);
    }

    public void setText(CharSequence text) {
        this.text.setText(text);
    }
}

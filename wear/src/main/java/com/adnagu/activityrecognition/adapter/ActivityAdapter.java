package com.adnagu.activityrecognition.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adnagu.activityrecognition.R;
import com.adnagu.activityrecognition.model.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private Context context;
    private Activity[] activities;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView name;

        ViewHolder(View view) {
            super(view);
            this.icon = view.findViewById(R.id.item_activity_icon);
            this.name = view.findViewById(R.id.item_activity_name);
        }
    }

    public ActivityAdapter(Context context, Activity[] activities) {
        this.context = context;
        this.activities = activities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Activity activity = activities[position];

        holder.icon.setImageResource(activity.drawable_res);
        holder.name.setText(context.getText(activity.title_res));
    }

    @Override
    public int getItemCount() {
        return activities.length;
    }
}
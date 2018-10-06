package com.adnagu.activityrecognition.model;

import com.adnagu.activityrecognition.R;

public enum Activity {
    UsingComputer("Using Computer", R.drawable.ic_activity),
    WashingDishes("Washing Dishes", R.drawable.ic_activity),
    DrivingCar("Driving Car", R.drawable.ic_activity),
    EatingMeal("Eating Meal", R.drawable.ic_activity),
    PlayingVideoGame("Playing VideoGame", R.drawable.ic_activity),
    HavingShower("Having Shower", R.drawable.ic_activity),
    PlayingGuitar("Playing Guitar", R.drawable.ic_activity),
    BrushingTeeth("Brushing Teeth", R.drawable.ic_activity);

    public final String name;
    public final int drawable_res;

    Activity(final String name, final int drawable_res) {
        this.name = name;
        this.drawable_res = drawable_res;
    }
}

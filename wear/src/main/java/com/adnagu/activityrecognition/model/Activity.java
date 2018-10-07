package com.adnagu.activityrecognition.model;

import com.adnagu.activityrecognition.R;

public enum Activity {
    UsingComputer(R.string.activity_using_computer, R.drawable.ic_activity),
    WashingDishes(R.string.activity_washing_dishes, R.drawable.ic_activity),
    DrivingCar(R.string.activity_driving_car, R.drawable.ic_activity),
    EatingMeal(R.string.activity_eating_meal, R.drawable.ic_activity),
    PlayingVideoGame(R.string.activity_playing_video_game, R.drawable.ic_activity),
    HavingShower(R.string.activity_having_shower, R.drawable.ic_activity),
    PlayingGuitar(R.string.activity_playing_guitar, R.drawable.ic_activity),
    BrushingTeeth(R.string.activity_brushing_teeth, R.drawable.ic_activity);

    public final int title_res;
    public final int drawable_res;

    Activity(final int title_res, final int drawable_res) {
        this.title_res = title_res;
        this.drawable_res = drawable_res;
    }
}

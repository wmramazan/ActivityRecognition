package com.adnagu.common.model;

public enum Activity {
    /*UsingComputer(R.string.activity_using_computer, R.drawable.ic_using_computer),
    WashingDishes(R.string.activity_washing_dishes, R.drawable.ic_washing_dishes),
    DrivingCar(R.string.activity_driving_car, R.drawable.ic_driving_car),
    EatingMeal(R.string.activity_eating_meal, R.drawable.ic_eating_meal),
    PlayingVideoGame(R.string.activity_playing_video_game, R.drawable.ic_playing_video_game),
    HavingShower(R.string.activity_having_shower, R.drawable.ic_having_shower),
    PlayingGuitar(R.string.activity_playing_guitar, R.drawable.ic_playing_guitar),
    BrushingTeeth(R.string.activity_brushing_teeth, R.drawable.ic_brushing_teeth);*/

    UsingComputer(0,0),
    WashingDishes(0,0),
    DrivingCar(0,0),
    EatingMeal(0,0),
    PlayingVideoGame(0,0),
    HavingShower(0,0),
    PlayingGuitar(0,0),
    BrushingTeeth(0,0);

    public final int title_res;
    public final int drawable_res;

    Activity(final int title_res, final int drawable_res) {
        this.title_res = title_res;
        this.drawable_res = drawable_res;
    }
}

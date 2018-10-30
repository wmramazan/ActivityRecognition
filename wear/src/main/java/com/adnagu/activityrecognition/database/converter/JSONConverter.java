package com.adnagu.activityrecognition.database.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * JSONConverter
 *
 * @author ramazan.vapurcu
 * Created on 10/30/2018
 */
public class JSONConverter {

    @TypeConverter
    public static ArrayList<Float> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Float>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Float> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static String fromArray(float[] list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

}

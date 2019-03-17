package com.adnagu.common.database.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;

/**
 * JSONConverter
 *
 * @author ramazan.vapurcu
 * Created on 10/30/2018
 */
public class JSONConverter {

    @TypeConverter
    public static List<Float> toList(String string) {
        Type listType = new TypeToken<List<Float>>() {}.getType();
        return new Gson().fromJson(string, listType);
    }

    @TypeConverter
    public static String toString(List<Float> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

}

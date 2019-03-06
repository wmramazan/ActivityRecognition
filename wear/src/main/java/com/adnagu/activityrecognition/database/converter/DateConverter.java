package com.adnagu.activityrecognition.database.converter;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * DateConverter
 *
 * @author ramazan.vapurcu
 * Created on 10/2/2018
 */
public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}

package com.adnagu.common.database.converter;

import java.util.Date;

import androidx.room.TypeConverter;

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

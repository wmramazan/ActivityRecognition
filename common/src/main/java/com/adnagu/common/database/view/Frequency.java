package com.adnagu.common.database.view;

import androidx.room.DatabaseView;

@DatabaseView("SELECT AVG(number_of_records) / number_of_sensors as frequency FROM (SELECT COUNT(id) AS number_of_records, COUNT(DISTINCT(sensor_id)) AS number_of_sensors, datetime(sensor_record.date / 1000, 'unixepoch') as date_string FROM sensor_record GROUP BY date_string)")
public class Frequency {
    public int frequency;
}

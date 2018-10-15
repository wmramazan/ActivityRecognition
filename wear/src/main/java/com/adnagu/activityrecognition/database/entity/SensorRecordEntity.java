package com.adnagu.activityrecognition.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * SensorRecordEntity
 *
 * @author ramazan.vapurcu
 * Created on 10/2/2018
 */
@Entity(tableName = "sensor_record", foreignKeys = @ForeignKey(entity = SensorEntity.class, parentColumns = "id", childColumns = "sensor_id", onDelete = CASCADE))
public class SensorRecordEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "value")
    private String value;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "sensor_id")
    private int sensorId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }
}

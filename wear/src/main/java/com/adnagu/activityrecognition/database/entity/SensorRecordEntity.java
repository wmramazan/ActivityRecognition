package com.adnagu.activityrecognition.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * SensorRecordEntity
 *
 * @author ramazan.vapurcu
 * Created on 10/2/2018
 */
@Entity(tableName = "sensor_record", foreignKeys = @ForeignKey(entity = SensorEntity.class, parentColumns = "id", childColumns = "sensor_id", onDelete = CASCADE), indices = @Index(value = {"sensor_id"}))
public class SensorRecordEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "sensor_id")
    private int sensorId;

    @ColumnInfo(name = "activity_id")
    private int activityId;

    @ColumnInfo(name = "values")
    private String values;

    public SensorRecordEntity(long timestamp, int sensorId, int activityId, String values) {
        this.timestamp = timestamp;
        this.sensorId = sensorId;
        this.activityId = activityId;
        this.values = values;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
}

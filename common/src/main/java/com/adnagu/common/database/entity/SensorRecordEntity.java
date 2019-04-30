package com.adnagu.common.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

/**
 * SensorRecordEntity
 *
 * @author ramazan.vapurcu
 * Created on 10/2/2018
 */
@Entity(tableName = "sensor_record", foreignKeys = {
        @ForeignKey(entity = ActivityRecordEntity.class, parentColumns = "id", childColumns = "activity_record_id", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = SensorEntity.class, parentColumns = "id", childColumns = "sensor_id", onDelete = ForeignKey.CASCADE)
}, indices = @Index(value = {"activity_record_id", "sensor_id"}))
public class SensorRecordEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "values")
    private List<Float> values;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "sensor_id")
    private int sensorId;

    @ColumnInfo(name = "activity_record_id")
    private int activityRecordId;

    @Ignore
    public SensorRecordEntity(int id, List<Float> values, Date date, long timestamp, int sensorId, int activityRecordId) {
        this.id = id;
        this.values = values;
        this.date = date;
        this.timestamp = timestamp;
        this.sensorId = sensorId;
        this.activityRecordId = activityRecordId;
    }

    public SensorRecordEntity(List<Float> values, Date date, long timestamp, int sensorId, int activityRecordId) {
        this.values = values;
        this.date = date;
        this.timestamp = timestamp;
        this.sensorId = sensorId;
        this.activityRecordId = activityRecordId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Float> getValues() {
        return values;
    }

    public void setValues(List<Float> values) {
        this.values = values;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public int getActivityRecordId() {
        return activityRecordId;
    }

    public void setActivityRecordId(int activityRecordId) {
        this.activityRecordId = activityRecordId;
    }

}

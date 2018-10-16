package com.adnagu.activityrecognition.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * SensorValueEntity
 *
 * @author ramazan.vapurcu
 * Created on 10/16/2018
 */
@Entity(tableName = "sensor_value", foreignKeys = @ForeignKey(entity = SensorValueEntity.class, parentColumns = "id", childColumns = "record_id", onDelete = CASCADE), indices = @Index(value = {"record_id"}))
public class SensorValueEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "value")
    private float value;

    @ColumnInfo(name = "record_id")
    private int recordId;

    public SensorValueEntity(float value, int recordId) {
        this.value = value;
        this.recordId = recordId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }
}

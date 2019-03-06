package com.adnagu.activityrecognition.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * ActivityRecordEntity
 *
 * @author ramazan.vapurcu
 * Created on 03/04/19
 */
@Entity(tableName = "activity_record", foreignKeys = @ForeignKey(entity = ActivityEntity.class, parentColumns = "id", childColumns = "activity_id", onDelete = ForeignKey.CASCADE), indices = @Index(value = {"activity_id"}))
public class ActivityRecordEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "activity_id")
    private int activityId;

    @ColumnInfo(name = "date")
    private Date date;

    public ActivityRecordEntity(int activityId, Date date) {
        this.activityId = activityId;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

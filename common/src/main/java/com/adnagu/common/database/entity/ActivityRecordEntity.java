package com.adnagu.common.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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

    @ColumnInfo(name = "test")
    private boolean test;

    public ActivityRecordEntity(int activityId, Date date, boolean test) {
        this.activityId = activityId;
        this.date = date;
        this.test = test;
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

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }
}

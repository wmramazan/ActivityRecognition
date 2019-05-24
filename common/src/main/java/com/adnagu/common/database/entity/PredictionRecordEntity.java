package com.adnagu.common.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * PredictionRecordEntity
 *
 * @author ramazan.vapurcu
 * Created on 4/24/2019
 */
@Entity(tableName = "prediction_record", foreignKeys = {
        @ForeignKey(entity = ActivityEntity.class, parentColumns = "id", childColumns = "activity_id", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = PredictionEntity.class, parentColumns = "id", childColumns = "prediction_id", onDelete = ForeignKey.CASCADE)
}, indices = @Index(value = {"activity_id", "prediction_id"}))
public class PredictionRecordEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "activity_id")
    private int activityId;

    @ColumnInfo(name = "prediction_id")
    private int predictionId;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "correct")
    private boolean correct;

    public PredictionRecordEntity(int activityId, int predictionId, Date date, boolean correct) {
        this.activityId = activityId;
        this.predictionId = predictionId;
        this.date = date;
        this.correct = correct;
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

    public int getPredictionId() {
        return predictionId;
    }

    public void setPredictionId(int predictionId) {
        this.predictionId = predictionId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}

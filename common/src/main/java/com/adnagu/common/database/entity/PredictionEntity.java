package com.adnagu.common.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * PredictionEntity
 *
 * @author ramazan.vapurcu
 * Created on 4/24/2019
 */
@Entity(tableName = "prediction")
public class PredictionEntity {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "correctness")
    private int correctness;

    public PredictionEntity(int id, Date date, int correctness) {
        this.id = id;
        this.date = date;
        this.correctness = correctness;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCorrectness() {
        return correctness;
    }

    public void setCorrectness(int correctness) {
        this.correctness = correctness;
    }
}

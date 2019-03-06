package com.adnagu.activityrecognition.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * ActivityEntity
 *
 * @author ramazan.vapurcu
 * Created on 03/04/19
 */
@Entity(tableName = "activity")
public class ActivityEntity {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    public ActivityEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

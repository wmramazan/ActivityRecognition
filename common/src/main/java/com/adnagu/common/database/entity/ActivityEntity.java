package com.adnagu.common.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

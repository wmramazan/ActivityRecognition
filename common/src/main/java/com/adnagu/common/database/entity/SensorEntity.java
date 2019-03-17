package com.adnagu.common.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * SensorEntity
 *
 * @author ramazan.vapurcu
 * Created on 10/15/2018
 */
@Entity(tableName = "sensor")
public class SensorEntity {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "vendor")
    private String vendor;

    @ColumnInfo(name = "min_delay")
    private int minDelay;

    @ColumnInfo(name = "max_delay")
    private int maxDelay;

    @ColumnInfo(name = "max_range")
    private float maxRange;

    @ColumnInfo(name = "resolution")
    private float resolution;

    @ColumnInfo(name = "power")
    private float power;

    public SensorEntity(int id, String name, String vendor, int minDelay, int maxDelay, float maxRange, float resolution, float power) {
        this.id = id;
        this.name = name;
        this.vendor = vendor;
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        this.maxRange = maxRange;
        this.resolution = resolution;
        this.power = power;
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

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public int getMinDelay() {
        return minDelay;
    }

    public void setMinDelay(int minDelay) {
        this.minDelay = minDelay;
    }

    public int getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(int maxDelay) {
        this.maxDelay = maxDelay;
    }

    public float getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(float maxRange) {
        this.maxRange = maxRange;
    }

    public float getResolution() {
        return resolution;
    }

    public void setResolution(float resolution) {
        this.resolution = resolution;
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }
}

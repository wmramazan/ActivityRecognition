package com.adnagu.activityrecognition.utils;

/**
 * ArffRecord
 *
 * @author ramazan.vapurcu
 * Created on 1/12/2019
 */
public class ArffRecord {
    private String value;
    private int timestamp;

    ArffRecord(String value, int timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}

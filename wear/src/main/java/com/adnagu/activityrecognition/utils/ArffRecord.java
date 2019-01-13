package com.adnagu.activityrecognition.utils;

/**
 * ArffRecord
 *
 * @author ramazan.vapurcu
 * Created on 1/12/2019
 */
public class ArffRecord {
    private String value;
    private long timestamp;

    ArffRecord(String value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

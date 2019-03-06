package com.adnagu.activityrecognition.ml;

/**
 * SequentialArffRecord
 *
 * @author ramazan.vapurcu
 * Created on 1/12/2019
 */
public class SequentialArffRecord {
    private String value;
    private long timestamp;

    SequentialArffRecord(String value, long timestamp) {
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

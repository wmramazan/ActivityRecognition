package com.adnagu.activityrecognition.common;

/**
 * BaseView
 *
 * @author ramazan.vapurcu
 * Created on 03/18/19
 */
public interface BaseView {
    void acquireWakeLock();
    void releaseWakeLock();
    void vibrate();
}

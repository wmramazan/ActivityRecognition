package com.adnagu.common.utils.listener;

import java.util.List;

/**
 * OnWindowListener
 *
 * @author ramazan.vapurcu
 * Created on 03/29/19
 */
public interface OnWindowListener {
    void onWindowStart();
    void onWindowSegment(List<Float> segment);
    void onWindowFinish();
}

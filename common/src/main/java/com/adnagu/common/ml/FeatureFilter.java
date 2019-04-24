package com.adnagu.common.ml;

import com.adnagu.common.model.Feature;

import java.util.ArrayList;
import java.util.List;

/**
 * FeatureFilter
 *
 * @author ramazan.vapurcu
 * Created on 04/09/2019
 */
public class FeatureFilter {

    private final String FEATURE_FILTER = "1,2,4,5,8,10,12,14,15,18,21,23,24,27,34,35,37,43,44,47,48,56,57,61,62,66,67,73,74,76,81,92,93,94,95,96,99,103,105,106,109,114,119,122,125,126,127,128,131,134,139,140,141,145,147,151,152,153,154,158,165,170,171,180,183,185,196,205,212,218,229,231";
    private final String FILTER_SEPARATOR = ",";

    private int[] selected_features;
    private int filter_index;
    private int feature_index;

    private boolean filter;

    public FeatureFilter() {
        filter = true;
        setFilter(FEATURE_FILTER, FILTER_SEPARATOR);
    }

    public FeatureFilter(boolean filter) {
        this.filter = filter;
        if (filter)
            setFilter(FEATURE_FILTER, FILTER_SEPARATOR);
    }

    public void setFilter(String featureFilter, String filterSeparator) {
        String[] filter = featureFilter.split(filterSeparator);
        selected_features = new int[filter.length];

        for (int i = 0; i < filter.length; i++)
            selected_features[i] = Integer.valueOf(filter[i]);

        init();
    }

    public void init() {
        filter_index = 0;
        feature_index = 1;
    }

    public boolean isValidFeature() {
        if (!filter)
            return true;

        if (filter_index < selected_features.length && selected_features[filter_index] == feature_index++) {
            filter_index++;
            return true;
        }
        return false;
    }

    public Feature[] getFeatureArray() {
        List<Feature> filteredFeatures = new ArrayList<>();

        for (Feature feature : Feature.values())
            if (isValidFeature())
                filteredFeatures.add(feature);

        Feature[] featureArray = new Feature[filteredFeatures.size()];
        featureArray = filteredFeatures.toArray(featureArray);

        return featureArray;
    }

}

package com.adnagu.common.ml;

import com.adnagu.common.model.Feature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * FeatureFilter
 *
 * @author ramazan.vapurcu
 * Created on 04/09/2019
 */
public class FeatureFilter {

    private final String FEATURE_FILTER = "92,93,99,95,57,100,101,56,74,62,75,73,96,69,80,17,66,8,9,23,60,67,44,165,4,173,174,164,22,21,166,43,61,18,179,160,88,10,79,105,31,54,171,114,158,5,34,122,127,124";
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

        Arrays.sort(selected_features);

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

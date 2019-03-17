package com.adnagu.common.ml;

import com.adnagu.common.model.Feature;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.List;

public class FeatureExtraction {

    private Feature[] features;
    private List<Float> featureValues;
    private DescriptiveStatistics statistics;

    public FeatureExtraction() {
        features = Feature.values();
        featureValues = new ArrayList<>();
        statistics = new DescriptiveStatistics();
    }

    public FeatureExtraction(Feature[] features) {
        this.features = features;
        featureValues = new ArrayList<>();
        statistics = new DescriptiveStatistics();
    }
    
    private void addFeatureValue(Feature feature) {
        switch (feature) {
            case min:
                featureValues.add((float) statistics.getMin());
                break;
            case max:
                featureValues.add((float) statistics.getMax());
                break;
            case range:
                featureValues.add(featureValues.get(Feature.max.ordinal()) - featureValues.get(Feature.min.ordinal()));
                break;
            case mean:
                featureValues.add((float) statistics.getMean());
                break;
            case quadraticMean:
                featureValues.add((float) statistics.getQuadraticMean());
                break;
            case std:
                featureValues.add((float) statistics.getStandardDeviation());
                break;
            case variance:
                featureValues.add((float) statistics.getVariance());
                break;
            case medianTop:
                featureValues.add((float) statistics.getPercentile(25));
                break;
            case medianMiddle:
                featureValues.add((float) statistics.getPercentile(50));
                break;
            case medianBottom:
                featureValues.add((float) statistics.getPercentile(75));
                break;
            case kurtosis:
                featureValues.add((float) statistics.getKurtosis());
                break;
            case popVariance:
                featureValues.add((float) statistics.getPopulationVariance());
                break;
            case skewness:
                featureValues.add((float) statistics.getPopulationVariance());
                break;
        }
    }

    public List<Float> getFeatureValues(List<Float> segment) {
        featureValues.clear();
        statistics.clear();

        for (Float value : segment)
            statistics.addValue(value);

        for (Feature feature : features)
            addFeatureValue(feature);

        return featureValues;
    }

}

package com.adnagu.activityrecognition.ml;

import com.adnagu.activityrecognition.model.Feature;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeatureExtraction {

    public static List<Float> extractAllFeatures(List<Float> segment) {
        List<Float> features = new ArrayList<>();

        DescriptiveStatistics statistics = new DescriptiveStatistics();
        for (Float value : segment)
            statistics.addValue(value);

//        min,
        features.add((float) statistics.getMin());
//        max,
        features.add((float) statistics.getMax());
//        maxDec,
//        maxInc,
//        range,
        features.add(features.get(Feature.max.ordinal()) - features.get(Feature.min.ordinal()));
//        mean,
        features.add((float) statistics.getMean());
//        insMean,
//        harmonicMean,
//        quadraticMean,
        features.add((float) statistics.getQuadraticMean());
//        insQuadraticMean,
//        mod,
//        median,
        
//        std,
        features.add((float) statistics.getStandardDeviation());
//        insStd,
//        variance,
        features.add((float) statistics.getVariance());
//        varianceCoeff,
//        medianTop,
        features.add((float) statistics.getPercentile(25));
//        medianMiddle,
        features.add((float) statistics.getPercentile(50));
//        medianBottom,
        features.add((float) statistics.getPercentile(75));
//        meanTop,
//        meanMiddle,
//        meanBottom,
//        medianTopSeg,
//        medianMiddleSeg,
//        medianBottomSeg,
//        meanTopSeg,
//        meanMiddleSeg,
//        meanBottomSeg,
//        zcr,
//        maxFft,
//        fftRate,
//        energy,
//        specRO,
//        specC,
//        specF,
//        maxima1,
//        maxima2,
//        maxima3,
//        maxima4,
//        maxima5

        return features;
    }

}

package com.adnagu.common.ml;

import android.content.Context;
import android.util.Log;

import com.adnagu.common.R;
import com.adnagu.common.model.Activity;
import com.adnagu.common.model.Feature;
import com.adnagu.common.model.SensorType;

import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

/**
 * ActivityPrediction
 *
 * @author ramazan.vapurcu
 * Created on 03/17/19
 */
public class ActivityPrediction {

    private final String DEBUG_TAG = getClass().getName();

    private Classifier classifier;
    private Instances instances;
    private ArrayList<Attribute> attributes;
    private int[] predictions;

    public ActivityPrediction(Context context) {
        attributes = new ArrayList<>();

        for (SensorType sensorType : SensorType.values())
            for (char value : sensorType.values)
                for (Feature feature : Feature.values())
                    attributes.add(
                            new Attribute(sensorType.prefix + "_" + feature.name() + "_" + value)
                    );

        init(context);
    }

    public ActivityPrediction(Context context, FeatureFilter filter) {
        attributes = new ArrayList<>();

        filter.init();
        for (SensorType sensorType : SensorType.values())
            for (char value : sensorType.values)
                for (Feature feature : Feature.values())
                    if (filter.isValidFeature())
                        attributes.add(
                                new Attribute(sensorType.prefix + "_" + feature.name() + "_" + value)
                        );

        init(context);
    }

    private void init(Context context) {
        predictions = new int[Activity.values().length];

        List<String> activityList = new ArrayList<>();
        for (Activity activity : Activity.values())
            activityList.add(activity.name());

        attributes.add(new Attribute("Class", activityList));

        instances = new Instances("ActivityRecognition", attributes, 0);
        instances.setClassIndex(attributes.size() - 1);

        try {
            classifier = (Classifier) SerializationHelper.read(
                    context.getResources().openRawResource(R.raw.random_forest)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int predict(List<Float> featureValues) {
        Instance instance = new DenseInstance(attributes.size());

        for (int i = 0; i < featureValues.size(); i++)
            instance.setValue(i, featureValues.get(i));

        instance.setDataset(instances);

        try {
            return (int) classifier.classifyInstance(instance);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void addInstance(List<Float> featureValues) {
        int prediction = predict(featureValues);

        if (prediction != -1) {
            predictions[prediction]++;
        }
    }

    public int getPrediction() {
        int index = 0;
        int maximum = predictions[0];

        for (int i = 1; i < predictions.length; i++) {
            if (maximum < predictions[i]) {
                index = i;
                maximum = predictions[i];
            }
        }

        for (int i = 0; i < predictions.length; i++)
            Log.d(DEBUG_TAG, Activity.values()[i].name() + "->" + predictions[i]);

        return index;
    }

    public void clear() {
        for (int i = 0; i < predictions.length; i++)
            predictions[i] = 0;
    }

}

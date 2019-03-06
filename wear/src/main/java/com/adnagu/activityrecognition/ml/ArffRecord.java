package com.adnagu.activityrecognition.ml;

public class ArffRecord {
    float x, y, z, a, b;
    int numberOfValues;

    public ArffRecord(float x, float y, float z, int numberOfValues) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.numberOfValues = numberOfValues;

        if (numberOfValues > 3) {
            a = (float) Math.sqrt(
                    x * x + y * y + z * z
            );
            if (numberOfValues == 5)
                b = (float) Math.sqrt(
                        x * x + z * z
                );
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public int getNumberOfValues() {
        return numberOfValues;
    }

    public void setNumberOfValues(int numberOfValues) {
        this.numberOfValues = numberOfValues;
    }
}

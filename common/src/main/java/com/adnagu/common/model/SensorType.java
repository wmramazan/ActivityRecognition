package com.adnagu.common.model;

public enum SensorType {
    Accelerometer(android.hardware.Sensor.TYPE_ACCELEROMETER, "acc", new char[]{'x', 'y', 'z', 'a', 'b'}),
    MagneticField(android.hardware.Sensor.TYPE_MAGNETIC_FIELD, "mag", new char[]{'x', 'y', 'z', 'a'}),
    Gyroscope(android.hardware.Sensor.TYPE_GYROSCOPE, "gyro", new char[]{'x', 'y', 'z'}),
    Gravity(android.hardware.Sensor.TYPE_GRAVITY, "gra", new char[]{'x', 'y', 'z'});
    //LinearAcceleration(android.hardware.Sensor.TYPE_LINEAR_ACCELERATION, "lacc", new char[]{'x', 'y', 'z'});

    public final int id;
    public final String prefix;
    public final char[] values;

    SensorType(int id, String prefix, char[] values) {
        this.id = id;
        this.prefix = prefix;
        this.values = values;
    }
}

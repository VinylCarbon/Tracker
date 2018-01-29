package com.tracker.common.utils;

public class SpeedFormatter {

    public static String format(long distanceInMeters, long timeInSeconds) {
        long speedMeterPerSecond = distanceInMeters / timeInSeconds;
        return speedMeterPerSecond + " m/s";
    }
}

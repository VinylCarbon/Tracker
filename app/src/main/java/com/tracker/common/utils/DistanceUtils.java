package com.tracker.common.utils;

import android.location.Location;

import javax.inject.Inject;

public class DistanceUtils {

    @Inject
    DistanceUtils() {
    }

    public double distanceBetween(double lat1, double lng1, double lat2, double lng2) {
        float[] dist = new float[1];
        Location.distanceBetween(lat1, lng1, lat2, lng2, dist);
        return dist[0];
    }

}

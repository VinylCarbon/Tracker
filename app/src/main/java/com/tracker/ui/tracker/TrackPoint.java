package com.tracker.ui.tracker;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class TrackPoint {

    abstract double latitude();

    abstract double longitude();

    abstract long timestamp();

    @NonNull
    public static TrackPoint.Builder builder() {
        return new AutoValue_TrackPoint.Builder();
    }

    @AutoValue.Builder
    interface Builder {

        TrackPoint.Builder latitude(final double latitude);

        TrackPoint.Builder longitude(final double longitude);

        TrackPoint.Builder timestamp(final long timestamp);

        TrackPoint build();
    }
}

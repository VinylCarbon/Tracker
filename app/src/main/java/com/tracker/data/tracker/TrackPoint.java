package com.tracker.data.tracker;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TrackPoint {

    public abstract double latitude();

    public abstract double longitude();

    public abstract long timestamp();

    @NonNull
    public static TrackPoint.Builder builder() {
        return new AutoValue_TrackPoint.Builder();
    }

    @AutoValue.Builder
    public interface Builder {

        TrackPoint.Builder latitude(final double latitude);

        TrackPoint.Builder longitude(final double longitude);

        TrackPoint.Builder timestamp(final long timestamp);

        TrackPoint build();
    }
}

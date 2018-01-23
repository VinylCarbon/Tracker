package com.tracker.ui.tracker;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
abstract class Track {

    abstract long id();

    @NonNull
    abstract String name();

    abstract long startTime();

    abstract long finishTime();

    @NonNull
    abstract List<TrackPoint> trackPoints();

    @NonNull
    public static Track.Builder builder() {
        return new AutoValue_Track.Builder();
    }

    @AutoValue.Builder
    interface Builder {

        Track.Builder id(final long id);

        Track.Builder name(final String name);

        Track.Builder startTime(final long startTime);

        Track.Builder finishTime(final long finishTime);

        Track.Builder trackPoints(final List<TrackPoint> trackPoints);

        Track build();
    }
}

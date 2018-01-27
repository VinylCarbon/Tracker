package com.tracker.data.tracker;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Track {

    public abstract long id();

    @NonNull
    public abstract String name();

    public abstract long startTime();

    public abstract long finishTime();

    @NonNull
    public abstract List<TrackPoint> trackPoints();

    @NonNull
    public static Track.Builder builder() {
        return new AutoValue_Track.Builder();
    }

    @AutoValue.Builder
    public interface Builder {

        Track.Builder id(final long id);

        Track.Builder name(final String name);

        Track.Builder startTime(final long startTime);

        Track.Builder finishTime(final long finishTime);

        Track.Builder trackPoints(final List<TrackPoint> trackPoints);

        Track build();
    }
}

package com.tracker.ui.tracks;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TrackViewEntity {

    abstract long id();

    @NonNull
    public abstract String name();

    @NonNull
    abstract String formattedCreatedOn();

    @NonNull
    public abstract String formattedTime();

    @NonNull
    abstract String formattedStartTime();

    @NonNull
    abstract String formattedFinishTime();

    @NonNull
    public abstract String formattedDistance();

    @NonNull
    public abstract String formattedSpeed();

    public static Builder builder() {
        return new AutoValue_TrackViewEntity.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder name(String name);

        public abstract Builder formattedCreatedOn(String formattedCreatedOn);

        public abstract Builder formattedStartTime(String formattedStartTime);

        public abstract Builder formattedFinishTime(String formattedFinishTime);

        public abstract Builder formattedDistance(String formattedDistance);

        public abstract Builder formattedSpeed(String formattedSpeed);

        public abstract Builder formattedTime(String formattedTime);

        public abstract TrackViewEntity build();
    }
}

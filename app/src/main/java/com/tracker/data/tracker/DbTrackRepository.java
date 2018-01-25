package com.tracker.data.tracker;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import polanski.option.Option;

class DbTrackRepository implements TrackRepository {
    @NonNull
    Option<TrackPoint> trackPoint = Option.none();
    @NonNull
    TrackingState trackingState = TrackingState.NOT_TRACKING;

    @Override
    public Observable<TrackPoint> trackPoint() {
        return null;
    }

    @Override
    public Observable<Track> track() {
        return null;
    }

    @Override
    public Observable<TrackingState> trackingState() {
        return null;
    }

    @Override
    public void addTrackPoint(TrackPoint trackPoint) {
        this.trackPoint = Option.ofObj(trackPoint);

        if (trackingState.isTracking()) {
            addToTrack(trackPoint);
        }
    }

    private void addToTrack(TrackPoint trackPoint) {
        // save to sql lite for current track.
    }
}

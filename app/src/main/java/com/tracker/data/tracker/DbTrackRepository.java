package com.tracker.data.tracker;

import io.reactivex.Observable;

class DbTrackRepository implements TrackRepository {

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
}

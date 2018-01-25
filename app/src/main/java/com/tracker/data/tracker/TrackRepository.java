package com.tracker.data.tracker;

import io.reactivex.Observable;

public interface TrackRepository {

    Observable<TrackPoint> trackPoint();

    Observable<Track> track();

    Observable<TrackingState> trackingState();

    void addTrackPoint(TrackPoint trackPoint);
}

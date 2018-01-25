package com.tracker.data.tracker;

import io.reactivex.Observable;
import io.reactivex.Single;
import polanski.option.Option;

public interface TrackRepository {

    Observable<TrackPoint> trackPoint();

    Observable<Track> track();

    Observable<TrackingState> trackingState();

    Single<Boolean> addTrackPoint(TrackPoint trackPoint);

    Single<Boolean> setTrackingState(boolean tracking);
}

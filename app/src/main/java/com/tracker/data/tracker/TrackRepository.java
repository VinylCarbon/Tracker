package com.tracker.data.tracker;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface TrackRepository {

    Observable<TrackPoint> trackPoint();

    Observable<Track> track();

    Observable<TrackingState> trackingState();

    Single<Boolean> addTrackPoint(TrackPoint trackPoint);

    Single<Boolean> setTrackingState(boolean tracking);

    Single<List<Track>> allTracks();
}

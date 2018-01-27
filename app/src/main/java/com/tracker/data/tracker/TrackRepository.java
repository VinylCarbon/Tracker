package com.tracker.data.tracker;

import com.tracker.data.tracker.db.TrackRaw;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface TrackRepository {

    Observable<TrackPoint> trackPoint();

    Observable<Track> track();

    Observable<TrackingState> trackingState();

    Single<Boolean> addTrackPoint(TrackPoint trackPoint);

    Single<Boolean> setTrackingState(boolean tracking);

    Flowable<List<TrackRaw>> allTracks();
}

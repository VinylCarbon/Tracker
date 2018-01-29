package com.tracker.data.tracker;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface TrackRepository {

    Observable<TrackPoint> trackPoint();

    Observable<Track> currentTrack();

    Observable<TrackingState> trackingState();

    Single<Boolean> addTrackPoint(TrackPoint trackPoint);

    Single<Boolean> setTrackingState(boolean tracking);

    Observable<List<Track>> allTracks();

    Observable<Track> track(long trackId);

    Observable<List<TrackPoint>> trackPoints(long trackId);
}

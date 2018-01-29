package com.tracker.data.tracker;

import android.support.annotation.NonNull;

import com.tracker.common.providers.TimestampProvider;
import com.tracker.common.providers.TrackNameProvider;
import com.tracker.common.utils.DistanceUtils;
import com.tracker.data.tracker.db.TrackDao;
import com.tracker.data.tracker.db.TrackPointDao;
import com.tracker.data.tracker.db.TrackPointRaw;
import com.tracker.data.tracker.db.TrackRaw;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import polanski.option.Option;

import static com.tracker.data.tracker.TrackingState.NOT_TRACKING;
import static com.tracker.data.tracker.TrackingState.TRACKING;
import static polanski.option.Option.none;
import static polanski.option.OptionUnsafe.getUnsafe;

class DbTrackRepository implements TrackRepository {
    @NonNull
    Option<TrackPoint> trackPoint = none();
    @NonNull
    Option<Track> activeTrack = none();
    @NonNull
    TrackingState trackingState = NOT_TRACKING;
    private long activeTrackDistanceInMeter = 0;


    private final PublishSubject<Track> currentTrackSubject = PublishSubject.create();
    private final PublishSubject<TrackPoint> trackPointSubject = PublishSubject.create();
    private final PublishSubject<TrackingState> trackingStateSubject = PublishSubject.create();

    @Inject
    TrackDao trackDao;
    @Inject
    TrackPointDao trackPointDao;
    @Inject
    TimestampProvider timestampProvider;
    @Inject
    TrackNameProvider trackNameProvider;
    @Inject
    DistanceUtils distanceUtils;

    @Inject
    public DbTrackRepository() {
    }

    @Override
    public Observable<TrackPoint> trackPoint() {
        return trackPointSubject;
    }

    @Override
    public Observable<Track> currentTrack() {
        return currentTrackSubject;
    }

    @Override
    public Observable<TrackingState> trackingState() {
        return trackingStateSubject;
    }

    @Override
    public Single<Boolean> addTrackPoint(TrackPoint trackPoint) {
        return Observable.fromCallable(() -> {
            trackPointSubject.onNext(trackPoint);

            if (trackingState.isTracking())
                addToTrack(trackPoint);
            this.trackPoint = Option.ofObj(trackPoint);
            return Boolean.TRUE;
        }).single(Boolean.FALSE);
    }

    @Override
    public Single<Boolean> setTrackingState(boolean tracking) {
        return Observable.fromCallable(() -> {
            if (tracking) startTracking();
            else stopTracking();
            return Boolean.TRUE;
        }).single(Boolean.FALSE);
    }

    @Override
    public Observable<List<Track>> allTracks() {
        return trackDao.allTracks()
                .toObservable()
                .map(trackRaws ->
                        Observable.fromIterable(trackRaws)
                                .map(this::track)
                                .toList()
                                .blockingGet());

    }

    @Override
    public Observable<Track> track(long trackId) {
        return trackDao.track(trackId)
                .toObservable()
                .map(this::track);
    }

    @Override
    public Observable<List<TrackPoint>> trackPoints(long trackId) {
        return trackPointDao.allTrackPoints(trackId)
                .toObservable()
                .map(trackPointRaws -> Observable.fromIterable(trackPointRaws)
                        .map(this::trackPoint)
                        .toList()
                        .blockingGet());
    }

    public void startTracking() {
        trackingState = TRACKING;
        trackingStateSubject.onNext(TRACKING);
        TrackRaw trackRaw = createAndInsertTrack();

        activeTrack = Option.ofObj(track(trackRaw));
    }

    public void stopTracking() {
        trackingState = NOT_TRACKING;
        trackingFinished();
        trackingStateSubject.onNext(NOT_TRACKING);
    }

    private void trackingFinished() {
        activeTrack
                .ifSome(track ->
                        trackDao.update(new TrackRaw.Builder()
                                .id(track.id())
                                .name(track.name())
                                .startTime(track.startTime())
                                .finishTime(timestampProvider.currentTimeMillis())
                                .distance(activeTrackDistanceInMeter)
                                .build()));
    }

    private void addToTrack(TrackPoint trackPoint) {
        if (activeTrack.isSome()) {
            getUnsafe(activeTrack).trackPoints().add(trackPoint);
            currentTrackSubject.onNext(getUnsafe(activeTrack));
            this.trackPoint.ifSome(trackPoint1 -> updateActiveTrackDistance(trackPoint, trackPoint1));

            trackPointDao.insert(trackPointRaw(trackPoint));
        }

    }

    private void updateActiveTrackDistance(TrackPoint trackPoint, TrackPoint trackPoint1) {
        activeTrackDistanceInMeter += (long) distanceUtils.distanceBetween(
                trackPoint1.latitude(), trackPoint1.longitude(),
                trackPoint.latitude(), trackPoint.longitude());
    }

    @NonNull
    private TrackRaw createAndInsertTrack() {
        TrackRaw trackRaw = createTrackRaw();
        long id = trackDao.insert(trackRaw);
        trackRaw.setId(id);
        return trackRaw;
    }

    private TrackRaw createTrackRaw() {
        activeTrackDistanceInMeter = 0;
        return new TrackRaw.Builder()
                .name(trackNameProvider.name())
                .startTime(timestampProvider.currentTimeMillis())
                .build();
    }

    private Track track(TrackRaw trackRaw) {
        return Track.builder()
                .id(trackRaw.getId())
                .name(trackRaw.getName())
                .startTime(trackRaw.getStartTime())
                .finishTime(trackRaw.getFinishTime())
                .distanceInMeter(trackRaw.getDistance())
                .trackPoints(new ArrayList<>())
                .build();
    }

    private TrackPointRaw trackPointRaw(TrackPoint trackPoint) {
        return new TrackPointRaw.Builder()
                .latitude(trackPoint.latitude())
                .longitude(trackPoint.longitude())
                .timestamp(trackPoint.timestamp())
                .trackId(getUnsafe(activeTrack).id())
                .build();
    }

    private TrackPoint trackPoint(TrackPointRaw trackPointRaw) {
        return TrackPoint.builder()
                .latitude(trackPointRaw.getLatitude())
                .longitude(trackPointRaw.getLongitude())
                .timestamp(trackPointRaw.getTimestamp())
                .build();
    }
}

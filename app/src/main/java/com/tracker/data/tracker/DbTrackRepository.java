package com.tracker.data.tracker;

import android.support.annotation.NonNull;

import com.tracker.common.providers.TimestampProvider;
import com.tracker.common.providers.TrackNameProvider;
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


    private final PublishSubject<Track> trackSubject = PublishSubject.create();
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
    public DbTrackRepository() {
    }

    @Override
    public Observable<TrackPoint> trackPoint() {
        return trackPointSubject;
    }

    @Override
    public Observable<Track> track() {
        return trackSubject;
    }

    @Override
    public Observable<TrackingState> trackingState() {
        return trackingStateSubject;
    }

    @Override
    public Single<Boolean> addTrackPoint(TrackPoint trackPoint) {
        return Observable.fromCallable(() -> {
            this.trackPoint = Option.ofObj(trackPoint);
            trackPointSubject.onNext(trackPoint);

            if (trackingState.isTracking())
                addToTrack(trackPoint);
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
        activeTrack.ifSome(track -> trackDao.update(new TrackRaw.Builder()
                .id(track.id())
                .name(track.name())
                .startTime(track.startTime())
                .finishTime(timestampProvider.currentTimeMillis())
                .build()));
    }

    private void addToTrack(TrackPoint trackPoint) {
        if (activeTrack.isSome()) {
            getUnsafe(activeTrack).trackPoints().add(trackPoint);
            trackSubject.onNext(getUnsafe(activeTrack));

            trackPointDao.insert(trackPointRaw(trackPoint));
        }
    }

    @NonNull
    private TrackRaw createAndInsertTrack() {
        TrackRaw trackRaw = createTrackRaw();
        long id = trackDao.insert(trackRaw);
        trackRaw.setId(id);
        return trackRaw;
    }

    private TrackRaw createTrackRaw() {
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
}

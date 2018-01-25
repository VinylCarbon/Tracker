package com.tracker.data.tracker;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.tracker.common.providers.TimestampProvider;
import com.tracker.common.providers.TrackNameProvider;
import com.tracker.data.tracker.db.TrackDao;
import com.tracker.data.tracker.db.TrackPointDao;
import com.tracker.data.tracker.db.TrackPointRaw;
import com.tracker.data.tracker.db.TrackRaw;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import polanski.option.Option;

import static com.tracker.data.tracker.TrackingState.NOT_TRACKING;
import static com.tracker.data.tracker.TrackingState.TRACKING;
import static polanski.option.OptionUnsafe.getUnsafe;

class DbTrackRepository implements TrackRepository {
    @NonNull
    Option<TrackPoint> trackPoint = Option.none();
    @NonNull
    Option<Track> activeTrack = Option.none();
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
    public void addTrackPoint(TrackPoint trackPoint) {
        this.trackPoint = Option.ofObj(trackPoint);
        trackPointSubject.onNext(trackPoint);

        if (trackingState.isTracking())
            addToTrack(trackPoint);
    }

    @Override
    public void startTracking() {
        trackingStateSubject.onNext(TRACKING);
        TrackRaw trackRaw = createAndInsertTrack();

        activeTrack = Option.ofObj(track(trackRaw));
    }

    @NonNull
    private TrackRaw createAndInsertTrack() {
        TrackRaw trackRaw = createTrackRaw();
        long id = trackDao.insert(trackRaw);
        trackRaw.setId(id);
        return trackRaw;
    }

    @Override
    public void stopTracking() {
        trackingStateSubject.onNext(NOT_TRACKING);
    }

    private void addToTrack(TrackPoint trackPoint) {
        if (activeTrack.isSome()) {
            getUnsafe(activeTrack).trackPoints().add(trackPoint);
            trackSubject.onNext(getUnsafe(activeTrack));

            trackPointDao.insert(trackPointRaw(trackPoint));
        }
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

package com.tracker.domain.tracker;

import android.support.annotation.NonNull;

import com.tracker.data.tracker.TrackPoint;
import com.tracker.data.tracker.TrackRepository;
import com.tracker.domain.ReactiveInteractor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import io.reactivex.Observable;
import polanski.option.Option;

public class RetrieveTrackPoint implements ReactiveInteractor.RetrieveInteractor<Void, TrackPoint> {

    @NonNull
    private final TrackRepository trackRepository;

    @Inject
    RetrieveTrackPoint(@NonNull final TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Nonnull
    @Override
    public Observable<TrackPoint> getBehaviourStream(@Nonnull final Option<Void> params) {
        return trackRepository.trackPoint();
    }
}

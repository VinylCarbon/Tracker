package com.tracker.domain.tracker;

import android.support.annotation.NonNull;

import com.tracker.data.tracker.TrackRepository;
import com.tracker.data.tracker.TrackingState;
import com.tracker.domain.ReactiveInteractor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import io.reactivex.Observable;
import polanski.option.Option;

public class RetrieveTrackingState implements ReactiveInteractor.RetrieveInteractor<Void, TrackingState> {

    @NonNull
    private final TrackRepository trackRepository;

    @Inject
    RetrieveTrackingState(@NonNull final TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Nonnull
    @Override
    public Observable<TrackingState> getBehaviourStream(@Nonnull final Option<Void> params) {
        return trackRepository.trackingState();
    }
}

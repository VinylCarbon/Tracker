package com.tracker.domain.tracker;

import android.support.annotation.NonNull;

import com.tracker.data.tracker.TrackRepository;
import com.tracker.data.tracker.TrackingState;
import com.tracker.domain.ReactiveInteractor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import io.reactivex.Single;
import polanski.option.Option;

import static polanski.option.OptionUnsafe.getUnsafe;

public class SendTrackingState implements ReactiveInteractor.SendInteractor<TrackingState, Boolean> {

    @NonNull
    private final TrackRepository trackRepository;

    @Inject
    SendTrackingState(@NonNull final TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Nonnull
    @Override
    public Single<Boolean> getSingle(@Nonnull Option<TrackingState> params) {
        return trackRepository.setTrackingState(getUnsafe(params).isTracking());
    }
}

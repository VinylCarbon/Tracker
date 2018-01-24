package com.tracker.domain.tracker;

import com.tracker.data.tracker.TrackingState;
import com.tracker.domain.ReactiveInteractor;

import javax.annotation.Nonnull;

import io.reactivex.Single;
import polanski.option.Option;

public class SendTrackingState implements ReactiveInteractor.SendInteractor<TrackingState, Void>{

    @Nonnull
    @Override
    public Single<Void> getSingle(@Nonnull Option<TrackingState> params) {
        return null;
    }
}

package com.tracker.domain.tracker;

import android.support.annotation.NonNull;

import com.tracker.data.tracker.Track;
import com.tracker.data.tracker.TrackPoint;
import com.tracker.data.tracker.TrackRepository;
import com.tracker.domain.ReactiveInteractor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import io.reactivex.Observable;
import polanski.option.Option;

public class RetrieveTrack implements ReactiveInteractor.RetrieveInteractor<Void, Track> {

    @NonNull
    private final TrackRepository trackRepository;

    @Inject
    RetrieveTrack(@NonNull final TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Nonnull
    @Override
    public Observable<Track> getBehaviourStream(@Nonnull final Option<Void> params) {
        return trackRepository.track();
    }
}

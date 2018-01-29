package com.tracker.domain.trackdetail;

import android.support.annotation.NonNull;

import com.tracker.data.tracker.Track;
import com.tracker.data.tracker.TrackRepository;
import com.tracker.domain.ReactiveInteractor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import io.reactivex.Observable;
import polanski.option.Option;

import static polanski.option.OptionUnsafe.getUnsafe;

public class RetrieveTrack implements ReactiveInteractor.RetrieveInteractor<Long, Track> {

    @NonNull
    private final TrackRepository trackRepository;

    @Inject
    RetrieveTrack(@NonNull final TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Nonnull
    @Override
    public Observable<Track> getBehaviourStream(@Nonnull final Option<Long> trackId) {
        return trackRepository.track(getUnsafe(trackId));
    }
}

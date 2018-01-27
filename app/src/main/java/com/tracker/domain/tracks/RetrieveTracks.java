package com.tracker.domain.tracks;

import android.support.annotation.NonNull;

import com.tracker.data.tracker.Track;
import com.tracker.data.tracker.TrackRepository;
import com.tracker.domain.ReactiveInteractor;

import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import io.reactivex.Observable;
import polanski.option.Option;

public class RetrieveTracks implements ReactiveInteractor.RetrieveInteractor<Void, List<Track>> {

    @NonNull
    private final TrackRepository trackRepository;

    @Inject
    RetrieveTracks(@NonNull final TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Nonnull
    @Override
    public Observable<List<Track>> getBehaviourStream(@Nonnull final Option<Void> params) {
        return trackRepository.allTracks().toObservable();
    }
}

package com.tracker.domain.trackdetail;

import android.support.annotation.NonNull;

import com.tracker.data.tracker.TrackPoint;
import com.tracker.data.tracker.TrackRepository;
import com.tracker.domain.ReactiveInteractor;

import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import io.reactivex.Observable;
import polanski.option.Option;

import static polanski.option.OptionUnsafe.getUnsafe;

public class RetrieveTrackPoints implements ReactiveInteractor.RetrieveInteractor<Long, List<TrackPoint>> {

    @NonNull
    private final TrackRepository trackRepository;

    @Inject
    RetrieveTrackPoints(@NonNull final TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Nonnull
    @Override
    public Observable<List<TrackPoint>> getBehaviourStream(@Nonnull final Option<Long> trackId) {
        return trackRepository.trackPoints(getUnsafe(trackId));
    }
}

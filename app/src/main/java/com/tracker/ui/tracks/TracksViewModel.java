package com.tracker.ui.tracks;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.tracker.data.tracker.TrackRepository;
import com.tracker.data.tracker.db.TrackRaw;
import com.tracker.domain.tracks.RetrieveTracks;
import com.tracker.ui.recyclerview.DisplayableItem;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TracksViewModel extends ViewModel {

    private TrackRepository trackRepository;

    @NonNull
    private final RetrieveTracks retrieveTracks;

    @NonNull
    private final MutableLiveData<List<DisplayableItem>> tracksLiveData = new MutableLiveData<>();

    @NonNull
    private final TrackDisplayableItemMapper tacksDisplayableItemMapper;

    @NonNull
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public TracksViewModel(@NonNull final TrackRepository trackRepository,
                           @NonNull final RetrieveTracks retrieveTracks,
                           @NonNull final TrackDisplayableItemMapper tacksDisplayableItemMapper) {
        this.trackRepository = trackRepository;
        this.retrieveTracks = retrieveTracks;
        this.tacksDisplayableItemMapper = tacksDisplayableItemMapper;

        compositeDisposable.add(bindToTracks());
    }

    private Disposable bindToTracks() {
        return trackRepository
                .allTracks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::tracksFound);
    }

    private void tracksFound(List<TrackRaw> trackRaws) {
        try {
            saveTracks(tacksDisplayableItemMapper.apply(trackRaws));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveTracks(List<DisplayableItem> displayableItems) {
        tracksLiveData.postValue(displayableItems);
    }

    @NonNull
    LiveData<List<DisplayableItem>> getTrackListLiveData() {
        return tracksLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}

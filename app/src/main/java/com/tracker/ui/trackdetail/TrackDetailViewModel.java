package com.tracker.ui.trackdetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.tracker.data.tracker.TrackPoint;
import com.tracker.domain.trackdetail.RetrieveTrack;
import com.tracker.domain.trackdetail.RetrieveTrackPoints;
import com.tracker.ui.tracks.TrackViewEntity;
import com.tracker.ui.tracks.TrackViewEntityMapper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import polanski.option.Option;

public class TrackDetailViewModel extends ViewModel {

    @NonNull
    private final RetrieveTrack retrieveTrack;
    @NonNull
    private final RetrieveTrackPoints retrieveTrackPoints;
    private TrackViewEntityMapper trackViewEntityMapper;

    @NonNull
    private final MutableLiveData<TrackViewEntity> trackLiveData = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<List<TrackPoint>> trackPointsLiveData = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    TrackDetailViewModel(@NonNull final RetrieveTrack retrieveTrack,
                         @NonNull final RetrieveTrackPoints retrieveTrackPoints,
                         @NonNull final TrackViewEntityMapper trackViewEntityMapper) {
        this.retrieveTrack = retrieveTrack;
        this.retrieveTrackPoints = retrieveTrackPoints;
        this.trackViewEntityMapper = trackViewEntityMapper;
    }

    @NonNull
    LiveData<TrackViewEntity> track() {
        return trackLiveData;
    }

    @NonNull
    LiveData<List<TrackPoint>> trackPath() {
        return trackPointsLiveData;
    }

    public void bindTrack(long trackId) {
        compositeDisposable.add(retrieveTrack
                .getBehaviourStream(Option.ofObj(trackId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(trackViewEntityMapper)
                .subscribe(trackLiveData::postValue));
        compositeDisposable.add(retrieveTrackPoints
                .getBehaviourStream(Option.ofObj(trackId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(trackPointsLiveData::postValue));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}

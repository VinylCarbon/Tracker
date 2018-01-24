package com.tracker.ui.tracker;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.tracker.data.tracker.Track;
import com.tracker.data.tracker.TrackPoint;
import com.tracker.data.tracker.TrackingState;
import com.tracker.domain.tracker.RetrieveTrack;
import com.tracker.domain.tracker.RetrieveTrackPoint;
import com.tracker.domain.tracker.RetrieveTrackingState;
import com.tracker.domain.tracker.SendTrackingState;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import polanski.option.Option;

import static polanski.option.Option.none;


public class TrackerViewModel extends ViewModel {

    @NonNull
    private final LocationProvider locationProvider;
    @NonNull
    private final RetrieveTrackingState retrieveTrackingState;
    @NonNull
    private final RetrieveTrackPoint retrieveTrackPoint;
    @NonNull
    private final RetrieveTrack retrieveTrack;
    @NonNull
    private final SendTrackingState sendTrackingState;

    @NonNull
    private final MutableLiveData<TrackingState> trackingStateLiveData = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<TrackPoint> trackPointLiveData = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<Track> trackLiveData = new MutableLiveData<>();

    @NonNull
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    TrackerViewModel(@NonNull LocationProvider locationProvider,
                     @NonNull final RetrieveTrackingState retrieveTrackingState,
                     @NonNull final RetrieveTrackPoint retrieveTrackPoint,
                     @NonNull final RetrieveTrack retrieveTrack,
                     @NonNull final SendTrackingState sendTrackingState) {
        this.locationProvider = locationProvider;
        this.retrieveTrackingState = retrieveTrackingState;
        this.retrieveTrackPoint = retrieveTrackPoint;
        this.retrieveTrack = retrieveTrack;
        this.sendTrackingState = sendTrackingState;

        startTrackUpdates();
        bindTracker();
    }

    private void startTrackUpdates() {
        locationProvider.startService();
    }

    private void bindTracker() {
        compositeDisposable.add(retrieveTrackingState.getBehaviourStream(none())
                .observeOn(Schedulers.computation())
                .subscribe(trackingStateLiveData::postValue));

        compositeDisposable.add(retrieveTrackPoint.getBehaviourStream(none())
                .observeOn(Schedulers.computation())
                .subscribe(trackPointLiveData::postValue));

        compositeDisposable.add(retrieveTrack.getBehaviourStream(none())
                .observeOn(Schedulers.computation())
                .subscribe(trackLiveData::postValue));
    }

    @NonNull
    LiveData<TrackingState> trackingState() {
        return trackingStateLiveData;
    }

    @NonNull
    LiveData<TrackPoint> trackPoint() {
        return trackPointLiveData;
    }

    @NonNull
    LiveData<Track> track() {
        return trackLiveData;
    }

    void startTracking() {
        compositeDisposable.add(sendTrackingState
                .getSingle(Option.ofObj(TrackingState.TRACKING))
                .subscribe(__ -> trackingStarted()));
    }

    void stopTracking() {
        compositeDisposable.add(sendTrackingState
                .getSingle(Option.ofObj(TrackingState.TRACKING))
                .subscribe(__ -> trackingStopped()));
    }

    private void trackingStarted() {
    }

    private void trackingStopped() {
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}

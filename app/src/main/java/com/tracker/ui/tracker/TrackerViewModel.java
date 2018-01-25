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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import polanski.option.Option;

import static polanski.option.Option.none;


public class TrackerViewModel extends ViewModel {

    private TrackingState trackingState = TrackingState.NOT_TRACKING;

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
    TrackerViewModel(@NonNull final LocationProvider locationProvider,
                     @NonNull final RetrieveTrackingState retrieveTrackingState,
                     @NonNull final RetrieveTrackPoint retrieveTrackPoint,
                     @NonNull final RetrieveTrack retrieveTrack,
                     @NonNull final SendTrackingState sendTrackingState) {
        this.locationProvider = locationProvider;
        this.retrieveTrackingState = retrieveTrackingState;
        this.retrieveTrackPoint = retrieveTrackPoint;
        this.retrieveTrack = retrieveTrack;
        this.sendTrackingState = sendTrackingState;
    }

    private void startTrackUpdates() {
        bindTracker();
        locationProvider.startService();
    }

    private void bindTracker() {
        compositeDisposable.add(retrieveTrackingState.getBehaviourStream(none())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setTrackingStateLiveData));

        compositeDisposable.add(retrieveTrackPoint.getBehaviourStream(none())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trackPointLiveData::postValue));

        compositeDisposable.add(retrieveTrack.getBehaviourStream(none())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trackLiveData::postValue));
    }

    private void setTrackingStateLiveData(@NonNull TrackingState trackingState) {
        this.trackingState = trackingState;
        if (trackingState.isTracking()) trackingStarted();
        else trackingStopped();

        trackingStateLiveData.postValue(trackingState);
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
        startLocationUpdates();
        compositeDisposable.add(sendTrackingState
                .getSingle(Option.ofObj(TrackingState.TRACKING))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(__ -> trackingStarted()));
    }

    void finishTracking() {
        compositeDisposable.add(sendTrackingState
                .getSingle(Option.ofObj(TrackingState.NOT_TRACKING))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(__ -> trackingStopped()));
    }

    private void trackingStarted() {
        locationProvider.showTrackingForeground();
    }

    private void trackingStopped() {
        locationProvider.hideTrackingForeground();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!trackingState.isTracking())
            locationProvider.stopService();
        compositeDisposable.dispose();
    }

    public void startLocationUpdates() {
        startTrackUpdates();
    }
}

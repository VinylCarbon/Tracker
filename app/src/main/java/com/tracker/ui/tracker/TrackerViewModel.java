package com.tracker.ui.tracker;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import javax.inject.Inject;


public class TrackerViewModel extends ViewModel {

    @NonNull
    private final MutableLiveData<TrackingState> trackingStateLiveData = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<TrackPoint> trackPointLiveData = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<Track> trackLiveData = new MutableLiveData<>();

    @Inject
    TrackerViewModel() {
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

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}

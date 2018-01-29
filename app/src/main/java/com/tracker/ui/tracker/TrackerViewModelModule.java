package com.tracker.ui.tracker;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.tracker.di.qualifiers.ForApplication;
import com.tracker.ui.trackdetail.TrackDetailViewModel;
import com.tracker.ui.tracker.LocationProvider;
import com.tracker.ui.tracker.ReactiveGpsLocationProvider;
import com.tracker.ui.tracker.TrackerViewModel;
import com.tracker.ui.tracks.TracksViewModel;
import com.tracker.ui.util.ViewModelUtil;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TrackerViewModelModule {

    @Singleton
    @Provides
    @Named("TrackerViewModel")
    static ViewModelProvider.Factory trackerViewModelProviderFactory(ViewModelUtil viewModelUtil,
                                                              TrackerViewModel trackerViewModel) {
        return viewModelUtil.createFor(trackerViewModel);
    }

    @Singleton
    @Provides
    LocationProvider locationProvider(@NonNull @ForApplication Context context) {
        return new ReactiveGpsLocationProvider(context);
    }
}

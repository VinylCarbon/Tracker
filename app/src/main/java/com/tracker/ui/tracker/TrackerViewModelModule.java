package com.tracker.ui.tracker;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.tracker.di.qualifiers.ForApplication;
import com.tracker.ui.util.ViewModelUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TrackerViewModelModule {

    @Singleton
    @Provides
    static ViewModelProvider.Factory viewModelProviderFactory(ViewModelUtil viewModelUtil,
                                                              TrackerViewModel trackerViewModel) {
        return viewModelUtil.createFor(trackerViewModel);
    }

    @Singleton
    @Provides
    LocationProvider locationProvider(@NonNull @ForApplication Context context) {
        return new MockLocationProvider(context);
    }
}

package com.tracker.ui.tracker;

import android.arch.lifecycle.ViewModelProvider;

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
}

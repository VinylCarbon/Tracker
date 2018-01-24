package com.tracker.ui.tracker;

import com.tracker.di.scope.FragmentScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class TrackerModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract TrackerFragment trackerFragment();
}

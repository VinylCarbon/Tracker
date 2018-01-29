package com.tracker.ui.trackdetail;

import com.tracker.di.scope.FragmentScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class TrackDetailModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract TrackDetailFragment trackDetailFragment();
}

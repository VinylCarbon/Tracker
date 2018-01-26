package com.tracker.di.module;

import com.tracker.di.scope.ActivityScope;
import com.tracker.ui.home.HomeActivity;
import com.tracker.ui.tracker.TrackerModule;
import com.tracker.ui.tracks.TracksActivity;
import com.tracker.ui.tracks.TracksModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = {TrackerModule.class})
    abstract HomeActivity homeActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {TracksModule.class})
    abstract TracksActivity tracksActivity();

}

package com.tracker.di.module;

import com.tracker.di.scope.ActivityScope;
import com.tracker.ui.home.HomeActivity;
import com.tracker.ui.tracker.TrackerModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = {TrackerModule.class})
    abstract HomeActivity homeActivity();

}

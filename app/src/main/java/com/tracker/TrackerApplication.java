package com.tracker;

import com.tracker.di.AppComponent;
import com.tracker.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;


public class TrackerApplication extends DaggerApplication {

    private static AppComponent appComponent;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }

    public static AppComponent appComponent() {
        return appComponent;
    }
}

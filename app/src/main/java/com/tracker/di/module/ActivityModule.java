package com.tracker.di.module;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.tracker.di.qualifiers.ForActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    @NonNull
    private final Activity activity;

    public ActivityModule(@NonNull final Activity activity) {
        this.activity = activity;
    }

    @ForActivity
    @Provides
    Context provideContext() {
        return activity;
    }
}

package com.tracker.di.module;

import android.app.Application;
import android.content.Context;

import com.tracker.di.qualifiers.ForApplication;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @ForApplication
    @Provides
    Context provideApplicationContext(Application app) {
        return app.getApplicationContext();
    }
}

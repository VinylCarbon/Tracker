package com.tracker.di.module;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.location.LocationRequest;
import com.tracker.di.qualifiers.ForApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;
import pl.charmas.android.reactivelocation2.ReactiveLocationProviderConfiguration;

@Module
public class LocationModule {

    @Singleton
    @Provides
    LocationRequest locationRequest() {
        return LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(100);
    }

    @Singleton
    @Provides
    ReactiveLocationProvider locationProvider(@NonNull @ForApplication Context context) {
        return new ReactiveLocationProvider(context,
                ReactiveLocationProviderConfiguration.builder()
                        .setRetryOnConnectionSuspended(true)
                        .build());
    }
}

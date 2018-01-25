package com.tracker.data.tracker;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class TrackerDataModule {

    @Singleton
    @Binds
    abstract TrackRepository trackRepository(DbTrackRepository dbTrackRepository);
}

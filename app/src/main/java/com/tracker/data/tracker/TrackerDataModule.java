package com.tracker.data.tracker;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TrackerDataModule {

    @Singleton
    @Provides
    TrackRepository trackRepository(){
        return new DbTrackRepository();
    }
}

package com.tracker.data.tracker;

import android.support.annotation.NonNull;

import com.tracker.data.tracker.db.TrackDao;
import com.tracker.data.tracker.db.TrackPointDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TrackerDataModule {

    @Singleton
    @Provides
    TrackRepository trackRepository(@NonNull TrackDao trackDao, @NonNull TrackPointDao trackPointDao) {
        return new DbTrackRepository(trackDao, trackPointDao);
    }
}

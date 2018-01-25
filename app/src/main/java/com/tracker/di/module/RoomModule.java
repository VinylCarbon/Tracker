package com.tracker.di.module;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import com.tracker.BuildConfig;
import com.tracker.data.TrackerDatabase;
import com.tracker.data.tracker.db.TrackDao;
import com.tracker.data.tracker.db.TrackPointDao;
import com.tracker.di.qualifiers.ForApplication;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {
    private static final String DB_NAME = "DB_NAME";

    @Provides
    @Named(DB_NAME)
    static String dbName() {
        return BuildConfig.DB_NAME;
    }

    @Singleton
    @Provides
    TrackerDatabase trackerDatabase(@NonNull @ForApplication Context context, @Named(DB_NAME) String dbName) {
        return Room.databaseBuilder(context, TrackerDatabase.class, dbName).build();
    }

    @Singleton
    @Provides
    TrackDao trackDao(@NonNull TrackerDatabase trackerDatabase) {
        return trackerDatabase.trackDao();
    }

    @Singleton
    @Provides
    TrackPointDao trackPointDao(@NonNull TrackerDatabase trackerDatabase) {
        return trackerDatabase.trackPointDao();
    }
}

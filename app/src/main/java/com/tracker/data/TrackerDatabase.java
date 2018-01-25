package com.tracker.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.tracker.BuildConfig;
import com.tracker.data.tracker.db.TrackDao;
import com.tracker.data.tracker.db.TrackPointDao;
import com.tracker.data.tracker.db.TrackPointRaw;
import com.tracker.data.tracker.db.TrackRaw;

@Database(entities = {TrackRaw.class, TrackPointRaw.class}, version = BuildConfig.DB_VERSION)
public abstract class TrackerDatabase extends RoomDatabase {

    public abstract TrackDao trackDao();

    public abstract TrackPointDao trackPointDao();
}

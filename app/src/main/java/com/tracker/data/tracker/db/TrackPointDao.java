package com.tracker.data.tracker.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface TrackPointDao {

    @Insert
    void insert(TrackPointRaw trackPointRaw);

    @Query("SELECT * FROM TRACKPOINTRAW WHERE trackId = :trackId")
    Flowable<List<TrackPointRaw>> allTrackPoints(long trackId);
}

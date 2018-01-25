package com.tracker.data.tracker.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TrackPointDao {

    @Insert
    void insert(TrackPointRaw... trackPointRaws);

    @Query("SELECT * FROM TRACKPOINTRAW WHERE trackId = :trackId")
    List<TrackPointRaw> allTrackPoints(long trackId);
}

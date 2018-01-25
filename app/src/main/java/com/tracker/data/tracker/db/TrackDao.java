package com.tracker.data.tracker.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TrackDao {

    @Insert
    void insert(TrackRaw... trackRaws);

    @Update
    void update(TrackRaw... trackRaws);

    @Delete
    void delete(TrackRaw... trackRaws);

    @Query("SELECT * FROM TRACKRAW WHERE finishTime = 0 LIMIT 1")
    TrackRaw activeTrack();

    @Query("SELECT * FROM TRACKRAW WHERE finishTime != 0")
    List<TrackRaw> allTracks();
}

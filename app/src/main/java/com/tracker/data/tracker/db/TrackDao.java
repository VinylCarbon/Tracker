package com.tracker.data.tracker.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface TrackDao {

    @Insert
    long insert(TrackRaw trackRaw);

    @Update
    void update(TrackRaw trackRaw);

    @Delete
    void delete(TrackRaw... trackRaws);

    @Query("SELECT * FROM TRACKRAW WHERE finishTime = 0 LIMIT 1")
    Flowable<TrackRaw> activeTrack();

    @Query("SELECT * FROM TRACKRAW WHERE finishTime != 0")
    Flowable<List<TrackRaw>> allTracks();
}

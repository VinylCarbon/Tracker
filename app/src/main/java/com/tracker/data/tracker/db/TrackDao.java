package com.tracker.data.tracker.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

@Dao
public interface TrackDao {

    @Insert
    void insert(TrackRaw... trackRaws);

    @Update
    void update(TrackRaw... trackRaws);

    @Delete
    void delete(TrackRaw... trackRaws);
}

package com.tracker.data.tracker.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class TrackRaw {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    private long startTime;

    private long finishTime;

    public TrackRaw() {
    }

    private TrackRaw(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setStartTime(builder.startTime);
        setFinishTime(builder.finishTime);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }


    public static final class Builder {
        private long id;
        private String name;
        private long startTime;
        private long finishTime;

        public Builder() {
        }

        public Builder id(long val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder startTime(long val) {
            startTime = val;
            return this;
        }

        public Builder finishTime(long val) {
            finishTime = val;
            return this;
        }

        public TrackRaw build() {
            return new TrackRaw(this);
        }
    }
}

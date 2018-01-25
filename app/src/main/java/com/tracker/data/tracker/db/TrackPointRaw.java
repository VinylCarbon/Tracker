package com.tracker.data.tracker.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = TrackRaw.class,
        parentColumns = "id",
        childColumns = "trackId",
        onDelete = CASCADE))
public class TrackPointRaw {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private double latitude;

    private double longitude;

    private long timestamp;

    private long trackId;

    public TrackPointRaw() {
    }

    private TrackPointRaw(Builder builder) {
        setId(builder.id);
        setLatitude(builder.latitude);
        setLongitude(builder.longitude);
        setTimestamp(builder.timestamp);
        setTrackId(builder.trackId);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }

    public static final class Builder {
        private long id;
        private double latitude;
        private double longitude;
        private long timestamp;
        private long trackId;

        public Builder() {
        }

        public Builder id(long val) {
            id = val;
            return this;
        }

        public Builder latitude(double val) {
            latitude = val;
            return this;
        }

        public Builder longitude(double val) {
            longitude = val;
            return this;
        }

        public Builder timestamp(long val) {
            timestamp = val;
            return this;
        }

        public Builder trackId(long val) {
            trackId = val;
            return this;
        }

        public TrackPointRaw build() {
            return new TrackPointRaw(this);
        }
    }
}

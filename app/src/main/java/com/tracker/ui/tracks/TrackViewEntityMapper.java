package com.tracker.ui.tracks;

import android.support.annotation.NonNull;

import com.tracker.R;
import com.tracker.common.utils.TimeUtils;
import com.tracker.data.tracker.Track;
import com.tracker.ui.providers.StringProvider;

import javax.inject.Inject;

import io.reactivex.functions.Function;

class TrackViewEntityMapper implements Function<Track, TrackViewEntity> {

    private final String TRACK_DATE_FORMAT_CREATED_ON = "MMM dd yy";
    private final String TRACK_TIME_FORMAT = "HH:mm a";

    @Inject
    StringProvider stringProvider;
    @Inject
    TimeUtils timeUtils;

    @Inject
    TrackViewEntityMapper() {
    }

    @Override
    public TrackViewEntity apply(Track track) throws Exception {
        return TrackViewEntity.builder()
                .id(track.id())
                .name(track.name())
                .formattedCreatedOn(mapToFormattedCreatedOn(track.startTime()))
                .formattedStartTime(mapToFormattedTime(track.startTime()))
                .formattedFinishTime(mapToFormattedFinishTime(track.startTime()))
                .formattedDistance(mapToFormattedDistance(track))
                .formattedSpeed(mapToFormattedSpeed(track))
                .build();
    }

    @NonNull
    private String mapToFormattedTime(long timeInMs) {
        return timeUtils.formatDateInMs(timeInMs, TRACK_TIME_FORMAT);
    }

    @NonNull
    private String mapToFormattedCreatedOn(long timeInMs) {
        return timeUtils.formatDateInMs(timeInMs, TRACK_DATE_FORMAT_CREATED_ON);
    }

    @NonNull
    private String mapToFormattedFinishTime(long timeInMs) {
        return timeInMs == 0 ? stringProvider.getString(R.string.tracking) : mapToFormattedTime(timeInMs);
    }

    @NonNull
    private String mapToFormattedDistance(Track track) {
        // TODO: calculate formattedDistance.
        return "";
    }

    @NonNull
    private String mapToFormattedSpeed(Track track) {
        // TODO: calculate speed.
        return null;
    }
}

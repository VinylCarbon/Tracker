package com.tracker.ui.tracks;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.tracker.R;
import com.tracker.common.utils.DistanceFormatter;
import com.tracker.common.utils.SpeedFormatter;
import com.tracker.common.utils.TimeUtils;
import com.tracker.data.tracker.Track;
import com.tracker.ui.providers.StringProvider;

import javax.inject.Inject;

import io.reactivex.functions.Function;

import static com.tracker.common.utils.DistanceFormatter.DistanceUnits.KILOMETERS;

public class TrackViewEntityMapper implements Function<Track, TrackViewEntity> {

    private final String TRACK_DATE_FORMAT_CREATED_ON = "MMM dd yy";
    private final String TRACK_TIME_FORMAT = "hh:mm a";

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
                .formattedTime(mapToEntityFormattedTime(track))
                .formattedStartTime(mapToFormattedTime(track.startTime()))
                .formattedFinishTime(mapToFormattedFinishTime(track.startTime()))
                .formattedDistance(mapToFormattedDistance(track))
                .formattedSpeed(mapToFormattedSpeed(track))
                .build();
    }

    @NonNull
    private String mapToEntityFormattedTime(@NonNull final Track track) {
        return stringProvider.getStringAndApplySubstitutions(R.string.track_formatted_time,
                Pair.create("from", mapToFormattedTime(track.startTime())),
                Pair.create("to", mapToFormattedFinishTime(track.finishTime())));
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
        if (track.distanceInMeter() == 0)
            return "";

        return stringProvider.getStringAndApplySubstitutions(R.string.track_formatted_distance,
                Pair.create("distance", DistanceFormatter.format((int) track.distanceInMeter(), KILOMETERS)));
    }

    @NonNull
    private String mapToFormattedSpeed(Track track) {
        if (track.distanceInMeter() == 0)
            return "";
        return stringProvider.getStringAndApplySubstitutions(R.string.track_formatted_speed,
                Pair.create("speed", formattedSpeed(track)));
    }

    private String formattedSpeed(Track track) {
        return SpeedFormatter.format(track.distanceInMeter(), (track.finishTime() - track.startTime()) * 1000);
    }
}

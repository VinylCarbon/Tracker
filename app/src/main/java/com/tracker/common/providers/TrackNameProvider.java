package com.tracker.common.providers;

import com.tracker.common.utils.TimeUtils;

import javax.inject.Inject;

public class TrackNameProvider {

    private static final String NAME_DATE_FORMAT = "MMM dd yy";
    private TimeUtils timeUtils;

    @Inject
    TrackNameProvider(TimeUtils timeUtils) {
        this.timeUtils = timeUtils;
    }


    public String name() {
        return "Track - " + timeUtils.formatDateInMs(System.currentTimeMillis(), NAME_DATE_FORMAT);
    }
}

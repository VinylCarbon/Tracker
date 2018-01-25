package com.tracker.common.providers;

import javax.inject.Inject;

public class TrackNameProvider {

    @Inject
    TrackNameProvider() {
    }


    public String name() {
        return "Track - " + System.currentTimeMillis();
    }
}

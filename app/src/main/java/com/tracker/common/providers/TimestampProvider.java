package com.tracker.common.providers;

import javax.inject.Inject;

public class TimestampProvider {

    @Inject
    TimestampProvider(){}

    public long currentTimeMillis(){
        return System.currentTimeMillis();
    }
}

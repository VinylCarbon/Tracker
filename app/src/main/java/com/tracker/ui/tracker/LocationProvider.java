package com.tracker.ui.tracker;

public interface LocationProvider {

    void startService();

    void showTrackingForeground();

    void stopService();

    void hideTrackingForeground();
}

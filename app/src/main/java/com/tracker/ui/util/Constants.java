package com.tracker.ui.util;

public interface Constants {

    interface ACTION {
        String MAIN_ACTION = "com.tracker.ui.tracker.TrackerService.action.main";

        String START_TRACKER_ACTION = "com.tracker.ui.tracker.TrackerService.action.starttracker";
        String STOP_TRACKER_ACTION = "com.tracker.ui.tracker.TrackerService.action.stoptracker";

        String START_TRACKER_ACTION_FOREGROUND = "com.tracker.ui.tracker.TrackerService.action.starttracker_foreground";
        String STOP_TRACKER_ACTION_FOREGROUND = "com.tracker.ui.tracker.TrackerService.action.stoptracker_foreground";

        String FINISH_TRACKING_ACTION = "com.tracker.ui.tracker.TrackerService.action.finishtracking";
    }

    interface NOTIFICATION_ID {
        int TRACKER_SERVICE = 101;
    }
}

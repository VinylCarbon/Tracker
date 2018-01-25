package com.tracker.ui.tracker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

class MockLocationProvider implements LocationProvider {
    private final Context context;

    MockLocationProvider(@NonNull final Context context) {
        this.context = context;
    }

    @Override
    public void startService() {
        context.startService(new Intent(context, TrackerService.class));
    }
}

package com.tracker.ui.tracker;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.location.LocationRequest;
import com.tracker.TrackerApplication;
import com.tracker.common.providers.TimestampProvider;
import com.tracker.data.tracker.TrackPoint;
import com.tracker.data.tracker.TrackRepository;

import javax.inject.Inject;

import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;

public class TrackerService extends Service {
    @Inject
    ReactiveLocationProvider reactiveLocationProvider;
    @Inject
    LocationRequest locationRequest;
    @Inject
    TrackRepository trackRepository;
    @Inject
    TimestampProvider timestampProvider;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();

        TrackerApplication.appComponent().inject(this);
        reactiveLocationProvider.getUpdatedLocation(locationRequest).subscribe(this::gotUpdatedLocation);
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void gotUpdatedLocation(Location location) {
        trackRepository.addTrackPoint(createTrackPoint(location));
    }

    private TrackPoint createTrackPoint(Location location) {
        return TrackPoint.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .timestamp(timestampProvider.currentTimeMillis()).build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

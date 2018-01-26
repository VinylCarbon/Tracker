package com.tracker.ui.tracker;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.location.LocationRequest;
import com.tracker.R;
import com.tracker.TrackerApplication;
import com.tracker.common.providers.TimestampProvider;
import com.tracker.data.tracker.TrackPoint;
import com.tracker.data.tracker.TrackRepository;
import com.tracker.ui.home.HomeActivity;
import com.tracker.ui.util.Constants;
import com.tracker.ui.util.Constants.ACTION;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;

import static com.tracker.ui.util.Constants.ACTION.FINISH_TRACKING_ACTION;
import static com.tracker.ui.util.Constants.ACTION.START_TRACKER_ACTION_FOREGROUND;
import static com.tracker.ui.util.Constants.ACTION.STOP_TRACKER_ACTION;
import static com.tracker.ui.util.Constants.ACTION.STOP_TRACKER_ACTION_FOREGROUND;

public class TrackerService extends Service {
    private static final String LOG_TAG = TrackerService.class.getSimpleName();
    @Inject
    ReactiveLocationProvider reactiveLocationProvider;
    @Inject
    LocationRequest locationRequest;
    @Inject
    TrackRepository trackRepository;
    @Inject
    TimestampProvider timestampProvider;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();

        TrackerApplication.appComponent().inject(this);
        compositeDisposable.add(
                reactiveLocationProvider
                        .getUpdatedLocation(locationRequest)
                        .subscribe(this::gotUpdatedLocation));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(STOP_TRACKER_ACTION)) {
                stopLocationUpdates();
                stopSelf();
            } else if (intent.getAction().equals(START_TRACKER_ACTION_FOREGROUND)) {
                showNotification();
            } else if (intent.getAction().equals(STOP_TRACKER_ACTION_FOREGROUND)) {
                stopForeground(true);
            } else if (intent.getAction().equals(FINISH_TRACKING_ACTION)) {
                finishTracking();
                stopForeground(true);
            }
        }
        return START_STICKY;
    }

    private void stopLocationUpdates() {
        reactiveLocationProvider.removeLocationUpdates(PendingIntent.getService(this, 1,
                new Intent(this, TrackerService.class), 0)).subscribe();
        compositeDisposable.dispose();
    }

    private void finishTracking() {
        trackRepository.setTrackingState(false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private void gotUpdatedLocation(Location location) {
        trackRepository.addTrackPoint(createTrackPoint(location))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private TrackPoint createTrackPoint(Location location) {
        return TrackPoint.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .timestamp(timestampProvider.currentTimeMillis()).build();
    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, HomeActivity.class);
        notificationIntent.setAction(ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent finishTrackIntent = new Intent(this, TrackerService.class);
        finishTrackIntent.setAction(FINISH_TRACKING_ACTION);
        PendingIntent pendingStopIntent = PendingIntent.getService(this, 0, finishTrackIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher_round);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Tracker")
                .setTicker("Tracker")
                .setContentText("Tracking path")
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .addAction(R.drawable.stop, "Stop Tracking", pendingStopIntent)
                .build();

        startForeground(Constants.NOTIFICATION_ID.TRACKER_SERVICE, notification);
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

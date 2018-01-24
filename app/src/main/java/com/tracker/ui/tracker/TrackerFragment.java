package com.tracker.ui.tracker;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tracker.R;
import com.tracker.data.tracker.Track;
import com.tracker.data.tracker.TrackPoint;
import com.tracker.data.tracker.TrackingState;
import com.tracker.ui.base.BaseFragment;

import javax.inject.Inject;

import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class TrackerFragment extends BaseFragment {
    private static final int REQUEST_CHECK_SETTINGS = 1001;
    private static final String TAG = TrackerFragment.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    LocationRequest locationRequest;
    @Inject
    ReactiveLocationProvider locationProvider;

    private TrackerViewModel trackerViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_tracker;
    }

    @Inject
    public TrackerFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        trackerViewModel = ViewModelProviders.of(this, viewModelFactory).get(TrackerViewModel.class);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        new RxPermissions(getActivity()).request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        onLocationPermissionGranted();
                    } else {
                        showLocationPermissionDenied();
                    }
                });
    }

    private void onLocationPermissionGranted() {
        checkLocationSettings();
    }

    private void checkLocationSettings() {
        locationProvider.checkLocationSettings(
                new LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                        .setAlwaysShow(true)
                        .build())
                .subscribe(locationSettingsResult -> {
                    Status status = locationSettingsResult.getStatus();
                    if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException th) {
                            Log.e(TAG, "Error opening settings activity.", th);
                        }
                    }
                });
    }

    private void showLocationPermissionDenied() {
        showUserCancelledLoactionAccess();
        checkLocationPermission();
    }

    void showTrackingState(TrackingState trackingState) {
    }

    void showTrackPoint(TrackPoint trackPoint) {
    }

    void showTrack(Track track) {
    }

    void startTracking() {
        trackerViewModel.startTracking();
    }

    void stopTracking() {
        trackerViewModel.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        break;
                    case RESULT_CANCELED:
                        showUserCancelledLoactionAccess();
                        checkLocationSettings();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void showUserCancelledLoactionAccess() {
        Toast.makeText(getActivity(), "No demo without location permissions :(",
                Toast.LENGTH_LONG).show();
    }
}

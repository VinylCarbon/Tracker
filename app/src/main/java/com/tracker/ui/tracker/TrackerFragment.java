package com.tracker.ui.tracker;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tracker.R;
import com.tracker.data.tracker.Track;
import com.tracker.data.tracker.TrackPoint;
import com.tracker.data.tracker.TrackingState;
import com.tracker.ui.base.BaseFragment;
import com.tracker.utils.ActivityUtils;

import javax.inject.Inject;

import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
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

    GoogleMap map;
    private SupportMapFragment mapFragment;

    private Button startTracking;
    private Button finishTracking;

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
        observeTrackingDetails();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        startTracking = view.findViewById(R.id.start_tracking);
        finishTracking = view.findViewById(R.id.finish_tracking);
        startTracking.setOnClickListener(__ -> startTracking());
        finishTracking.setOnClickListener(__ -> finishTracking());
        mapFragment.getMapAsync(this::configureMap);
    }

    private void observeTrackingDetails() {
        trackerViewModel.trackingState().observe(this, this::showTrackingState);
        trackerViewModel.track().observe(this, this::showTrack);
        trackerViewModel.trackPoint().observe(this, this::showTrackPoint);
    }

    private void configureMap(GoogleMap map) {
        this.map = map;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(false);
        map.getUiSettings().setRotateGesturesEnabled(false);
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        enableMapMyLocation();
    }

    @Override
    public void onStart() {
        super.onStart();
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        new RxPermissions(getActivity()).request(ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        onLocationPermissionGranted();
                    } else {
                        showLocationPermissionDenied();
                    }
                });
    }

    @SuppressLint("MissingPermission")
    private void onLocationPermissionGranted() {
        checkLocationSettings();
        enableMapMyLocation();
        trackerViewModel.startLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void enableMapMyLocation() {
        if (map != null && ActivityUtils.isLocationPermissionGranted(getActivity()))
            map.setMyLocationEnabled(true);
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
        showUserCancelledLocationAccess();
        checkLocationPermission();
    }

    void showTrackingState(TrackingState trackingState) {
        if (trackingState.isTracking()) showTracking();
        else showNotTracking();
    }

    private void showTracking() {
        startTracking.setVisibility(View.GONE);
        finishTracking.setVisibility(View.VISIBLE);
    }

    private void showNotTracking() {
        startTracking.setVisibility(View.VISIBLE);
        finishTracking.setVisibility(View.GONE);

        if (map != null) map.clear();
    }

    void showTrackPoint(TrackPoint trackPoint) {
        if (map == null) return;
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(trackPoint.latitude(), trackPoint.longitude())));
        map.animateCamera(CameraUpdateFactory.zoomTo(17));
    }

    void showTrack(Track track) {
        if (map == null) return;
        map.clear();
        map.addPolyline(polylineOptions(track));
    }

    private PolylineOptions polylineOptions(Track track) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(Color.BLUE)
                .width(5)
                .visible(true)
                .zIndex(30)
                .geodesic(true);
        for (TrackPoint trackPoint : track.trackPoints())
            polylineOptions.add(new LatLng(trackPoint.latitude(), trackPoint.longitude()));
        return polylineOptions;
    }

    void startTracking() {
        trackerViewModel.startTracking();
    }

    void finishTracking() {
        trackerViewModel.finishTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        break;
                    case RESULT_CANCELED:
                        showUserCancelledLocationAccess();
                        checkLocationSettings();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void showUserCancelledLocationAccess() {
        Toast.makeText(getActivity(), "No demo without location permissions :(",
                Toast.LENGTH_LONG).show();
    }
}

package com.tracker.ui.trackdetail;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tracker.R;
import com.tracker.data.tracker.Track;
import com.tracker.data.tracker.TrackPoint;
import com.tracker.ui.base.BaseFragment;
import com.tracker.utils.ActivityUtils;

import javax.inject.Inject;
import javax.inject.Named;

public class TrackDetailFragment extends BaseFragment {
    public static final String EXTRA_TRACK_ID = "EXTRA_TRACK_ID";
    @Inject
    @Named("TrackDetailViewModel")
    ViewModelProvider.Factory viewModelFactory;
    private TrackDetailViewModel trackDetailViewModel;

    GoogleMap map;
    private SupportMapFragment mapFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_track_detail;
    }

    @Inject
    public TrackDetailFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        trackDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(TrackDetailViewModel.class);
        observeTrackingDetails();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::configureMap);
    }

    private void observeTrackingDetails() {
//        trackDetailViewModel.
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

    @SuppressLint("MissingPermission")
    private void enableMapMyLocation() {
        if (map != null && ActivityUtils.isLocationPermissionGranted(getActivity()))
            map.setMyLocationEnabled(true);
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
}

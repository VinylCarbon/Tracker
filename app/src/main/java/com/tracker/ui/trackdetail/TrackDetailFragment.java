package com.tracker.ui.trackdetail;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tracker.R;
import com.tracker.data.tracker.TrackPoint;
import com.tracker.ui.base.BaseFragment;
import com.tracker.ui.tracks.TrackViewEntity;
import com.tracker.ui.util.ViewModelUtil;
import com.tracker.utils.ActivityUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackDetailFragment extends BaseFragment {
    public static final String EXTRA_TRACK_ID = "EXTRA_TRACK_ID";

    @Inject
    ViewModelUtil viewModelUtil;
    @Inject
    TrackDetailViewModel detailViewModel;

    private TrackDetailViewModel trackDetailViewModel;

    private GoogleMap map;
    private SupportMapFragment mapFragment;

    @BindView(R.id.track_name)
    TextView trackName;
    @BindView(R.id.track_time)
    TextView trackTime;
    @BindView(R.id.track_distance)
    TextView trackDistance;
    @BindView(R.id.track_speed)
    TextView trackSpeed;

    private long trackId;

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
        trackDetailViewModel = ViewModelProviders.of(this, viewModelUtil.createFor(detailViewModel)).get(TrackDetailViewModel.class);
        observeTrackingDetails();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trackId = getArguments().getLong(EXTRA_TRACK_ID);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::configureMap);
        ButterKnife.bind(this, view);
    }

    private void observeTrackingDetails() {
        trackDetailViewModel.bindTrack(trackId);
        trackDetailViewModel.track().observe(this, this::showTrackDetails);
        trackDetailViewModel.trackPath().observe(this, this::showTrackPath);
    }

    private void showTrackDetails(TrackViewEntity viewEntity) {
        trackName.setText(viewEntity.name());
        trackTime.setText(viewEntity.formattedTime());
        trackDistance.setText(viewEntity.formattedDistance());
        trackSpeed.setText(viewEntity.formattedSpeed());
    }

    private void showTrackPath(List<TrackPoint> trackPoints) {
        if (map == null) return;

        map.clear();
        showTrackPoints(trackPoints);
        map.addPolyline(polylineOptions(trackPoints));
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

    void showTrackPoints(List<TrackPoint> trackPoints) {
        if (map == null) return;
        if (trackPoints.isEmpty())
            return;

        try {
            LatLngBounds latLngBounds = new LatLngBounds(
                    latLng(trackPoints.get(0)),
                    latLng(trackPoints.get(trackPoints.size() - 1)));
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10));
            map.animateCamera(CameraUpdateFactory.zoomTo(15));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void enableMapMyLocation() {
        if (map != null && ActivityUtils.isLocationPermissionGranted(getActivity()))
            map.setMyLocationEnabled(true);
    }

    private PolylineOptions polylineOptions(List<TrackPoint> trackPoints) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(Color.BLUE)
                .width(5)
                .visible(true)
                .zIndex(30)
                .geodesic(true);
        for (TrackPoint trackPoint : trackPoints)
            polylineOptions.add(latLng(trackPoint));
        return polylineOptions;
    }

    @NonNull
    private LatLng latLng(@NonNull final TrackPoint trackPoint) {
        return new LatLng(trackPoint.latitude(), trackPoint.longitude());
    }
}

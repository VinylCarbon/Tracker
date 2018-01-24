package com.tracker.ui.tracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tracker.R;
import com.tracker.data.tracker.Track;
import com.tracker.data.tracker.TrackPoint;
import com.tracker.data.tracker.TrackingState;
import com.tracker.ui.base.BaseFragment;

import javax.inject.Inject;

public class TrackerFragment extends BaseFragment {

    TrackerViewModel viewModel;

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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    void showTrackingState(TrackingState trackingState) {
    }

    void showTrackPoint(TrackPoint trackPoint) {
    }

    void showTrack(Track track) {
    }

    void startTracking() {
        viewModel.startTracking();
    }

    void stopTracking() {
        viewModel.stopTracking();
    }
}

package com.tracker.ui.tracks;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tracker.R;
import com.tracker.ui.base.BaseFragment;
import com.tracker.ui.recyclerview.DisplayableItem;
import com.tracker.ui.recyclerview.RecyclerViewAdapter;
import com.tracker.ui.util.ViewModelUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TracksFragment extends BaseFragment {

    @Inject
    RecyclerViewAdapter adapter;
    @Inject
    ViewModelUtil viewModelUtil;
    @Inject
    TracksViewModel tracksViewModel;


    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.tracks)
    RecyclerView trackList;
    @BindView(R.id.empty_tracks)
    View emptyTracks;


    @Inject
    public TracksFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_tracks;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TracksViewModel viewModel = ViewModelProviders.of(this, viewModelUtil.createFor(tracksViewModel))
                .get(TracksViewModel.class);
        viewModel.getTrackListLiveData().observe(this, this::updatedTracks);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        refresh.setRefreshing(true);
        refresh.setOnRefreshListener(this::refreshTracks);
        configureRecyclerView();
    }

    private void refreshTracks() {
    }

    private void configureRecyclerView() {
        trackList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        trackList.setAdapter(adapter);
    }

    private void updatedTracks(@NonNull final List<DisplayableItem> displayableItems) {
        refresh.setRefreshing(false);
        adapter.update(displayableItems);
        if (displayableItems.isEmpty())
            showEmptyTracks();
        else showTracks();
    }

    private void showTracks() {
        trackList.setVisibility(VISIBLE);
        emptyTracks.setVisibility(GONE);
    }

    private void showEmptyTracks() {
        trackList.setVisibility(GONE);
        emptyTracks.setVisibility(VISIBLE);
    }
}

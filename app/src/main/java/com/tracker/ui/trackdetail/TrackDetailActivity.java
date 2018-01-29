package com.tracker.ui.trackdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tracker.R;
import com.tracker.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Lazy;
import polanski.option.Option;

import static com.tracker.utils.ActivityUtils.addFragmentToActivity;

public class TrackDetailActivity extends BaseActivity {
    public static final String EXTRA_TRACK_ID = "EXTRA_TRACK_ID";
    public static final String EXTRA_TRACK_NAME = "EXTRA_TRACK_NAME";

    @Inject
    Lazy<TrackDetailFragment> trackDetailFragmentLazy;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Option.ofObj(savedInstanceState).ifNone(this::showTrackDetailFragment);
        init();
    }

    private void init() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getIntent().getStringExtra(EXTRA_TRACK_NAME));
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_container;
    }

    private void showTrackDetailFragment() {
        TrackDetailFragment trackDetailFragment = trackDetailFragmentLazy.get();
        trackDetailFragment.setArguments(bundleWithTrackId(getIntent().getLongExtra(EXTRA_TRACK_ID, 0)));
        addFragmentToActivity(getSupportFragmentManager(), trackDetailFragment, R.id.container);
    }

    private Bundle bundleWithTrackId(long trackId) {
        Bundle bundle = new Bundle();
        bundle.putLong(TrackDetailFragment.EXTRA_TRACK_ID, trackId);
        return bundle;
    }

}

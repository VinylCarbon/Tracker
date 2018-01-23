package com.tracker.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tracker.R;
import com.tracker.ui.base.BaseActivity;
import com.tracker.ui.tracker.TrackerFragment;

import javax.inject.Inject;

import dagger.Lazy;
import polanski.option.Option;

import static com.tracker.utils.ActivityUtils.addFragmentToActivity;


public class HomeActivity extends BaseActivity {

    @Inject
    Lazy<TrackerFragment> trackerFragmentLazy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Option.ofObj(savedInstanceState).ifNone(this::showMarketPriceChart);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    private void showMarketPriceChart() {
        addFragmentToActivity(getSupportFragmentManager(), trackerFragmentLazy.get(), R.id.container);
    }
}

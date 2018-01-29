package com.tracker.ui.tracks;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tracker.R;
import com.tracker.di.qualifiers.ForApplication;
import com.tracker.ui.recyclerview.DisplayableItem;
import com.tracker.ui.recyclerview.ViewHolderBinder;
import com.tracker.ui.recyclerview.ViewHolderFactory;
import com.tracker.ui.trackdetail.TrackDetailActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.track_name)
    TextView trackName;
    @BindView(R.id.track_time)
    TextView trackTime;
    @BindView(R.id.track_distance)
    TextView trackDistance;
    @BindView(R.id.track_speed)
    TextView trackSpeed;

    private TrackViewEntity viewEntity;

    public TrackViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this::trackItemSelected);
    }

    private void trackItemSelected(View view) {
        Intent trackDetailIntent = new Intent(view.getContext(), TrackDetailActivity.class);
        trackDetailIntent.putExtra(TrackDetailActivity.EXTRA_TRACK_ID, viewEntity.id());
        trackDetailIntent.putExtra(TrackDetailActivity.EXTRA_TRACK_NAME, viewEntity.name());
        view.getContext().startActivity(trackDetailIntent);
    }

    private void bind(@NonNull final TrackViewEntity viewEntity) {
        this.viewEntity = viewEntity;
        trackName.setText(viewEntity.name());
        trackTime.setText(viewEntity.formattedTime());
        trackDistance.setText(viewEntity.formattedDistance());
        trackSpeed.setText(viewEntity.formattedSpeed());
    }

    static class TrackHolderFactory extends ViewHolderFactory {

        @Inject
        TrackHolderFactory(@NonNull @ForApplication Context context) {
            super(context);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder createViewHolder(@NonNull final ViewGroup parent) {
            return new TrackViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_track, parent, false));
        }
    }

    static class TrackHolderBinder implements ViewHolderBinder {

        @Inject
        TrackHolderBinder() {
        }

        @Override
        public void bind(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull DisplayableItem item) {
            TrackViewHolder trackViewHolder = TrackViewHolder.class.cast(viewHolder);
            TrackViewEntity trackViewEntity = TrackViewEntity.class.cast(item.model());
            trackViewHolder.bind(trackViewEntity);
        }
    }

}

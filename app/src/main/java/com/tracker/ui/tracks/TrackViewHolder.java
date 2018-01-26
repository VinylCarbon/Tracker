package com.tracker.ui.tracks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tracker.R;
import com.tracker.di.qualifiers.ForApplication;
import com.tracker.ui.recyclerview.DisplayableItem;
import com.tracker.ui.recyclerview.ViewHolderBinder;
import com.tracker.ui.recyclerview.ViewHolderFactory;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class TrackViewHolder extends RecyclerView.ViewHolder {

    public TrackViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    private void bind(@NonNull final TrackViewEntity viewEntity) {
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
            TrackViewEntity trackViewEntity = TrackViewEntity.class.cast(item);
            trackViewHolder.bind(trackViewEntity);
        }
    }

}

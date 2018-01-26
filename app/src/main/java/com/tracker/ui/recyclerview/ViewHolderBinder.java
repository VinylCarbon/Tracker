package com.tracker.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.ViewHolder;

public interface ViewHolderBinder {

    /**
     Populates the passed {@link ViewHolder} with the details of the passed {@link DisplayableItem}.
     */
    void bind(@NonNull final ViewHolder viewHolder, @NonNull final DisplayableItem item);
}

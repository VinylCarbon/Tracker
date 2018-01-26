package com.tracker.ui.tracks;

import com.tracker.ui.recyclerview.DisplayableItem;
import com.tracker.ui.recyclerview.ItemComparator;

class TrackItemComarator implements ItemComparator {

    @Override
    public boolean areItemsTheSame(DisplayableItem item1, DisplayableItem item2) {
        return item1.equals(item2);
    }

    @Override
    public boolean areContentsTheSame(DisplayableItem item1, DisplayableItem item2) {
        return item1.equals(item2);
    }
}

package com.tracker.ui.tracks;

import com.tracker.common.preconditions.AndroidPreconditions;
import com.tracker.di.scope.FragmentScope;
import com.tracker.ui.recyclerview.ItemComparator;
import com.tracker.ui.recyclerview.RecyclerViewAdapter;
import com.tracker.ui.recyclerview.ViewHolderBinder;
import com.tracker.ui.recyclerview.ViewHolderFactory;
import com.tracker.ui.tracks.TrackViewHolder.TrackHolderBinder;
import com.tracker.ui.tracks.TrackViewHolder.TrackHolderFactory;

import java.util.Map;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntKey;
import dagger.multibindings.IntoMap;

import static com.tracker.ui.DisplayableTypes.TRACK_ITEM;

@Module
public abstract class TracksModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract TracksFragment tracksFragment();

    @Provides
    static RecyclerViewAdapter adapter(ItemComparator itemComparator,
                                       Map<Integer, ViewHolderFactory> factoryMap,
                                       Map<Integer, ViewHolderBinder> binderMap,
                                       AndroidPreconditions androidPreconditions) {
        return new RecyclerViewAdapter(itemComparator, factoryMap, binderMap, androidPreconditions);
    }

    @Provides
    static ItemComparator itemComparator() {
        return new TrackItemComarator();
    }

    @Binds
    @IntoMap
    @IntKey(TRACK_ITEM)
    abstract ViewHolderFactory viewHolderFactory(TrackHolderFactory trackHolderFactory);

    @Binds
    @IntoMap
    @IntKey(TRACK_ITEM)
    abstract ViewHolderBinder viewHolderBinder(TrackHolderBinder trackHolderBinder);
}

package com.tracker.data.cache;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class CacheEntry<T> {

    @NonNull
    abstract T cachedObject();

    abstract long creationTimestamp();

    static <T> Builder<T> builder() {
        return new AutoValue_CacheEntry.Builder<>();
    }

    @AutoValue.Builder
    interface Builder<T> {

        Builder<T> cachedObject(T object);

        Builder<T> creationTimestamp(long creationTimestamp);

        CacheEntry<T> build();
    }
}

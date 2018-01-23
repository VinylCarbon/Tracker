package com.tracker.data.store;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Maybe;

public interface Store<Key, Value> {

    void put(@NonNull final Value value);

    void putAll(@NonNull final List<Value> values);

    void clear();

    Maybe<Value> get(@NonNull final Key key);

    Maybe<List<Value>> getAll();


    interface MemoryStore<Key, Value> extends Store<Key, Value> {
    }

    interface DiskStore<Key, Value> extends Store<Key, Value> {
    }
}

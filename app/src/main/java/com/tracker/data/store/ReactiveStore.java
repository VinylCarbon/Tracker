package com.tracker.data.store;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import polanski.option.Option;

public interface ReactiveStore<Key, Value> {

    void put(@NonNull final Value value);

    void putAll(@NonNull final List<Value> values);

    void replaceAll(@NonNull final List<Value> values);

    Observable<Option<Value>> get(@NonNull final Key key);

    Observable<Option<List<Value>>> getAll();
}

package com.tracker.data.cache;

import android.support.annotation.NonNull;

import com.tracker.common.providers.TimestampProvider;
import com.tracker.common.utils.ListUtils;
import com.tracker.data.store.Store;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import polanski.option.Option;

import static polanski.option.Option.none;
import static polanski.option.Option.ofObj;

public class Cache<Key, Value> implements Store.MemoryStore<Key, Value> {

    @NonNull
    private final Function<Value, Key> extractKeyFromModel;

    @NonNull
    private final TimestampProvider timestampProvider;

    @NonNull
    private final Option<Long> itemLifespanMs;

    private final Map<Key, CacheEntry<Value>> cache = new ConcurrentHashMap<>();

    public Cache(@NonNull final Function<Value, Key> extractKeyFromModel,
                 @NonNull final TimestampProvider timestampProvider) {
        this(extractKeyFromModel, timestampProvider, none());
    }

    public Cache(@NonNull final Function<Value, Key> extractKeyFromModel,
                 @NonNull final TimestampProvider timestampProvider,
                 final long timeoutMs) {
        this(extractKeyFromModel, timestampProvider, ofObj(timeoutMs));
    }

    private Cache(@NonNull Function<Value, Key> extractKeyFromModel,
                  @NonNull TimestampProvider timestampProvider,
                  @NonNull Option<Long> itemLifespanMs) {
        this.extractKeyFromModel = extractKeyFromModel;
        this.timestampProvider = timestampProvider;
        this.itemLifespanMs = itemLifespanMs;
    }

    @Override
    public void put(@NonNull Value value) {
        Single.fromCallable(() -> extractKeyFromModel.apply(value))
                .subscribeOn(Schedulers.computation())
                .subscribe(key -> cache.put(key, createCacheEntry(value)));
    }

    @Override
    public void putAll(@NonNull List<Value> values) {
        Observable.fromIterable(values)
                .toMap(extractKeyFromModel, this::createCacheEntry)
                .subscribeOn(Schedulers.computation())
                .subscribe(cache::putAll);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public Maybe<Value> get(@NonNull Key key) {
        return Maybe.fromCallable(() -> cache.containsKey(key))
                .filter(isPresent -> isPresent)
                .map(__ -> cache.get(key))
                .filter(this::notExpired)
                .map(CacheEntry::cachedObject)
                .subscribeOn(Schedulers.computation());
    }

    @Override
    public Maybe<List<Value>> getAll() {
        return Observable.fromIterable(cache.values())
                .filter(this::notExpired)
                .map(CacheEntry::cachedObject)
                .toList()
                .filter(ListUtils::isNotEmpty)
                .subscribeOn(Schedulers.computation());
    }

    private CacheEntry<Value> createCacheEntry(Value value) {
        return CacheEntry.<Value>builder()
                .cachedObject(value)
                .creationTimestamp(timestampProvider.currentTimeMillis())
                .build();
    }

    private boolean notExpired(@NonNull CacheEntry<Value> cacheEntry) {
        return itemLifespanMs.match(lifespanMs -> cacheEntry.creationTimestamp() + lifespanMs > timestampProvider.currentTimeMillis(), () -> true);
    }
}

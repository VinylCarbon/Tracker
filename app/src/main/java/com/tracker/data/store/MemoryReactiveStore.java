package com.tracker.data.store;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import polanski.option.Option;
import polanski.option.function.Func1;

import static polanski.option.Option.none;
import static polanski.option.Option.ofObj;

public class MemoryReactiveStore<Key, Value> implements ReactiveStore<Key, Value> {

    @NonNull
    private final Store.MemoryStore<Key, Value> cache;

    @NonNull
    private final Func1<Value, Key> extractKeyFromModel;

    @NonNull
    private final Subject<Option<List<Value>>> allSubject;

    @NonNull
    private final Map<Key, Subject<Option<Value>>> subjectMap = new HashMap<>();

    public MemoryReactiveStore(@NonNull Store.MemoryStore<Key, Value> cache,
                               @NonNull Func1<Value, Key> extractKeyFromModel) {
        this.allSubject = PublishSubject.<Option<List<Value>>>create().toSerialized();
        this.cache = cache;
        this.extractKeyFromModel = extractKeyFromModel;
    }

    @Override
    public void put(@NonNull final Value value) {
        Key key = extractKeyFromModel.call(value);
        cache.put(value);
        getOrCreateSubjectForKey(key).onNext(ofObj(value));

        publishAll();
    }

    @Override
    public void putAll(@NonNull final List<Value> values) {
        cache.putAll(values);

        allSubject.onNext(ofObj(values));
        publishInEachKey();
    }

    @Override
    public void replaceAll(@NonNull final List<Value> values) {
        cache.clear();
        putAll(values);
    }

    @Override
    public Observable<Option<Value>> get(@NonNull final Key key) {
        return Observable.defer(() -> getOrCreateSubjectForKey(key))
                .startWith(getValue(key))
                .observeOn(Schedulers.computation());
    }

    @Override
    public Observable<Option<List<Value>>> getAll() {
        return Observable.defer(() -> allSubject.startWith(getAllValues()))
                .observeOn(Schedulers.computation());
    }

    private Subject<Option<Value>> getOrCreateSubjectForKey(@NonNull final Key key) {
        synchronized (subjectMap) {
            return ofObj(subjectMap.get(key)).orDefault(() -> createAndStoreNewSubjectKey(key));
        }
    }

    private void publishAll() {
        final Option<List<Value>> allValues = cache.getAll().map(Option::ofObj).blockingGet(none());
        allSubject.onNext(allValues);
    }

    private Subject<Option<Value>> createAndStoreNewSubjectKey(Key key) {
        final Subject<Option<Value>> processor = PublishSubject.<Option<Value>>create().toSerialized();
        synchronized (subjectMap) {
            subjectMap.put(key, processor);
        }
        return processor;
    }

    private void publishInEachKey() {
        final Set<Key> keySet;
        synchronized (subjectMap) {
            keySet = new HashSet<>(subjectMap.keySet());
        }
        for (Key key : keySet) {
            final Option<Value> value = cache.get(key).map(Option::ofObj).blockingGet(none());
            publishInKey(key, value);
        }
    }

    private void publishInKey(@NonNull final Key key, @NonNull final Option<Value> model) {
        final Subject<Option<Value>> processor;
        synchronized (subjectMap) {
            processor = subjectMap.get(key);
        }
        ofObj(processor).ifSome(it -> it.onNext(model));
    }

    private Option<Value> getValue(@NonNull final Key key) {
        return cache.get(key).map(Option::ofObj).blockingGet();
    }

    private Option<List<Value>> getAllValues() {
        return cache.getAll().map(Option::ofObj).blockingGet(none());
    }
}

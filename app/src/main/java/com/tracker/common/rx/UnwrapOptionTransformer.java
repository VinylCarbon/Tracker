package com.tracker.common.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import polanski.option.Option;
import polanski.option.OptionUnsafe;

public class UnwrapOptionTransformer<T> implements ObservableTransformer<Option<T>, T> {
    @Override
    public ObservableSource<T> apply(Observable<Option<T>> upstream) {
        return upstream.filter(Option::isSome).map(OptionUnsafe::getUnsafe);
    }

    public static <T> UnwrapOptionTransformer<T> create() {
        return new UnwrapOptionTransformer<>();
    }
}

package com.tracker.domain;

import javax.annotation.Nonnull;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import polanski.option.Option;

public interface ReactiveInteractor {

    interface SendInteractor<Params, Result> extends ReactiveInteractor {

        @Nonnull
        Single<Result> getSingle(@Nonnull final Option<Params> params);
    }

    interface RetrieveInteractor<Params, Result> extends ReactiveInteractor {

        @Nonnull
        Observable<Result> getBehaviourStream(@Nonnull final Option<Params> params);
    }

    interface RequestInteractor<Params, Result> extends ReactiveInteractor {

        @Nonnull
        Single<Result> getSingle(@Nonnull final Option<Params> params);
    }

    interface DeleteInteractor<Params, Result> extends ReactiveInteractor {

        @Nonnull
        Single<Result> getSingle(@Nonnull final Option<Params> params);
    }

    interface RefreshInteractor<Params> extends ReactiveInteractor {

        @Nonnull
        Completable getRefreshSingle(@Nonnull final Option<Params> params);
    }
}

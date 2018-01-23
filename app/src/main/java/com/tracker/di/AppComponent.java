package com.tracker.di;

import com.tracker.TrackerAppication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<TrackerAppication> {

    void inject(TrackerAppication trackerAppication);

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(TrackerAppication trackerAppication);

        AppComponent build();
    }
}

package com.tracker.di;

import android.app.Application;

import com.tracker.TrackerApplication;
import com.tracker.di.module.ActivityBindingModule;
import com.tracker.di.module.ApplicationModule;
import com.tracker.di.module.DataModule;
import com.tracker.di.module.LocationModule;
import com.tracker.di.module.RoomModule;
import com.tracker.di.module.ViewModelModule;
import com.tracker.ui.tracker.TrackerService;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, ApplicationModule.class, RoomModule.class,
        DataModule.class, ViewModelModule.class, LocationModule.class, ActivityBindingModule.class})
public interface AppComponent extends AndroidInjector<TrackerApplication> {

    void inject(TrackerApplication trackerApplication);

    void inject(TrackerService trackerService);

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}

package com.tracker.di.module;

import com.tracker.data.tracker.TrackerDataModule;

import dagger.Module;

@Module(includes = {TrackerDataModule.class})
public final class DataModule {
}

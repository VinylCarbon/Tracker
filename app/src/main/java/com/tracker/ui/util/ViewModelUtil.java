package com.tracker.ui.util;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

public class ViewModelUtil {

    @Inject
    ViewModelUtil() {
    }

    public <T extends ViewModel> ViewModelProvider.Factory createFor(@NonNull final T viewModel) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                if (modelClass.isAssignableFrom(viewModel.getClass()))
                    return (T) viewModel;
                throw new IllegalArgumentException("Unexpected viewModel class " + modelClass);
            }
        };
    }
}

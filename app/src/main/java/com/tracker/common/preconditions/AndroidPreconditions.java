package com.tracker.common.preconditions;

import android.os.Looper;

import javax.inject.Inject;

public class AndroidPreconditions {

    @Inject
    AndroidPreconditions() {
    }

    /**
     * Asserts that the current thread is a worker thread.
     */
    public void assertWorkerThread() {
        if (isMainThread()) {
            throw new IllegalStateException(
                    "This task must be run on a worker thread and not on the Main thread.");
        }
    }

    /**
     * Asserts that the current thread is the Main Thread.
     */
    public void assertUiThread() {
        if (!isMainThread()) {
            throw new IllegalStateException(
                    "This task must be run on the Main thread and not on a worker thread.");
        }
    }

    /**
     * Returns whether the current thread is the Android main thread
     *
     * @return true if the current thread is the main thread, otherwise; false.
     */
    public boolean isMainThread() {
        return Looper.getMainLooper().getThread().equals(Thread.currentThread());
    }
}

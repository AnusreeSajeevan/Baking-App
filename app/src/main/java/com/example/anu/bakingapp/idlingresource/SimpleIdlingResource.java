package com.example.anu.bakingapp.idlingresource;


import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;


public class SimpleIdlingResource implements IdlingResource {

    @Nullable private volatile ResourceCallback mResourceCallback;

    //ideleness id controlled by this boolean variable
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }
    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.mResourceCallback = callback;
    }

    /**
     * sets new idle state, if mIsIdle is true, it pings {@literal mResourceCallback}
     * @param isIdleNow will be true, if idle and will be false if there is no pending operations
     */
    public void setIdleState(boolean isIdleNow){
        mIsIdleNow.set(isIdleNow);
        if (isIdleNow && mResourceCallback != null){
            mResourceCallback.onTransitionToIdle();
        }
    }
}

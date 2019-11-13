package com.wistron.myapplication;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class ActivityLifeCycleObserver implements LifecycleObserver {
    public static final String TAG = "Alex";
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreateEvent(){
        Log.d(TAG, "Observer onCreateEvent");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroyEvent(){
        Log.d(TAG, "Observer onDestroyEvent");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStartEvent(){
        Log.d(TAG, "Observer onStartEvent");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPauseEvent(){
        Log.d(TAG, "Observer onPauseEvent");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResumeEvent(){
        Log.d(TAG, "Observer onResumeEvent");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStopEvent(){
        Log.d(TAG, "Observer onStopEvent");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public void onAnyEvent(){
        Log.d(TAG, "Observer onAnyEvent");
    }

}


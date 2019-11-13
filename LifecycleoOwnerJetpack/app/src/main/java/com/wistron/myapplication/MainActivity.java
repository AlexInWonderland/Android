package com.wistron.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    String TAG = "Alex Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLifecycle().addObserver(new ActivityLifeCycleObserver());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, TAG+"OnStart");
    }
}

package com.wistron.custombraodcastreceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    CustomBroadcastReceiver cbr = new CustomBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter i = new IntentFilter("com.alex.EXAMPLE_ACTION");
        registerReceiver(cbr, i);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(cbr);
    }
}

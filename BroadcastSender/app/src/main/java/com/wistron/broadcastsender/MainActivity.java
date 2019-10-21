package com.wistron.broadcastsender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
    }

    public void sendBroadCast(View v){
        Intent i = new Intent("com.alex.EXAMPLE_ACTION");
        i.putExtra("com.alex.EXTRA_TXT", "Broadcast received!");
        sendBroadcast(i);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String s = intent.getStringExtra("com.alex.EXTRA_TXT");
            tv.setText(s);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("com.alex.EXAMPLE_ACTION");
        registerReceiver(broadcastReceiver, filter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
    
}

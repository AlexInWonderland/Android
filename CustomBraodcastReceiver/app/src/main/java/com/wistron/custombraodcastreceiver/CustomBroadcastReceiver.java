package com.wistron.custombraodcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CustomBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if("com.alex.EXAMPLE_ACTION".equals(intent.getAction())){
            String receivedmsg = intent.getStringExtra("com.alex.EXTRA_TXT");
            Toast.makeText(context, receivedmsg, Toast.LENGTH_SHORT).show();
        }
    }
}

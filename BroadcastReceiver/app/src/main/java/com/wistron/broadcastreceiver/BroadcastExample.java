package com.wistron.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class BroadcastExample extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            boolean c = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if(c){
                Toast.makeText(context, "connect lost", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context, "connect back", Toast.LENGTH_LONG).show();
            }
        }
    }
}

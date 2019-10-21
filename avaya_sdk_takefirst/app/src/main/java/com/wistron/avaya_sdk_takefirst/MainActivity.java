package com.wistron.avaya_sdk_takefirst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.avaya.clientservices.call.Call;
import com.avaya.clientservices.call.CallService;

public class MainActivity extends AppCompatActivity {

    private SDKManager sdkManagerInstance;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sdkManagerInstance = SDKManager.getInstance(this);
        // Configure and create Client when application started
        sdkManagerInstance.setupClientConfiguration(getApplication());

        // Configure and create User
        sdkManagerInstance.setupUserConfiguration();

        startService(new Intent(this, ForegroundService.class));
        Button btn = findViewById(R.id.btn_call);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call c = sdkManagerInstance.createCall("501033");
                sdkManagerInstance.startCall(c);
            }
        });
    }
}

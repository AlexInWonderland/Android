package com.wistron.avaya_sdk_example;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {


    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn = view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences settings = getActivity().getSharedPreferences(SDKManager.CLIENTSDK_TEST_APP_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor settingsEditor = settings.edit();
                settingsEditor.putString(SDKManager.ADDRESS, "aurademo.avaya.com.tw");
                settingsEditor.putInt(SDKManager.PORT, 5061);
                settingsEditor.putString(SDKManager.DOMAIN, "avaya.com.tw");
                settingsEditor.putBoolean(SDKManager.USE_TLS, true);
                settingsEditor.putString(SDKManager.EXTENSION, "501031");
                settingsEditor.putString(SDKManager.PASSWORD, "123456");
                settingsEditor.apply();

                // We need to delete old user, because settings may changed. We can handle this by OnSharedPreferenceChangeListener
                // but as we don't have separate 'login' button let's recreate and re-login user each time 'apply' button pressed.
                // The following function will force logout if user was logged in. User will be recreated in SDKManager.onClientUserRemoved() once old user deleted.
                // Attempt to login will be done after new user creation.
                if (SDKManager.getInstance(getActivity()).getUser() == null) {
                    // If current user is already null we are just creating new user here, as we don't need to remove old one
                    SDKManager.getInstance(getActivity()).setupUserConfiguration();
                } else {
                    SDKManager.getInstance(getActivity()).delete(SDKManager.getInstance(getActivity()).isUserLoggedIn());
                }
            }
        });
    }
}

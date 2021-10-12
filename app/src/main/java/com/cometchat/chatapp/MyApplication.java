package com.cometchat.chatapp;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.uikit.ui_settings.UIKitSettings;

public class MyApplication extends Application {

    public MyApplication() {
        // this method fires only once per application start.
        // getApplicationContext returns null here

        Log.i("main", "Constructor fired");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // this method fires once as well as constructor
        // but also application has context here

        Log.i("main", "onCreate fired");
        this.initCometChat();
    }

    private void initCometChat() {
        AppSettings appSettings=new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(Constants.COMETCHAT_REGION).build();

        CometChat.init(this, Constants.COMETCHAT_APP_ID, appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                UIKitSettings.setAuthKey(Constants.COMETCHAT_AUTH_KEY);
                CometChat.setSource("uikit","android","java");
                Toast.makeText(MyApplication.this, "Initialized CometChat", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(CometChatException e) {
                Toast.makeText(MyApplication.this, "Failure to initialize CometChat", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.example.user.myapplication;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;

/**
 * Created by user on 2016/5/5.
 */
public class SimpleUIApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("2r2OYW7rQeHV6GyKRV2qHjztebEcCbjSzrN8OXS9")
                        .clientKey("0gdK4Z9rCC6WkfawkP48vYwCzPfJp4vGdZkEtreQ")
                                //.applicationId("1IIbdKr6rgMlclbPtrTcibogTq4wst4GVJC2dOX2")
                                //.clientKey("：lYpFFD6Bz8mendveOW91UvjypoGruuaaQPc4EUyR")


                        .server("https://parseapi.back4app.com/")
                        .build()
        );

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}

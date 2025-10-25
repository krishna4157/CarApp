package com.vasilkoff.easyvpnfree;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.androidnetworking.AndroidNetworking;

//import com.crashlytics.android.Crashlytics;
//import com.crashlytics.android.answers.Answers;
//
//import io.fabric.sdk.android.Fabric;

public class App extends  androidx.multidex.MultiDexApplication {

    private static App instance;
    private static final String PROPERTY_ID = "UA-89622148-1";
    private static final String PROPERTY_ID_PRO = "UA-89641705-1";

    @Override
    public void onCreate() {
        super.onCreate();
//        if (!BuildConfig.DEBUG)
//            Fabric.with(this, new Crashlytics());
        AndroidNetworking.initialize(getApplicationContext());

        instance = this;
    }

    synchronized public void getDefaultTracker() {
//        if (mTracker == null) {
//            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//            mTracker = analytics.newTracker(BuildConfig.FLAVOR == "pro" ? PROPERTY_ID_PRO : PROPERTY_ID);
//        }
//        return mTracker;
//        return new Tracker
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static String getResourceString(int resId) {
        return instance.getString(resId);
    }

    public static App getInstance() {
        return instance;
    }

}

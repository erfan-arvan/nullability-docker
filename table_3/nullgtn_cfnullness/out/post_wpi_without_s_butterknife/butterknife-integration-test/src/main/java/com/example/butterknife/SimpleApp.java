package com.example.butterknife;

import android.app.Application;
import butterknife.ButterKnife;
import android.support.annotation.Nullable;

public class SimpleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(BuildConfig.DEBUG);
    }
}

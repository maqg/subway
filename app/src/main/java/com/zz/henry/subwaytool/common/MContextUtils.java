package com.zz.henry.subwaytool.common;

import android.app.Application;

/**
 * Created by henry on 15/6/9.
 */
public class MContextUtils extends Application {

    public static MContextUtils instance;

    public static MContextUtils getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}

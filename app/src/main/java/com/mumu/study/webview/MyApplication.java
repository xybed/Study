package com.mumu.study.webview;

import android.support.multidex.MultiDexApplication;

/**
 * Created by Administrator on 2016/11/25.
 */

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        MyCrashHandler handler = MyCrashHandler.getInstance();
        handler.init(this);
    }
}

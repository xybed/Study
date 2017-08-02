package com.mumu.study.service;

import android.os.Binder;
import android.util.Log;

/**
 * Created by Administrator on 2017/8/2.
 */

public class MyBinder extends Binder{

    public void startDownload(){
        Log.e("Binder", "startDownload");
    }
}

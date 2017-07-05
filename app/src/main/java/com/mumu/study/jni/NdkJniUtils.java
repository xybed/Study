package com.mumu.study.jni;

/**
 * Created by Administrator on 2017/2/8.
 */

public class NdkJniUtils {
    public native String getCLanguageString();
    static {
        System.loadLibrary("mumu");
    }
}

package com.jiaying.workstation.utils;

import android.util.Log;

/**
 * 作者：lenovo on 2016/2/26 10:24
 * 邮箱：353510746@qq.com
 * 功能：打印日志
 */
public class MyLog {
    public static final boolean DEBUG = true;

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }
    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }
}

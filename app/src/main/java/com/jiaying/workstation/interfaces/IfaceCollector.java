package com.jiaying.workstation.interfaces;

import android.view.SurfaceHolder;

/**
 * 作者：lenovo on 2016/3/15 07:17
 * 邮箱：353510746@qq.com
 * 功能：人脸采集
 */
public interface IfaceCollector {
    int close();
    void collect();
    int open();
    void setOnColectCallback(OnFaceCollectCallback onFaceCollectCallback);
}

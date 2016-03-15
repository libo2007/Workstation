package com.jiaying.workstation.engine;

import android.app.Activity;

import com.jiaying.workstation.interfaces.IfaceCollector;
import com.jiaying.workstation.interfaces.OnFaceCollectCallback;

/**
 * 作者：lenovo on 2016/3/15 07:28
 * 邮箱：353510746@qq.com
 * 功能：
 */
public class FaceCollector implements IfaceCollector {

    private OnFaceCollectCallback onFaceCollectCallback;
    private Activity activity;
    private static FaceCollector faceCollector = null;

    private FaceCollector(Activity activity) {
        this.activity = activity;
    }

    public synchronized static FaceCollector getInstance(Activity activity) {
        if (faceCollector == null) {
            faceCollector = new FaceCollector(activity);
        }
        return faceCollector;
    }

    @Override
    public int close() {
        return 0;
    }

    @Override
    public void collect() {

    }

    @Override
    public int open() {
        return 0;
    }

    @Override
    public void setOnColectCallback(OnFaceCollectCallback onFaceCollectCallback) {
        this.onFaceCollectCallback = onFaceCollectCallback;
    }
}

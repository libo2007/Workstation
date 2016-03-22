package com.jiaying.workstation.interfaces;

import android.graphics.Bitmap;

/**
 * 作者：李波 on 2016/3/4 13:08
 * 邮箱：353510746@qq.com
 * 功能：指纹识别
 */
public interface IfingerprintReader {
    public int open();
    public void read();
    public int close();
    public void setOnFingerprintReadCallback(OnFingerprintReadCallback onFingerprintReadCallback);

    /**
     * 作者：lenovo on 2016/3/4 17:14
     * 邮箱：353510746@qq.com
     * 功能：身份证回调
     */
    interface OnFingerprintReadCallback {
        /*
        bitmap:为null时表示读取异常
         */
        public void onFingerPrintInfo(Bitmap bitmap, String info, String timeout);
    }
}

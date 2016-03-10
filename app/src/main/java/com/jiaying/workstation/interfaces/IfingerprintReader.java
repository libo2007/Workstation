package com.jiaying.workstation.interfaces;

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
}

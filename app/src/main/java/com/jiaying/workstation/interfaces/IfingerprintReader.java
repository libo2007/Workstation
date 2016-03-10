package com.jiaying.workstation.interfaces;

/**
 * 作者：李波 on 2016/3/4 13:08
 * 邮箱：353510746@qq.com
 * 功能：指纹识别
 */
public interface IfingerprintReader {
    //读取指纹
    public void read();
    //关闭操作
    public void close();
}

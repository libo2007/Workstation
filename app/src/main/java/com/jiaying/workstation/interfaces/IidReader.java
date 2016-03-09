package com.jiaying.workstation.interfaces;

/**
 * Created by Administrator on 2016/3/9 0009.
 */
public interface IidReader {
    public int open();
    public void read();
    public int close();
    public void setOnReadCallback(OnReadCallback onReadCallback);
}

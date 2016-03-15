package com.jiaying.workstation.engine;

import com.jiaying.workstation.interfaces.IidReader;

/**
 * Created by Administrator on 2016/3/9 0009.
 */
public class ProxyIdReader implements IidReader {
    private IidReader iidReader;
    private static ProxyIdReader proxyIdReader = null;

    private ProxyIdReader(IidReader iidReader) {
        this.iidReader = iidReader;
    }

    public synchronized static ProxyIdReader getInstance(IidReader iidReader) {
        if (proxyIdReader == null) {
            proxyIdReader = new ProxyIdReader(iidReader);
        }
        return proxyIdReader;
    }

    @Override
    public int open() {
        return this.iidReader.open();
    }

    @Override
    public void read() {
        this.iidReader.read();
    }

    @Override
    public int close() {
        return this.iidReader.close();
    }

    @Override
    public void setOnIdReadCallback(OnIdReadCallback onIdReadCallback) {
        this.iidReader.setOnIdReadCallback(onIdReadCallback);
    }
}

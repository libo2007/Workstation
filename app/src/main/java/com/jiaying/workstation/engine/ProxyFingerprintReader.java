package com.jiaying.workstation.engine;

import com.jiaying.workstation.interfaces.IfingerprintReader;

/**
 * 作者：lenovo on 2016/3/4 13:27
 * 邮箱：353510746@qq.com
 * 功能：指纹代理
 */
public class ProxyFingerprintReader implements IfingerprintReader {
    private IfingerprintReader ifingerprintReader;
    private static ProxyFingerprintReader proxyFingerprintReader = null;

    private ProxyFingerprintReader(IfingerprintReader ifingerprintReader) {
        this.ifingerprintReader = ifingerprintReader;
    }

    public synchronized static ProxyFingerprintReader getInstance(IfingerprintReader ifingerprintReader) {
        if (proxyFingerprintReader == null) {
            proxyFingerprintReader = new ProxyFingerprintReader(ifingerprintReader);
        }
        return proxyFingerprintReader;
    }

    @Override
    public int open() {
        return this.ifingerprintReader.open();
    }

    @Override
    public void read() {
        this.ifingerprintReader.read();
    }
    @Override
    public int close() {
        return this.ifingerprintReader.close();
    }
    @Override
    public void setOnFingerprintReadCallback(OnFingerprintReadCallback onFingerprintReadCallback) {
        this.ifingerprintReader.setOnFingerprintReadCallback(onFingerprintReadCallback);
    }
}

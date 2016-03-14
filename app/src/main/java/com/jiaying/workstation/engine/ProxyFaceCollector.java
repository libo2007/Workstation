package com.jiaying.workstation.engine;

/**
 * Created by hipil on 2016/3/14.
 */
public class ProxyFaceCollector {
    private static ProxyFaceCollector ourInstance = new ProxyFaceCollector();

    public static ProxyFaceCollector getInstance() {
        return ourInstance;
    }

    private ProxyFaceCollector() {
    }
}

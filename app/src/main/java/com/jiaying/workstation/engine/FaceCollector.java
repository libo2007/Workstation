package com.jiaying.workstation.engine;

/**
 * Created by hipil on 2016/3/14.
 */
public class FaceCollector {
    private static FaceCollector ourInstance = new FaceCollector();

    public static FaceCollector getInstance() {
        return ourInstance;
    }

    private FaceCollector() {
    }
}

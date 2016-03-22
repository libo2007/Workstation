package com.jiaying.workstation.utils;

/**
 * Created by hipil on 2016/3/22.
 */
public class DealFlag {
    private boolean isDeal = true;

    public synchronized boolean isFirst() {
        boolean reFlag = isDeal;
        isDeal = false;
        return reFlag;
    }

    public synchronized void reset() {
        this.isDeal = true;
    }
}

package com.jiaying.workstation.interfaces;

import android.graphics.Bitmap;

/**
 * Created by hipil on 2016/3/14.
 */
public interface IfaceCollector {
    public interface OnfaceCollectCallback {
        public void onCollect(Bitmap faceBitmap, int x, int y, int length, int width);
    }
}

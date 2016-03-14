package com.jiaying.workstation.interfaces;

import com.jiaying.workstation.entity.IdentityCardEntity;

/**
 * Created by Administrator on 2016/3/9 0009.
 */
public interface IidReader {
    public int open();
    public void read();
    public int close();
    public void setOnIdReadCallback(OnIdReadCallback onIdReadCallback);

    /**
     * Created by Administrator on 2016/3/9 0009.
     */
    interface OnIdReadCallback {
        public void onRead(IdentityCardEntity identityCardEntity);
    }
}

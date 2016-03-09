package com.jiaying.workstation.engine;

import com.jiaying.workstation.interfaces.IfingerPrint;
import com.jiaying.workstation.interfaces.Iidentification;

/**
 * 作者：lenovo on 2016/3/4 13:27
 * 邮箱：353510746@qq.com
 * 功能：指纹代理
 */
public class ProxyFingerPrint implements IfingerPrint {

    private IfingerPrint ifingerPrint;

    public ProxyFingerPrint(IfingerPrint ifingerPrint) {
        this.ifingerPrint = ifingerPrint;
    }

    @Override
    public void read() {
        ifingerPrint.read();
    }

    @Override
    public void close() {
        ifingerPrint.close();
    }


}

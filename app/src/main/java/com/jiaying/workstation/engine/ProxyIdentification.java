package com.jiaying.workstation.engine;

import com.jiaying.workstation.interfaces.Iidentification;

/**
 * 作者：lenovo on 2016/3/4 13:27
 * 邮箱：353510746@qq.com
 * 功能：身份证代理
 */
public class ProxyIdentification implements Iidentification {

    private Iidentification iidentification;

    public ProxyIdentification(Iidentification iidentification) {
        this.iidentification = iidentification;
    }

    @Override
    public void read() {
        iidentification.read();
    }

    @Override
    public void close() {
        iidentification.close();
    }


}

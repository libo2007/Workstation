package com.jiaying.workstation.interfaces;

import com.jiaying.workstation.entity.IdentityCard;

/**
 * 作者：lenovo on 2016/3/4 17:14
 * 邮箱：353510746@qq.com
 * 功能：身份证回调
 */
public interface IidentificationCallback {
    public void onResultInfo(String info, IdentityCard card);
}

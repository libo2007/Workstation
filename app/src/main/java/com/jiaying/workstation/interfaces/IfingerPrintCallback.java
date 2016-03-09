package com.jiaying.workstation.interfaces;

import android.graphics.Bitmap;

import com.jiaying.workstation.entity.IdentityCard;

/**
 * 作者：lenovo on 2016/3/4 17:14
 * 邮箱：353510746@qq.com
 * 功能：身份证回调
 */
public interface IfingerPrintCallback {
    /*
    bitmap:指纹，info：提示信息；timeout超时
     */
    public void onFingerPrintInfo(Bitmap bitmap, String info,String timeout);
}

package com.jiaying.workstation.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * 作者：lenovo on 2016/3/10 23:08
 * 邮箱：353510746@qq.com
 * 功能：
 */
public class ToastUtils {
    public static void showToast(Activity activity,String text){
        Toast.makeText(activity,text,Toast.LENGTH_SHORT).show();
    }
    public static void showToast(Activity activity,int text_id){
        Toast.makeText(activity,text_id,Toast.LENGTH_SHORT).show();
    }
}

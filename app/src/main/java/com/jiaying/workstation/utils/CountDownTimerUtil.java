package com.jiaying.workstation.utils;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import com.jiaying.workstation.R;
import com.jiaying.workstation.constant.Constants;

/**
 * 作者：lenovo on 2016/3/5 15:25
 * 邮箱：353510746@qq.com
 * 功能：倒计时类
 */
public class CountDownTimerUtil {
    private TextView textView;
    private Context context;
    private CountDownTimer countDownTimer = null;
    public CountDownTimerUtil(TextView textView, Context context) {
        this.textView = textView;
        this.context = context;
    }
    public void start() {
        countDownTimer =  new CountDownTimer(Constants.COUNT_DOWN_TIME, 1000) {
            public void onTick(long millisUntilFinished) {
                textView.setText("" + millisUntilFinished / 1000);
            }
            public void onFinish() {
                textView.setText("");
                Toast.makeText(context,R.string.identify_time_out,Toast.LENGTH_SHORT).show();
                ((Activity) context).finish();
            }
        };
        countDownTimer.start();
    }
    public void cancel(){
        if(countDownTimer != null){
            countDownTimer.cancel();
            textView.setText("");
        }
    }
}

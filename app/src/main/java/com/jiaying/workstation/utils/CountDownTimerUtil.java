package com.jiaying.workstation.utils;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import com.jiaying.workstation.R;
import com.jiaying.workstation.constant.Constants;
import com.jiaying.workstation.interfaces.OnCountDownTimerFinishCallback;

/**
 * 作者：lenovo on 2016/3/5 15:25
 * 邮箱：353510746@qq.com
 * 功能：倒计时类
 */
public class CountDownTimerUtil {
    private TextView textView;
    private Context context;
    private CountDownTimer countDownTimer = null;
    private static CountDownTimerUtil countDownTimerUtil = null;
    private OnCountDownTimerFinishCallback onCountDownTimerFinishCallback;

    private CountDownTimerUtil(TextView textView, Context context) {
        this.textView = textView;
        this.context = context;
    }

    public static CountDownTimerUtil getInstance(TextView textView, Context context) {
        if (countDownTimerUtil == null) {
            countDownTimerUtil = new CountDownTimerUtil(textView, context);
        }
        return countDownTimerUtil;
    }

    //默认20s倒计时
    public void start() {
        countDownTimer = new CountDownTimer(Constants.COUNT_DOWN_TIME, 1000) {
            public void onTick(long millisUntilFinished) {
                textView.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                countDownTimerUtil = null;
                textView.setText("");
                Toast.makeText(context, R.string.identify_time_out, Toast.LENGTH_SHORT).show();
                ((Activity) context).finish();
            }
        };
        countDownTimer.start();
    }

    public void start(int time) {
        countDownTimer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                textView.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                textView.setText("");
                countDownTimerUtil = null;
//                Toast.makeText(context, R.string.identify_time_out, Toast.LENGTH_SHORT).show();
//                ((Activity) context).finish();
                onCountDownTimerFinishCallback.onFinish();
            }
        };
        countDownTimer.start();
    }

    public void cancel() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            textView.setText("");
            countDownTimerUtil = null;
        }
    }


    public void setOnCountDownTimerFinishCallback(OnCountDownTimerFinishCallback onCountDownTimerFinishCallback) {
        this.onCountDownTimerFinishCallback = onCountDownTimerFinishCallback;
    }
}

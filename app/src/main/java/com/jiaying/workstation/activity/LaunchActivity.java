package com.jiaying.workstation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jiaying.workstation.R;

/**
 * 启动页面
 */
public class LaunchActivity extends Activity {
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        mHandler.postDelayed(new runnable(), 3000);
    }

    private class runnable implements Runnable {
        @Override
        public void run() {
            Intent it = new Intent(LaunchActivity.this, MainActivity.class);
            startActivity(it);
            finish();
        }
    }

}

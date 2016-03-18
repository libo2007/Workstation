package com.jiaying.workstation.activity.launch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.softfan.dataCenter.DataCenterClientService;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.loginandout.LoginActivity;
import com.jiaying.workstation.thread.ObservableZXDCSignalListenerThread;

/**
 * 启动页面，三秒后跳转到选择护士界面
 */
public class LaunchActivity extends Activity {
    private Handler mHandler = new Handler();

    public  static DataCenterClientService clientService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        connectTcpIpServer();
        mHandler.postDelayed(new runnable(), 3000);
    }

    private class runnable implements Runnable {
        @Override
        public void run() {
            Intent it = new Intent(LaunchActivity.this, LoginActivity.class);
            startActivity(it);
            finish();
        }
    }

    //连服务器
    private void connectTcpIpServer() {
        ObservableZXDCSignalListenerThread observableZXDCSignalListenerThread = new ObservableZXDCSignalListenerThread();
        observableZXDCSignalListenerThread.start();
    }

}

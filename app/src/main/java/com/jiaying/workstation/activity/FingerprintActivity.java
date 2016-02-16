package com.jiaying.workstation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jiaying.workstation.R;
import com.jiaying.workstation.utils.SetTopView;
/*
指纹认证
 */
public class FingerprintActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_fingerprint);
        new SetTopView(this, R.string.title_activity_fingerprint, true);
        //认证通过后跳到主界面
        new Handler().postDelayed(new runnable(), 3000);
    }


    @Override
    public void loadData() {

    }

    @Override
    public void initVariables() {

    }

    private class runnable implements Runnable {
        @Override
        public void run() {
            Intent it = new Intent(FingerprintActivity.this, MainActivity.class);
            startActivity(it);
            finish();
        }
    }
}


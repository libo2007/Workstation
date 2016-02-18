package com.jiaying.workstation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jiaying.workstation.R;
import com.jiaying.workstation.utils.SetTopView;

/*
人脸采集
 */
public class FaceCollectionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_identity_card);
        new SetTopView(this, R.string.title_activity_identity, false);
        //认证通过后跳到指纹界面
        new Handler().postDelayed(new runnable(), 3000);
    }

    @Override
    public void loadData() {

    }


    private class runnable implements Runnable {
        @Override
        public void run() {
            Intent it = new Intent(FaceCollectionActivity.this, FingerprintActivity.class);
            startActivity(it);
            finish();
        }
    }
}


package com.jiaying.workstation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jiaying.workstation.R;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.utils.SetTopView;

/*
身份证模块
 */
public class IdentityCardActivity extends BaseActivity {

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
            Intent it = new Intent(IdentityCardActivity.this, FingerprintActivity.class);
            it.putExtra(IntentExtra.EXTRA_TYPE,getIntent().getIntExtra(IntentExtra.EXTRA_TYPE,0));
            startActivity(it);
            finish();
        }
    }
}


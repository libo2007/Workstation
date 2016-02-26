package com.jiaying.workstation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jiaying.workstation.R;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;
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
        new SetTopView(this, R.string.title_activity_identity, true);
        //认证通过后跳到指纹界面
        new Handler().postDelayed(new runnable(), 3000);
    }

    @Override
    public void loadData() {

    }


    private class runnable implements Runnable {
        @Override
        public void run() {
            Intent it = null;
            int type = getIntent().getIntExtra(IntentExtra.EXTRA_TYPE, 0);
            if (type == TypeConstant.TYPE_SEARCH) {
                //查询的跳到查询结果
                it = new Intent(IdentityCardActivity.this, SearchResultActivity.class);
            } else {
                //其他情况，到指纹
                it = new Intent(IdentityCardActivity.this, FingerprintActivity.class);
                it.putExtra(IntentExtra.EXTRA_TYPE, getIntent().getIntExtra(IntentExtra.EXTRA_TYPE, 0));
            }
            startActivity(it);
            finish();
        }
    }
}


package com.jiaying.workstation.activity.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.BaseActivity;
import com.jiaying.workstation.utils.SetTopView;

/**
 * 登记完成
 */
public class RegisterOverActivity extends BaseActivity {
    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_register_over);
        new SetTopView(this,R.string.title_activity_register_over,true);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}

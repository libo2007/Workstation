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
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;
import com.jiaying.workstation.utils.SetTopView;

/*
指纹认证模块
 */
public class FingerprintActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_fingerprint);
        new SetTopView(this, R.string.title_activity_fingerprint, false);
        //认证通过后跳到主界面
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
            if (type == TypeConstant.TYPE_REG) {
                //登记的话就到采集人脸
                it = new Intent(FingerprintActivity.this, FaceCollectionActivity.class);
            } else if (type == TypeConstant.TYPE_BLOODPLASMACOLLECTION) {
                //献浆的，去选择浆机
                it = new Intent(FingerprintActivity.this, PulpMachineSelectActivity.class);
            } else if (type == TypeConstant.TYPE_PHYSICAL_EXAM) {
                //体检，去体检
                it = new Intent(FingerprintActivity.this, PhysicalExamActivity.class);
            } else {
                //其他的情况
                it = new Intent(FingerprintActivity.this, MainActivity.class);
            }
           if(it!= null){
               startActivity(it);
               finish();
           }
        }
    }
}


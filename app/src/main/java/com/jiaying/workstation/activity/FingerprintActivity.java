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
    private Handler mHandler = new Handler();
    private Runnable mRunnable = null;
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
        mRunnable = new runnable();
        mHandler.postDelayed(mRunnable, 3000);
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
            }else if (type == TypeConstant.TYPE_PHYSICAL_EXAM_SUBMIT_XJ) {
                //体检完成后提交体检，献浆员打指纹-》医生打指纹

                new SetTopView(FingerprintActivity.this, R.string.title_activity_fingerprint_xj, false);
                it = new Intent(FingerprintActivity.this, FingerprintActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                it.putExtra(IntentExtra.EXTRA_TYPE, TypeConstant.TYPE_PHYSICAL_EXAM_SUBMIT_DOC);
            }else if (type == TypeConstant.TYPE_PHYSICAL_EXAM_SUBMIT_DOC) {
                //体检完成后提交体检，医生打指纹后，显示体检结果
                new SetTopView(FingerprintActivity.this, R.string.title_activity_fingerprint_doc, false);
                it = new Intent(FingerprintActivity.this, PhysicalExamResultActivity.class);
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


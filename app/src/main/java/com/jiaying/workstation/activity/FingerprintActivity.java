package com.jiaying.workstation.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.workstation.R;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;
import com.jiaying.workstation.engine.LDFingerPrint;
import com.jiaying.workstation.engine.ProxyFingerPrint;
import com.jiaying.workstation.interfaces.IfingerPrint;
import com.jiaying.workstation.interfaces.IfingerPrintCallback;
import com.jiaying.workstation.interfaces.Iidentification;
import com.jiaying.workstation.utils.CountDownTimerUtil;
import com.jiaying.workstation.utils.MyLog;
import com.jiaying.workstation.utils.SetTopView;

/*
指纹认证模块
 */
public class FingerprintActivity extends BaseActivity implements IfingerPrintCallback{
    private static final String TAG = "FingerprintActivity";
    private Handler mHandler = new Handler();
    private Runnable mRunnable = null;

    private IfingerPrint ifingerPrint = null;
    private CountDownTimerUtil countDownTimerUtil;
    private TextView result_txt;
    private TextView state_txt;
    private ImageView photo_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initVariables() {
        //指纹识别准备
        ifingerPrint  = new LDFingerPrint(this,this);
        ProxyFingerPrint proxyFingerPrint = new ProxyFingerPrint(ifingerPrint);
        proxyFingerPrint.read();
        //倒计时开始
        countDownTimerUtil = new CountDownTimerUtil(result_txt,this);
        countDownTimerUtil.start();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_fingerprint);
        new SetTopView(this, R.string.title_activity_fingerprint, true);
        result_txt = (TextView) findViewById(R.id.result_txt);
        state_txt = (TextView) findViewById(R.id.state_txt);
        photo_image = (ImageView) findViewById(R.id.photo_image);


    }

    @Override
    public void loadData() {

    }

    @Override
    public void onFingerPrintInfo(Bitmap bitmap, String info) {
        //指纹识别结果
        if(bitmap != null){
            countDownTimerUtil.cancel();
            photo_image.setImageBitmap(bitmap);

            //认证通过后跳到
            mRunnable = new runnable();
            mHandler.postDelayed(mRunnable, 3000);
        }else{
            MyLog.e(TAG,"finger print is null");
        }
        if(!TextUtils.isEmpty(info)){
            state_txt.setText(info);
        }
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
            } else if (type == TypeConstant.TYPE_PHYSICAL_EXAM_SUBMIT_XJ) {
                //体检完成后提交体检，献浆员打指纹-》医生打指纹

                new SetTopView(FingerprintActivity.this, R.string.title_activity_fingerprint_xj, false);
                it = new Intent(FingerprintActivity.this, FingerprintActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                it.putExtra(IntentExtra.EXTRA_TYPE, TypeConstant.TYPE_PHYSICAL_EXAM_SUBMIT_DOC);
            } else if (type == TypeConstant.TYPE_PHYSICAL_EXAM_SUBMIT_DOC) {
                //体检完成后提交体检，医生打指纹后，显示体检结果
                new SetTopView(FingerprintActivity.this, R.string.title_activity_fingerprint_doc, false);
                it = new Intent(FingerprintActivity.this, PhysicalExamResultActivity.class);
            } else {
                //其他的情况
                it = new Intent(FingerprintActivity.this, MainActivity.class);
            }
            if (it != null) {
                startActivity(it);
                finish();
            }
        }
    }
}


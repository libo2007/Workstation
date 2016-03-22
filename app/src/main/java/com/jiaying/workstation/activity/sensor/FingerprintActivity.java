package com.jiaying.workstation.activity.sensor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.BaseActivity;
import com.jiaying.workstation.activity.CameraPreviewActivity;
import com.jiaying.workstation.activity.MainActivity;
import com.jiaying.workstation.activity.physicalexamination.PhysicalExamActivity;
import com.jiaying.workstation.activity.physicalexamination.PhysicalExamResultActivity;
import com.jiaying.workstation.activity.plasmacollection.SelectPlasmaMachineActivity;
import com.jiaying.workstation.constant.Constants;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;
import com.jiaying.workstation.engine.LdFingerprintReader;
import com.jiaying.workstation.engine.ProxyFingerprintReader;
import com.jiaying.workstation.interfaces.IfingerprintReader;
import com.jiaying.workstation.utils.CountDownTimerUtil;
import com.jiaying.workstation.utils.MyLog;
import com.jiaying.workstation.utils.SetTopView;
import com.jiaying.workstation.utils.ToastUtils;

/*
指纹认证模块
 */
public class FingerprintActivity extends BaseActivity implements IfingerprintReader.OnFingerprintReadCallback {
    private static final String TAG = "FingerprintActivity";
    private Handler mHandler = new Handler();
    private Runnable mRunnable = null;


    private IfingerprintReader ifingerprintReader = null;
    private ProxyFingerprintReader proxyFingerprintReader = null;
    //    private CountDownTimerUtil countDownTimerUtil;
    private TextView result_txt;
    private TextView state_txt;
    private ImageView photo_image;

    private TextView nameTextView = null;
    private TextView idCardNoTextView = null;
    private ImageView avaterImageView = null;
    private String donorName = null;
    private Bitmap avatarBitmap = null;
    private String idCardNO = null;
    private int source;

    private CountDownTimerUtil countDownTimerUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initVariables() {

        Intent donorInfoIntent = getIntent();
        source = donorInfoIntent.getIntExtra("source", 0);
        switch (source) {
            case TypeConstant.TYPE_LOG:
                break;
            case TypeConstant.TYPE_REG:
                donorName = donorInfoIntent.getStringExtra("donorName");
                Bitmap tempBitmap = donorInfoIntent.getParcelableExtra("avatar");
                Matrix matrix = new Matrix();
                matrix.postScale(1.0f, 1.0f);
                avatarBitmap = Bitmap.createBitmap(tempBitmap, 0, 0, tempBitmap.getWidth(),
                        tempBitmap.getHeight(), matrix, true);
                idCardNO = donorInfoIntent.getStringExtra("idCardNO");
                break;

        }

        //指纹识别准备
        ifingerprintReader = LdFingerprintReader.getInstance(this);
        proxyFingerprintReader = ProxyFingerprintReader.getInstance(ifingerprintReader);
        proxyFingerprintReader.setOnFingerprintReadCallback(this);
        proxyFingerprintReader.open();
        proxyFingerprintReader.read();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_fingerprint);
        new SetTopView(this, R.string.title_activity_fingerprint, true);

        result_txt = (TextView) findViewById(R.id.result_txt);
        state_txt = (TextView) findViewById(R.id.state_txt);
        photo_image = (ImageView) findViewById(R.id.photo_image);
        //开始倒计时
        countDownTimerUtil = CountDownTimerUtil.getInstance(result_txt, this);
        countDownTimerUtil.start();


        switch (source) {

            case TypeConstant.TYPE_LOG:
                break;
            case TypeConstant.TYPE_REG:
                nameTextView = (TextView) this.findViewById(R.id.name_txt);
                nameTextView.setText(donorName);
                avaterImageView = (ImageView) this.findViewById(R.id.head_image);
                avaterImageView.setImageBitmap(avatarBitmap);
                idCardNoTextView = (TextView) this.findViewById(R.id.id_txt);
                idCardNoTextView.setText(idCardNO);
                break;
        }
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onFingerPrintInfo(final Bitmap bitmap) {
        //指纹识别结果
        if (bitmap != null) {
//            countDownTimerUtil.cancel();
            FingerprintActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    countDownTimerUtil.cancel();
                    photo_image.setImageBitmap(bitmap);
                }
            });


            //认证通过后跳到
            mRunnable = new runnable();
            mHandler.postDelayed(mRunnable, 1000);
        } else {
            MyLog.e(TAG, "finger print is null");
        }
//        if (!TextUtils.isEmpty(info)) {
//            state_txt.setText(info);
//        }
//        if (!TextUtils.isEmpty(timeout)) {
//            if (timeout.equals(Constants.COUNT_DOWN_TIME + "")) {
//                ToastUtils.showToast(this, R.string.identify_time_out);
//                finish();
//            } else {
//                result_txt.setText(timeout);
//            }
//
//        }

    }

    private class runnable implements Runnable {
        @Override
        public void run() {
            Intent it = null;
            int type = getIntent().getIntExtra(IntentExtra.EXTRA_TYPE, 0);
            if (type == TypeConstant.TYPE_REG) {
                //登记的话就到采集人脸
//                it = new Intent(FingerprintActivity.this, FaceCollectionActivity.class);
                it = new Intent(FingerprintActivity.this, CameraPreviewActivity.class);
            } else if (type == TypeConstant.TYPE_BLOODPLASMACOLLECTION) {
                //献浆的，去选择浆机
                it = new Intent(FingerprintActivity.this, SelectPlasmaMachineActivity.class);
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

    @Override
    protected void onPause() {
        super.onPause();
//        closeFingerReader();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        closeFingerReader();
        if (countDownTimerUtil != null) {
            countDownTimerUtil.cancel();
            countDownTimerUtil = null;
        }
    }

    private void closeFingerReader() {
        if (ifingerprintReader != null) {
            ifingerprintReader.close();
        }
    }
}


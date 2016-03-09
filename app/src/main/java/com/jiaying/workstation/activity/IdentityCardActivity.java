package com.jiaying.workstation.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.workstation.R;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;
import com.jiaying.workstation.engine.LdIdReader;
import com.jiaying.workstation.engine.ProxyIdReader;
import com.jiaying.workstation.entity.IdentityCard;
import com.jiaying.workstation.interfaces.IidReader;
import com.jiaying.workstation.interfaces.OnReadCallback;
import com.jiaying.workstation.utils.CountDownTimerUtil;
import com.jiaying.workstation.utils.MyLog;
import com.jiaying.workstation.utils.SetTopView;

/*
身份证模块
 */
public class IdentityCardActivity extends BaseActivity implements OnReadCallback {
    private static final String TAG = "IdentityCardActivity";
    private TextView result_txt;
    private TextView state_txt;
    private ImageView photo_image;
    private String donorName;
    private Bitmap avtar;
    private String idCardNO;
    private ProxyIdReader proxyIdReader;
    private CountDownTimerUtil countDownTimerUtil;
    private IidReader iidReader;
    public static TextToSpeech mTts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initVariables() {
        //身份证读取预备
        iidReader = LdIdReader.getInstance(this);
        proxyIdReader = ProxyIdReader.getInstance(iidReader);
        proxyIdReader.setOnReadCallback(this);
        proxyIdReader.open();
        proxyIdReader.read();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_identity_card);
        new SetTopView(this, R.string.title_activity_identity, true);
        result_txt = (TextView) findViewById(R.id.result_txt);
//        state_txt = (TextView) findViewById(R.id.state_txt);
//        photo_image = (ImageView) findViewById(R.id.photo_image);
        //倒计时开始
        countDownTimerUtil = new CountDownTimerUtil(result_txt, this);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();


        countDownTimerUtil.start();


    }

    @Override
    public void onRead(IdentityCard identityCard) {
        proxyIdReader.close();
        if (identityCard != null) {
            IdentityCardActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    countDownTimerUtil.cancel();
                }
            });
            MyLog.e(TAG, "card info:" + identityCard.toString());

            donorName = identityCard.getName();
            avtar = identityCard.getPhotoBmp();
            idCardNO = identityCard.getIdcardno();
            //认证通过后跳到指纹界面
            new Handler().postDelayed(new runnable(), 10);

        } else {
            MyLog.e(TAG, "card is null");
        }
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
                it.putExtra("donorName", donorName);
                it.putExtra("avatar", avtar);
                it.putExtra("idCardNO", idCardNO);
                it.putExtra("source", TypeConstant.TYPE_REG);
            }
            startActivity(it);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        iidReader.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iidReader.close();
        if (countDownTimerUtil != null) {
            countDownTimerUtil.cancel();
            countDownTimerUtil = null;
        }
    }
}


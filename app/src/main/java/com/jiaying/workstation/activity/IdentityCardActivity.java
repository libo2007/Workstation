package com.jiaying.workstation.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.workstation.R;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;
import com.jiaying.workstation.engine.LDIdentification;
import com.jiaying.workstation.engine.ProxyIdentification;
import com.jiaying.workstation.entity.IdentityCard;
import com.jiaying.workstation.interfaces.Iidentification;
import com.jiaying.workstation.interfaces.IidentificationCallback;
import com.jiaying.workstation.utils.CountDownTimerUtil;
import com.jiaying.workstation.utils.MyLog;
import com.jiaying.workstation.utils.SetTopView;

import org.w3c.dom.Text;

/*
身份证模块
 */
public class IdentityCardActivity extends BaseActivity implements IidentificationCallback {
    private static final String TAG = "IdentityCardActivity";
    private TextView result_txt;
    private TextView state_txt;
    private ImageView photo_image;
    private String donorName;
    private Bitmap avtar;
    private String idCardNO;
    private Iidentification iidentification = null;
    private CountDownTimerUtil countDownTimerUtil;
    private ProxyIdentification proxyIdentificatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initVariables() {
        //身份证读取预备
        iidentification = new LDIdentification(this, this);
        proxyIdentificatio = new ProxyIdentification(iidentification);
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
        proxyIdentificatio.read();
        countDownTimerUtil.start();

    }

    @Override
    public void onResultInfo(String info, IdentityCard card) {
        //身份证信息

        if (card != null) {
            IdentityCardActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    countDownTimerUtil.cancel();

                }
            });
            MyLog.e(TAG, "card info:" + card.toString());

            donorName = card.getName();
            avtar = card.getPhotoBmp();
            idCardNO = card.getIdcardno();
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
    protected void onDestroy() {
        super.onDestroy();
        if (iidentification != null) {
            iidentification.close();
        }
        if (countDownTimerUtil != null) {
            countDownTimerUtil.cancel();
            countDownTimerUtil = null;
        }
    }
}


package com.jiaying.workstation.activity;

import android.content.Intent;
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
    private  Iidentification iidentification = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iidentification  = new LDIdentification(this, this);
        ProxyIdentification proxyIdentification = new ProxyIdentification(iidentification);
        proxyIdentification.operateID();
    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_identity_card);
        new SetTopView(this, R.string.title_activity_identity, true);
        result_txt = (TextView) findViewById(R.id.result_txt);
        state_txt = (TextView) findViewById(R.id.state_txt);
        photo_image = (ImageView) findViewById(R.id.photo_image);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onResultInfo(String info, IdentityCard card) {
        //身份证信息
        if (!TextUtils.isEmpty(info)) {
            MyLog.e(TAG, "info:" + info);
            state_txt.setText(info);
        } else {
            MyLog.e(TAG, "info is null");
        }
        if (card != null) {
            MyLog.e(TAG, "card info:" + card.toString());
            result_txt.setText(card.toString());
            photo_image.setImageBitmap(card.getPhotoBmp());
            //认证通过后跳到指纹界面
//            new Handler().postDelayed(new runnable(), 3000);
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
            }
            startActivity(it);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       if(iidentification != null){
           iidentification.close();
       }
    }
}


package com.jiaying.workstation.engine;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.jiaying.workstation.entity.IdentityCardEntity;
import com.jiaying.workstation.interfaces.IidReader;
import com.jiaying.workstation.utils.LDAPI;

/**
 * Created by Administrator on 2016/3/9 0009.
 */
public class LdIdReader implements IidReader {
    private OnIdReadCallback onIdReadCallback;
    long ssart = System.currentTimeMillis();
    long ssend = System.currentTimeMillis();
    long times;
    private Handler cardHandler;
    private HandlerThread readCardThread;
    private boolean readFlag = false;
    private int readStatus = 0;
    private static LdIdReader ldIdReader = null;

    private LDAPI ZAZAPI;

    private LdIdReader(Activity activity) {
        ZAZAPI = new LDAPI(activity, 4, 1);
        readFlag = true;
        readCardThread = new HandlerThread("MyHandlerThread");
        readCardThread.start();
        cardHandler = new Handler(readCardThread.getLooper());
    }

    public synchronized static LdIdReader getInstance(Activity activity) {
        if (ldIdReader == null) {
            ldIdReader = new LdIdReader(activity);
        }
        return ldIdReader;
    }

    @Override
    public int open() {

        // 上电
        int powerOnFlag = ZAZAPI.card_power_on();
        if (1 == powerOnFlag) {//上电成功
            // 初始化
            boolean initFlag = ZAZAPI.InitIDCardDevice(null);
            if (initFlag) {//初始化成功
                return 1;
            } else {//初始化失败
                return 0;
            }
        } else {//上电失败
            return 0;
        }
    }

    @Override
    public void read() {

        //开始读取身份证
        readFlag = false;
        ssart = System.currentTimeMillis();
        ssend = System.currentTimeMillis();
        cardHandler.post(cardTasks);
    }


    private Runnable cardTasks = new Runnable() {
        public void run()// 运行该服务执行此函数
        {
            String temp = "";
            int ret = 0;
            long timecount = 0;
            Log.e("readStatus = ", "" + readStatus);
            if (readFlag) {
//                mTts.speak("返回", TextToSpeech.QUEUE_FLUSH, null);
                return;
            }

            switch (readStatus) {
                case 0:
                    temp += "请刷读身份证";
                    readStatus++;
                    break;
                case 1:
                    ssart = System.currentTimeMillis();

                    //进行寻卡操作，找到卡返回true，未找到返回false。
                    if (ZAZAPI.FindIDCard())
                        readStatus++;
                    else {
                        readStatus = 1;
                    }
                    temp += "请刷读身份证";
                    break;
                case 2:
                    //读卡计时开始
                    readStatus++;
                    break;
                case 3:
                    //寻找到卡后，需要选定身份证卡，如果选定成功则为true，选定失败则为false。
                    if (ZAZAPI.SelectCard())
                        readStatus++;
                    else
                        readStatus = 3;
                    ssend = System.currentTimeMillis();
                    timecount = (ssend - ssart);
                    Log.e("time = ", "" + timecount);
                    if (timecount > 5000) {
                        Log.e("time = ", "" + "over");
                        temp += "读卡超时" + "\r\n";
                        readStatus = 0;
                    }

                    temp += "开始选卡中" + "\r\n";
                    temp += "请等待";
                    //读取身份证卡的信息，返回职位IDCard
                    break;
                case 4:
                    LDAPI.idcard = ZAZAPI.readCard();
                    if (LDAPI.idcard != null)
                        readStatus++;
                    else
                        readStatus = 4;

                    ssend = System.currentTimeMillis();
                    timecount = (ssend - ssart);
                    if (timecount > 5000) {
                        temp += "读卡超时" + "\r\n";
//                    mCallback.onResultInfo(temp, null);
                        readStatus = 0;
                    } else {
                        if (readStatus == 5) {
                            timecount = (ssend - ssart);
                            times = timecount;
                            readStatus++;

                            temp += "读卡完成" + "\r\n";
                            temp += "耗时:" + timecount + "ms";
//                        mCallback.onResultInfo(temp, null);
                            sendIdCard();
                        } else {
                            readStatus = 4;

                            temp += "正在读卡中";
//                        mCallback.onResultInfo(temp, null);
                        }
                    }
//                mCallback.onResultInfo(temp, null);
                    break;
                default:
                    if (ZAZAPI.readCard() != null)
                        //if(ZAZAPI.FindIDCard())
                        readStatus = 5;
                    else
                        readStatus = 0;


                    temp += "请拿起身份证重新刷卡";
//                mtvMessage.setText(temp);
//                mCallback.onResultInfo(temp, null);
                    break;

            }

            cardHandler.postDelayed(cardTasks, 10);

        }

    };

    public void sendIdCard() {

        if (LDAPI.idcard != null) {

            //读取到了身份证信息
            IdentityCardEntity card = new IdentityCardEntity();
            card.setName(LDAPI.idcard.name);
            card.setAddr(LDAPI.idcard.address);
            card.setNation(LDAPI.idcard.nation);
            card.setYear(LDAPI.idcard.birthday.substring(0, 4));
            card.setMonth(LDAPI.idcard.birthday.substring(4, 6));
            card.setDay(LDAPI.idcard.birthday.substring(6, 8));
            card.setIdcardno(LDAPI.idcard.idcardno);
            Bitmap bit = ZAZAPI.getPhotoBmp();
            card.setPhotoBmp(ZAZAPI.getPhotoBmp());
            onIdReadCallback.onRead(card);
        }
    }

    @Override
    public int close() {

        readFlag = true;
        cardHandler.removeCallbacks(cardTasks);
        if (ZAZAPI != null) {
            if (ZAZAPI.CloseIDCardDevice(null)) {
                return ZAZAPI.card_power_off();
            } else {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public void setOnIdReadCallback(OnIdReadCallback onIdReadCallback) {
        this.onIdReadCallback = onIdReadCallback;

    }
}

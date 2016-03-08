package com.jiaying.workstation.engine;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.jiaying.workstation.entity.IdentityCard;
import com.jiaying.workstation.interfaces.Iidentification;
import com.jiaying.workstation.interfaces.IidentificationCallback;
import com.jiaying.workstation.utils.LDAPI;

/**
 * 作者：lenovo on 2016/3/4 13:25
 * 邮箱：353510746@qq.com
 * 功能：深圳龙盾平板
 */
public class LDIdentification  {
    private IidentificationCallback mCallback;
    private Activity mActivity;
    long ssart = System.currentTimeMillis();
    long ssend = System.currentTimeMillis();
    private Handler objHandler_card;
    private HandlerThread thread;
    private long times = 0;
    private boolean readflag = false;
    private int readstatus = 0;

    private LDAPI ZAZAPI;

    public LDIdentification(Activity mActivity) {

        this.mActivity = mActivity;
        ZAZAPI = new LDAPI(mActivity, 4, 1);
        readflag = true;
        thread = new HandlerThread("MyHandlerThread");
        thread.start();
        objHandler_card = new Handler(thread.getLooper());

    }

    public void setOnRead(IidentificationCallback mCallback) {
        this.mCallback = mCallback;
    }

    //读卡器上电
    public int electrifyIdReader() {
        String resutl = "";

        // 给身份证模块上电，这里需要判断
        if (1 == ZAZAPI.card_power_on()) {
            return 1;
//            mCallback.onResultInfo(resutl, null);
        } else {
            resutl = "身份证模块上电失败";
            return 0;
//            mCallback.onResultInfo(resutl, null);
        }
    }

    //读卡器断电
    public int unelectrifyIdReader() {
        if (ZAZAPI != null) {
            return ZAZAPI.card_power_off();
        }
        else{
            return -1;
        }
    }


// 打开身份证读卡器
    public boolean openIdReader() {
        String resutl = "";
        if (ZAZAPI.InitIDCardDevice(null)) {
            resutl = "打开读卡器成功";
//            mCallback.onResultInfo(resutl, null);
            return true;
        } else {
            resutl = "打开读卡器失败";

//            mCallback.onResultInfo(resutl, null);
            return false;
        }
    }


    public void read() {
        //开始读取身份证
        try {
            thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        readflag = false;
        readIdCardInfo();
    }
    
    public boolean closeIdReader() {
        //关闭身份证模块
        objHandler_card.removeCallbacks(cardTasks);
        if (ZAZAPI.CloseIDCardDevice(null)) {
            return true;
        } else {
            return false;
        }


    }


    private void readIdCardInfo() {
        ssart = System.currentTimeMillis();
        ssend = System.currentTimeMillis();
        objHandler_card.postDelayed(cardTasks, 0);
    }

    private Runnable cardTasks = new Runnable() {
        public void run()// 运行该服务执行此函数
        {
            String temp = "";
            String sfztemp = "";
            int ret = 0;
            long timecount = 0;
            byte[] sfznum = new byte[8];
            Log.e("readstatus = ", "" + readstatus);
            if (readflag) {
//                mTts.speak("返回", TextToSpeech.QUEUE_FLUSH, null);
                timecount = 6;
                return;
            }

            if (readstatus == 0) {
//                resettextdata();
                temp += "请刷读身份证";
//                mCallback.onResultInfo(temp, null);
                readstatus++;
            } else if (readstatus == 1)//寻卡
            {
                ssart = System.currentTimeMillis();

                //进行寻卡操作，找到卡返回true，未找到返回false。
                if (ZAZAPI.FindIDCard())
                    readstatus++;
                else {
                    readstatus = 1;
                }
                temp += "请刷读身份证";
//                mCallback.onResultInfo(temp, null);

            } else if (readstatus == 2) {    //读卡计时开始
                readstatus++;
            } else if (readstatus == 3) {    //开始选卡
                //寻找到卡后，需要选定身份证卡，如果选定成功则为true，选定失败则为false。
                if (ZAZAPI.SelectCard())
                    readstatus++;
                else
                    readstatus = 3;
                ssend = System.currentTimeMillis();
                timecount = (ssend - ssart);
                Log.e("time = ", "" + timecount);
                if (timecount > 3000) {
                    Log.e("time = ", "" + "over");
                    temp += "读卡超时" + "\r\n";
                    readstatus = 0;
                }

                temp += "开始选卡中" + "\r\n";
                temp += "请等待";
                //读取身份证卡的信息，返回职位IDCard
            } else if (readstatus == 4) {
                LDAPI.idcard = ZAZAPI.readCard();
                if (LDAPI.idcard != null)
                    readstatus++;
                else
                    readstatus = 4;

                ssend = System.currentTimeMillis();
                timecount = (ssend - ssart);
                if (timecount > 3000) {
                    temp += "读卡超时" + "\r\n";
//                    mCallback.onResultInfo(temp, null);
                    readstatus = 0;
                } else {
                    if (readstatus == 5) {
                        timecount = (ssend - ssart);
                        times = timecount;
                        readstatus++;

                        temp += "读卡完成" + "\r\n";
                        temp += "耗时:" + timecount + "ms";
//                        mCallback.onResultInfo(temp, null);
                        sfzdest();
                    } else {
                        readstatus = 4;

                        temp += "正在读卡中";
//                        mCallback.onResultInfo(temp, null);
                    }
                }
//                mCallback.onResultInfo(temp, null);
            } else// if(readstatus==5)
            {
                if (ZAZAPI.readCard() != null)
                    //if(ZAZAPI.FindIDCard())
                    readstatus = 5;
                else
                    readstatus = 0;


                temp += "请拿起身份证重新刷卡";
//                mtvMessage.setText(temp);
//                mCallback.onResultInfo(temp, null);
            }
            objHandler_card.postDelayed(cardTasks, 200);
        }

    };

    public void sfzdest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);


        if (LDAPI.idcard != null) {
//            showtextdata();
            //读取到了身份证信息
            IdentityCard card = new IdentityCard();
            card.setName(LDAPI.idcard.name);
            card.setAddr(LDAPI.idcard.address);
            card.setNation(LDAPI.idcard.nation);
            card.setYear(LDAPI.idcard.birthday.substring(0, 4));
            card.setMouth(LDAPI.idcard.birthday.substring(4, 6));
            card.setDay(LDAPI.idcard.birthday.substring(6, 8));
            card.setIdcardno(LDAPI.idcard.idcardno);
            Bitmap bit = ZAZAPI.getPhotoBmp();
            card.setPhotoBmp(ZAZAPI.getPhotoBmp());
            mCallback.onResultInfo(null, card);
            LDAPI.idcard = LDAPI.idcard;
            if (LDAPI.idcard.fpflag) {
                if (LDAPI.idcard != null) {
//                    mTts.speak("读卡完成,此证件含指纹", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    if (LDAPI.idcard != null) {
//                    mTts.speak("读卡完成,此证件无指纹", TextToSpeech.QUEUE_FLUSH, null);
                    }

                }
            } else {
                if (LDAPI.idcard != null) {
                    //                mTts.speak("请先打开身份证", TextToSpeech.QUEUE_FLUSH, null);
                }


            }

        }
    }
}

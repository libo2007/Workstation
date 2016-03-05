package com.jiaying.workstation.engine;

import android.app.Activity;
import android.app.AlertDialog;
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
public class LDIdentification implements Iidentification {
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

    public LDIdentification(IidentificationCallback mCallback, Activity mActivity) {
        this.mCallback = mCallback;
        this.mActivity = mActivity;
        ZAZAPI = new LDAPI(mActivity, 4, 1);
        readflag = true;
        thread = new HandlerThread("MyHandlerThread");
        thread.start();
        objHandler_card = new Handler();
        ZAZAPI.card_power_on();
        oPenTask();
    }

    //打开读卡器
    private void oPenTask() {
        String resutl = "";
        if (ZAZAPI.InitIDCardDevice(null)) {
            resutl = "准备读卡器成功";
            resutl = "打开读卡器成功";
        } else {
            resutl = "准备读卡器失败";
            resutl = "打开读卡器失败";
            ZAZAPI.CloseIDCardDevice(null);
        }
        mCallback.onResultInfo(resutl, null);
    }

    @Override
    public void read() {
        //开始读取身份证
        try {
            thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        readflag = false;
        readsfztask();
    }

    @Override
    public void close() {
        if (ZAZAPI != null) {
            ZAZAPI.card_power_off();
        }
        objHandler_card.removeCallbacks(cardTasks);
    }

    private void readsfztask() {
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
                mCallback.onResultInfo(temp, null);
                readstatus++;
            } else if (readstatus == 1)//寻卡
            {
                ssart = System.currentTimeMillis();
                //ZAZAPI.Read_ICSFZ_PHYIDNumber();

//					ret  =ZAZAPI.getIDbaseshenzhen(sfznum);
//					if(ret==1)
//					{
//						for(int i=0;i<8;i++)
//						{
//							sfztemp+= Integer.toHexString(sfznum[i]&0xff)+" ";
//						}
//						ssmtvMessage.setText("卡号信息："+sfztemp);
//					}
//					else
//					{
//						ssmtvMessage.setText("卡号信息：");
//					}
                if (ZAZAPI.FindIDCard())
                    readstatus++;
                else {

                    readstatus = 1;
                }


                temp += "请刷读身份证";
                mCallback.onResultInfo(temp, null);

            } else if (readstatus == 2) {    //读卡计时开始

                readstatus++;
            } else if (readstatus == 3) {    //开始选卡

                if (ZAZAPI.SelectCard())
                    readstatus++;
                else
                    readstatus = 3;

                ssend = System.currentTimeMillis();
                timecount = (ssend - ssart);
                Log.e("time = ", "" + timecount);
                if (timecount > 5000) {
                    Log.e("time = ", "" + "over");
                    temp += "读卡超时" + "\r\n";
                    readstatus = 0;
//                    inerr++;
                }
//                temp = Sstr;
//                temp = temp + Strok + Integer.toString(inok) + "\r\n";
//                temp = temp + Strerr + Integer.toString(inerr) + "\r\n";
                temp += "开始选卡中" + "\r\n";
                temp += "请等待";
//                mtvMessage.setText(temp);
                mCallback.onResultInfo(temp, null);
            } else if (readstatus == 4) {    //读卡内容
                LDAPI.idcard = ZAZAPI.readCard();
                if (LDAPI.idcard != null)
                    readstatus++;
                else
                    readstatus = 4;

                ssend = System.currentTimeMillis();
                timecount = (ssend - ssart);
                if (timecount > 5000) {
                    temp += "读卡超时" + "\r\n";
                    mCallback.onResultInfo(temp, null);
                    readstatus = 0;
//                    inerr++;
                } else {
                    if (readstatus == 5) {
                        timecount = (ssend - ssart);
                        times = timecount;
                        readstatus++;
//                        inok++;
//                        temp = Sstr;
//                        temp = temp + Strok + Integer.toString(inok) + "\r\n";
//                        temp = temp + Strerr + Integer.toString(inerr) + "\r\n";
                        temp += "读卡完成" + "\r\n";
                        temp += "耗时:" + timecount + "ms";
                        mCallback.onResultInfo(temp, null);
                        sfzdest();
                    } else {
                        readstatus = 4;
//                        temp = Sstr;
//                        temp = temp + Strok + Integer.toString(inok) + "\r\n";
//                        temp = temp + Strerr + Integer.toString(inerr) + "\r\n";
                        temp += "正在读卡中";
                        mCallback.onResultInfo(temp, null);
                    }
                }
//                mtvMessage.setText(temp);
                mCallback.onResultInfo(temp, null);
            } else// if(readstatus==5)
            {
                if (ZAZAPI.readCard() != null)
                    //if(ZAZAPI.FindIDCard())
                    readstatus = 5;
                else
                    readstatus = 0;

//                temp = Sstr;
//                temp = temp + Strok + Integer.toString(inok) + "\r\n";
//                temp = temp + Strerr + Integer.toString(inerr) + "\r\n";
//                temp = temp + Strtime + times + "ms\r\n";
                temp += "请拿起身份证重新刷卡";
//                mtvMessage.setText(temp);
                mCallback.onResultInfo(temp, null);
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

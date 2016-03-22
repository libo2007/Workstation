package com.jiaying.workstation.engine;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.Toast;

import com.jiaying.workstation.constant.Constants;
import com.jiaying.workstation.interfaces.IfingerprintReader;
import com.jiaying.workstation.utils.ZA_finger;
import com.za.android060;

import java.io.DataOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 作者：lenovo on 2016/3/8 13:44
 * 邮箱：353510746@qq.com
 * 功能：龙盾指纹识别
 */
public class LdFingerprintReader implements IfingerprintReader {

    private OnFingerprintReadCallback onFingerprintReadCallback;
    private Activity mActivity;
    private ZA_finger za_finger;
    private boolean fpflag = false;
    private boolean fpcharflag = false;
    private boolean fpmatchflag = false;
    private int fpcharbuf = 0;
    long ssart = System.currentTimeMillis();
    long ssend = System.currentTimeMillis();
    private Handler objHandler_fp;
    private HandlerThread thread;
    private static LdFingerprintReader ldFingerprintReader = null;

    android060 a6 = new android060();
    String TAG = "060";
    int DEV_ADDR = 0xffffffff;
    private Handler objHandler_3;
    String sdCardRoot = Environment
            .getExternalStorageDirectory()
            .getAbsolutePath();

    private int usborcomtype;///0 noroot  1root
    private int defDeviceType;
    private int defiCom;
    private int defiBaud;

    public LdFingerprintReader(Activity mActivity) {
        this.mActivity = mActivity;
        usborcomtype = 0;
        defDeviceType = 2;
        defiCom = 3;
        defiBaud = 12;
        thread = new HandlerThread("MyHandlerThread");
        thread.start();
        objHandler_fp = new Handler(thread.getLooper());//
        za_finger = new ZA_finger();
    }

    public synchronized static LdFingerprintReader getInstance(Activity activity) {
        if (ldFingerprintReader == null) {
            ldFingerprintReader = new LdFingerprintReader(activity);
        }
        return ldFingerprintReader;
    }
    //打开设备


    @Override
    public int open() {
        // ZA_finger.fppower(1);
        // ZA_finger.cardpower(1);
        za_finger.finger_power_on();
        try {
            thread.sleep(200);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int status = 0;
        if (1 == usborcomtype) {
            LongDunD8800_CheckEuq();
            //status = a6.ZAZOpenDeviceEx(-1, 5, 3, 12, 0, 0);
            status = a6.ZAZOpenDeviceEx(-1, defDeviceType, defiCom, defiBaud, 0, 0);
        } else {
            int fd = getrwusbdevices();
            Log.e(TAG, "zhw === open fd: " + fd);

            //try {
            //	Thread.sleep(1000);
            //} catch (InterruptedException e) {
            // TODO Auto-generated catch block
            //	e.printStackTrace();
            //}
            status = a6.ZAZOpenDeviceEx(fd, defDeviceType, defiCom, defiBaud, 0, 0);

            //a6.ZAZCloseDeviceEx();

            //fd=getrwusbdevices();
            //Log.e(TAG, "zhw === open fd: " + fd);
            //status = a6.ZAZOpenDeviceEx(fd, 5, 3, 12, 0, 0);


        }
        Log.e(TAG, " open status: " + status);
        //offLine(true);

        if (status == 1) {
            Toast.makeText(mActivity, "打开设备成功",
                    Toast.LENGTH_SHORT).show();
//            button.setText("关         闭");
        } else {
            Toast.makeText(mActivity, "打开设备失败",
                    Toast.LENGTH_SHORT).show();
        }
        return 0;
    }

    @Override
    public void read() {

        // TODO Auto-generated method stub
        fpflag = true;
        fpcharflag = true;
        fpmatchflag = true;
        try {
            thread.sleep(200);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        fpflag = false;
        readsfpimg();
//				int nRet;
//				Log.d(TAG, "Enroll: Please press finger1......");
//				while ((nRet = a6.ZAZGetImage(DEV_ADDR)) == PS_NO_FINGER) {
//
//					try {
//						Thread.sleep(50);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//
//				Log.d(TAG, " UpImage: ");
//				int[] len = { 0, 0 };
//				char[] Image = new char[256 * 288];
//				a6.ZAZUpImage(DEV_ADDR, Image, len);
//
//				Log.d(TAG, " UpImage: len " + len[0]);
//
//				String str = "/mnt/sdcard/test.bmp";
//
//				a6.ZAZImgData2BMP(Image, str);


    }

    public void readsfpimg() {
        ssart = System.currentTimeMillis();
        ssend = System.currentTimeMillis();
        objHandler_fp.postDelayed(fpTasks, 0);
    }

    private Runnable fpTasks = new Runnable() {
        public void run()// 运行该服务执行此函数
        {
            String temp = "";
            long timecount = 0;
            ssend = System.currentTimeMillis();
            timecount = (ssend - ssart);
            if (fpflag) return;
            if (timecount > Constants.COUNT_DOWN_TIME) {
                temp = "读指纹等待超时" + "\r\n";
//                mtvMessage.setText(temp);
                onFingerprintReadCallback.onFingerPrintInfo(null);
                return;
            }
            int nRet = 0;
            nRet = a6.ZAZGetImage(DEV_ADDR);
            if (nRet == 0) {
                int[] len = {0, 0};
                char[] Image = new char[256 * 288];
                a6.ZAZUpImage(DEV_ADDR, Image, len);
                String str = "/mnt/sdcard/test.bmp";
                a6.ZAZImgData2BMP(Image, str);
                temp = "获取图像成功";
//                mtvMessage.setText(temp);
                onFingerprintReadCallback.onFingerPrintInfo(null);
                Bitmap bmpDefaultPic;
                bmpDefaultPic = BitmapFactory.decodeFile(str, null);
//                mFingerprintIv.setImageBitmap(bmpDefaultPic);
                onFingerprintReadCallback.onFingerPrintInfo(bmpDefaultPic);
            } else if (nRet == a6.PS_NO_FINGER) {
//                temp = "正在读取指纹中   剩余时间:" + ((Constants.COUNT_DOWN_TIME - (ssend - ssart))) / 1000 + "s";
                temp = ((Constants.COUNT_DOWN_TIME - (ssend - ssart))) / 1000 + "";
//                mtvMessage.setText(temp);
                onFingerprintReadCallback.onFingerPrintInfo(null);
                objHandler_fp.postDelayed(fpTasks, 100);
            } else if (nRet == a6.PS_GET_IMG_ERR) {
                temp = "获取图像错误";
                Log.d(TAG, temp + "2: " + nRet);
                objHandler_fp.postDelayed(fpTasks, 100);
                //mtvMessage.setText(temp);
                onFingerprintReadCallback.onFingerPrintInfo(null);
                return;
            } else {
                temp = "设备异常";
                Log.d(TAG, temp + "2: " + nRet);
//                mtvMessage.setText(temp);
                onFingerprintReadCallback.onFingerPrintInfo(null);
                return;
            }

        }
    };

    public int LongDunD8800_CheckEuq() {
        Process process = null;
        DataOutputStream os = null;

        // for (int i = 0; i < 10; i++)
        // {
        String path = "/dev/bus/usb/00*/*";
        String path1 = "/dev/bus/usb/00*/*";
        File fpath = new File(path);
        Log.d("*** LongDun D8800 ***", " check path:" + path);
        // if (fpath.exists())
        // {
        String command = "chmod 777 " + path;
        String command1 = "chmod 777 " + path1;
        Log.d("*** LongDun D8800 ***", " exec command:" + command);
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            return 1;
        } catch (Exception e) {
            Log.d("*** DEBUG ***", "Unexpected error - Here is what I know: " + e.getMessage());
        }
        //  }
        //  }
        return 0;
    }

    /**
     *
     */
    public int getrwusbdevices() {

        // get FileDescriptor by Android USB Host API
        UsbManager mUsbManager = (UsbManager) mActivity
                .getSystemService(Context.USB_SERVICE);

        final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(mActivity, 0,
                new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        BroadcastReceiver mUsbReceiver = null;
        mActivity.registerReceiver(mUsbReceiver, filter);
        Log.i(TAG, "zhw 060");
        int fd = -1;
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            Log.i(TAG,
                    device.getDeviceName() + " "
                            + Integer.toHexString(device.getVendorId()) + " "
                            + Integer.toHexString(device.getProductId()));

            if ((device.getVendorId() == 0x2109)
                    && (0x7638 == device.getProductId())) {
                Log.d(TAG, " get FileDescriptor ");
                mUsbManager.requestPermission(device, mPermissionIntent);
                while (!mUsbManager.hasPermission(device)) {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (mUsbManager.hasPermission(device)) {
                    if (mUsbManager
                            .openDevice(device) != null) {
                        fd = mUsbManager
                                .openDevice(device).getFileDescriptor();
                        Log.d(TAG, " get FileDescriptor fd " + fd);
                        return fd;
                    } else
                        Log.e(TAG, "UsbManager openDevice failed");

                    mUsbManager
                            .openDevice(device).close();
                }
                break;
            }

        }

        return 0;
    }

    @Override
    public int close() {
        byte[] tmp = {5, 6, 7};
        //a6.ZAZBT_rev(tmp, tmp.length);
        int status = a6.ZAZCloseDeviceEx();
        Log.e(TAG, " close status: " + status);
        //offLine(false);
//        button.setText("打         开");
        //ZA_finger.fppower(0);
        //ZA_finger.cardpower(0);
        return status;
    }

    @Override
    public void setOnFingerprintReadCallback(OnFingerprintReadCallback onFingerprintReadCallback) {
        this.onFingerprintReadCallback = onFingerprintReadCallback;
    }

}

package com.jiaying.workstation.activity;

import android.app.Activity;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.Menu;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.jiaying.workstation.R;
import com.jiaying.workstation.utils.SetTopView;

/**
 * 作者：lenovo on 2016/3/12 10:07
 * 邮箱：353510746@qq.com
 * 功能：
 */
public class CameraPreviewActivity extends BaseActivity{
    private  Bitmap bitmap = null;


    SurfaceView sfvBottom,sfvTop;
    ImageView ivUI, ivFinger;
    SurfaceViewBottomCallback sfvBottomCallback;
    SurfaceViewTopCallback sfvTopCallback;
    FpGetAutofocusCallback fpGetAfCallback;
    Button btnOk, btnAgain,btnFlash;
    Bitmap bitmapUI;
    Camera camera;
    int cameraID;
    Camera.Parameters cameraParameter;
    TakeFrameCallback tfCallback;
    MsgHandler takeFrameHandler;
    String src, name, path,loginPath,succName,nameTemp;
    private final int SUCCESS=1;
    //this list used to save the areas which will be focused
    List<Camera.Area> focuAreas;

    //the size of focused area
    Rect focusRect = new Rect(-300,-300,300,300);
    int finger,fptNO;

    Camera.Area autoFocusArea;
    final int CONTINUE=0,STOP=1;
    boolean isFocus,isFlash=false;



    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        //without title, full screen, portratit preview, highlight the screen.
        Log.e("ERROR", "===3.1===");
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera_preview);
        new SetTopView(this,R.string.face_collect,true);
        Log.e("ERROR", "===3.2===");
        //底层的sfvBottom用于显示实时的预览画面，ivUI在第二层显示矩形框，sfvTop在最顶层显示动画效果，ivFinger，用于显示获取到的指纹
        sfvBottom =(SurfaceView)findViewById(R.id.sfvFrame);
        ivUI = (ImageView)findViewById(R.id.ivUI);
        ivFinger = (ImageView)findViewById(R.id.ivFinger);
        sfvTop = (SurfaceView)findViewById(R.id.sfvAnimation);

        Log.e("ERROR", "===3.3===");
        //给两个SurfaceView添加回调接口
        sfvBottomCallback = new SurfaceViewBottomCallback();
        sfvBottom.getHolder().addCallback(sfvBottomCallback);
        sfvTopCallback = new SurfaceViewTopCallback();
        sfvTop.getHolder().addCallback(sfvTopCallback);

        Log.e("ERROR", "===3.4===");
        //将最顶层的SurfaeView设置为透明,在该surface上可以画一些动画效果
        sfvTop.setZOrderOnTop(true);
        sfvTop.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        Log.e("ERROR", "===3.5===");
        //get the
        this.btnOk=(Button) this.findViewById(R.id.buttonOk);
        this.btnAgain=(Button)this.findViewById(R.id.buttonAgain);
        this.btnFlash=(Button)this.findViewById(R.id.buttonFlash);
        BtnCallback ButtonCallback = new BtnCallback();
        btnOk.setOnClickListener(ButtonCallback);
        btnAgain.setOnClickListener(ButtonCallback);
        btnFlash.setOnClickListener(ButtonCallback);
        Log.e("ERROR", "===3.6===");

        //load the openCV library
        Log.e("ERROR", "===3.7===");
        //read in the UI bitmap of collection interface
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        bitmapUI = BitmapFactory.decodeResource(this.getResources(), R.mipmap.camera_preview_ui1, options);
        Log.e("ERROR", "===3.8===");
        //获得全局的对象，该全局对象保存位图以降低不同的Activity之间传递位图的复杂度

        tfCallback = new TakeFrameCallback();
        focuAreas =new ArrayList<Camera.Area>();
        fpGetAfCallback = new FpGetAutofocusCallback();
        autoFocusArea = new Camera.Area(focusRect,1000);
        takeFrameHandler = new MsgHandler();
        finger=1;
        fptNO=1;

        Log.e("ERROR", "===3.9===");
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();

            if(bundle != null){
                //用户名以及来源
                name = bundle.getString("name").trim();
                src  = bundle.getString("src").trim();

                //如果是登陆和匹配，则有该路径为已注册的指纹特征的路径
                if(src.equals("login"))
                    path = bundle.getString("path").trim();
            }
        }
        Log.e("ERROR", "===3.10===");
    }

    @Override
    public void loadData() {

    }
    private void setCameraId(int id){
        cameraID = id;
    }
    private Camera getBackCamera() {
        Camera camera=null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();
        // get the number of camera

        for ( int camIdx = 0; camIdx < cameraCount;camIdx++ )
        {
            Camera.getCameraInfo( camIdx, cameraInfo );
            // get camerainfo of the Idth camera

            if ( cameraInfo.facing ==   CameraInfo.CAMERA_FACING_FRONT )
            // CAMERA_FACING_FRONT or CAMERA_FACING_BACK
            {
                try {
                    camera = Camera.open(camIdx);
                    setCameraId(camIdx);
                }
                catch (RuntimeException e)
                {
                    e.printStackTrace();
                }
                break;
            }
        }
        return camera;
    }

    private void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0; break;

            case Surface.ROTATION_90:
                degrees = 90; break;

            case Surface.ROTATION_180:
                degrees = 180; break;

            case Surface.ROTATION_270:
                degrees = 270; break;
        }
        int result;      if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
            // compensate the mirror
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;      }
        camera.setDisplayOrientation(result);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.camera_preview, menu);
        return true;
    }

    private final class SurfaceViewBottomCallback implements SurfaceHolder.Callback{

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


            cameraParameter = camera.getParameters();

//            cameraParameter.setPreviewSize(1920,1088);
            camera.setParameters(cameraParameter);

            try {
                //set where to display the preview data
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
            //turn on the light
            cameraParameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            CameraPreviewActivity.this.btnFlash.setText(R.string.flash_on);
            isFlash = true;
            camera.setParameters(cameraParameter);
            camera.autoFocus(fpGetAfCallback);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            //获取camera对象，给ivUI添加扫描矩形框,设置自动对焦区域
            ivUI.setImageBitmap(bitmapUI);
            camera = getBackCamera();
            focuAreas.add(autoFocusArea);
            cameraParameter = camera.getParameters();
            cameraParameter.setMeteringAreas(focuAreas);
            camera.setParameters(cameraParameter);
            setCameraDisplayOrientation(CameraPreviewActivity.this,cameraID,camera);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

            if(camera!=null){
                camera.stopPreview();
                camera.release();
                camera = null;}
        }
    }
    private final class SurfaceViewTopCallback implements SurfaceHolder.Callback{

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {


        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {


        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

    }



    private final class BtnCallback implements OnClickListener{

        @Override
        public void onClick(View btn) {
            int id = btn.getId();
            switch(id){
                case R.id.buttonOk:{
                    cameraParameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(cameraParameter);
                    isFlash = false;
                    Intent intent=new Intent();
                    intent.putExtra("name", name);//用户名
                    intent.putExtra("src", src);//来源
                    intent.putExtra("path", path);//当来源为登陆或者匹配是，为已提取特征的文件名
                    intent.setClass(CameraPreviewActivity.this, MainActivity.class);
                    startActivityForResult(intent,2);
                    //ivFinger.setVisibility(View.INVISIBLE);
                    //camera.autoFocus(fpGetAfCallback);
                }break;
                case R.id.buttonAgain:{
                    if(!isFlash){
                        cameraParameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(cameraParameter);
                        CameraPreviewActivity.this.btnFlash.setText(R.string.flash_on);
                        isFlash = true;
                    }
                    ivFinger.setVisibility(View.INVISIBLE);
                    camera.autoFocus(fpGetAfCallback);
                }break;
                case R.id.buttonFlash:{
                    if(!isFlash){
                        cameraParameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(cameraParameter);
                        CameraPreviewActivity.this.btnFlash.setText(R.string.flash_on);
                        isFlash = true;
                    }
                    else{
                        cameraParameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(cameraParameter);
                        CameraPreviewActivity.this.btnFlash.setText(R.string.flash_off);
                        isFlash = false;
                    }
                }break;
            }

        }

    }

    private final class TakeFrameCallback implements PreviewCallback{
        private int count=0;
        public void setCount(int a){
            count=a;
        }
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            count++;
            if(count==3){


                Camera.Parameters parameters=camera.getParameters();

                int imageFormat=parameters.getPreviewFormat();
                int previewWidth=parameters.getPreviewSize().width;
                int previewHeight=parameters.getPreviewSize().height;

                //将帧格式特定数据格式的数据经过转换为BitmapFactory可处理的格式
                Rect rect=new Rect(0,0,previewWidth,previewHeight);
                YuvImage yuvImg = new YuvImage(data, imageFormat, previewWidth, previewHeight, null);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
                yuvImg.compressToJpeg(rect, 100, bos);
                byte[] tmp = bos.toByteArray();
                bitmap    = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int fingerprintImageHeight =bitmap.getHeight();
                int fingerprintImageWidth =bitmap.getWidth();
                Matrix matrix=new Matrix();
                matrix.postRotate(90);
                //截取取景框中的指纹图像，并将得到的bitmap旋转
                bitmap = Bitmap.createBitmap(bitmap,
                        (fingerprintImageWidth/2)-250, (fingerprintImageHeight/2)-200, 500, 400, matrix, true);

                Message msgStop = Message.obtain();
                msgStop.what = STOP;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                takeFrameHandler.sendMessage(msgStop);
            }else{
                //发送继续拍照的消息，这段代码必须放在照片保存完成之后！因为camera.setOneShotPreviewCallback(tfCallback);这句代码是异步的。
                Message msgContinue = Message.obtain();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                msgContinue.what=CONTINUE;
                takeFrameHandler.sendMessage(msgContinue);

            }
        }
    }

    private final class MsgHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                //前count-1次继续采集
                case CONTINUE:{
                    camera.autoFocus(fpGetAfCallback);
                    break;}

                //if the countth focus, collect this image.
                case STOP:{
                    tfCallback.setCount(0);

                    ivFinger.setImageBitmap(bitmap);
                    ivFinger.setVisibility(View.VISIBLE);
                    //display the collected fingerprint image.


                    cameraParameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(cameraParameter);
                    CameraPreviewActivity.this.btnFlash.setText(R.string.flash_off);
                    isFlash = false;
                    //after collection finishs, turn off the flash light.
                    break;}
            }
        }
    }

    private final class FpGetAutofocusCallback implements AutoFocusCallback{

        @Override
        public void onAutoFocus(boolean success, Camera camera) {

            if(success){
                isFocus = true;
                camera.setOneShotPreviewCallback(tfCallback);
                //successful focus,set isFocus true.
            }
            else{
                isFocus = false;
                camera.setOneShotPreviewCallback(tfCallback);
                //failing focus,set isFocus true.
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        switch(resultCode){
            case 1:
                //successful
                if(src.equals("register")){
                    succName = name+"_register_minu.txt";
                    Intent dataintent = new Intent();
                    dataintent.putExtra("name", succName);
                    CameraPreviewActivity.this.setResult(SUCCESS,dataintent);
                }else{
                    CameraPreviewActivity.this.setResult(SUCCESS,data);
                }
                CameraPreviewActivity.this.finish();
                break;
            case 0:
                //failing
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}

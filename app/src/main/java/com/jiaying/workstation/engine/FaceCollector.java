package com.jiaying.workstation.engine;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jiaying.workstation.interfaces.IfaceCollector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lenovo on 2016/3/15 07:28
 * 邮箱：353510746@qq.com
 * 功能：
 */
public class FaceCollector implements IfaceCollector {
    private Bitmap bitmap = null;
    private OnFaceCollectCallback onFaceCollectCallback;
    private Activity activity;
    SurfaceView sfvBottom;
    SurfaceViewBottomCallback sfvBottomCallback;
    private static FaceCollector faceCollector = null;
    Camera camera;
    int cameraID;
    Camera.Parameters cameraParameter;
    boolean isFocus, isFlash = false;
    private final int SUCCESS = 1;
    //this list used to save the areas which will be focused
    List<Camera.Area> focuAreas;

    //the size of focused area
    Rect focusRect = new Rect(-300, -300, 300, 300);
    int finger, fptNO;
    FpGetAutofocusCallback fpGetAfCallback;
    Camera.Area autoFocusArea;
    final int CONTINUE = 0, STOP = 1;
    TakeFrameCallback tfCallback;
    MsgHandler takeFrameHandler;


    private FaceCollector(Activity activity, SurfaceView sfvBottom) {
        this.activity = activity;
        this.sfvBottom = sfvBottom;
        //SurfaceView添加回调接口
        sfvBottomCallback = new SurfaceViewBottomCallback();
        sfvBottom.getHolder().addCallback(sfvBottomCallback);
        tfCallback = new TakeFrameCallback();
        focuAreas = new ArrayList<Camera.Area>();
        fpGetAfCallback = new FpGetAutofocusCallback();
        autoFocusArea = new Camera.Area(focusRect, 1000);
        takeFrameHandler = new MsgHandler();
        finger = 1;
        fptNO = 1;

    }

    public synchronized static FaceCollector getInstance(Activity activity, SurfaceView sfvBottom) {
        if (faceCollector == null) {
            faceCollector = new FaceCollector(activity, sfvBottom);

        }
        return faceCollector;
    }

    private final class MsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //前count-1次继续采集
                case CONTINUE: {
                   if(camera != null){
                       camera.autoFocus(fpGetAfCallback);
                   }
                    break;
                }

                //if the countth focus, collect this image.
                case STOP: {
                    onFaceCollectCallback.onCollect(bitmap);
                    close();
                    break;
                }
            }
        }
    }


    @Override
    public int close() {
        tfCallback.setCount(0);
        cameraParameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(cameraParameter);
        isFlash = false;
//        if (bitmap != null) {
//            bitmap.recycle();
//            bitmap = null;
//        }
        return 0;
    }

    @Override
    public void collect() {
        cameraParameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(cameraParameter);
        camera.autoFocus(fpGetAfCallback);
    }

    @Override
    public int open() {

        return 0;
    }

    @Override
    public void setOnColectCallback(OnFaceCollectCallback onFaceCollectCallback) {
        this.onFaceCollectCallback = onFaceCollectCallback;
    }


    private final class SurfaceViewBottomCallback implements SurfaceHolder.Callback {

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
            isFlash = true;
            camera.setParameters(cameraParameter);
            camera.autoFocus(fpGetAfCallback);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            //获取camera对象，给ivUI添加扫描矩形框,设置自动对焦区域
//            ivUI.setImageBitmap(bitmapUI);
            camera = getBackCamera();
            focuAreas.add(autoFocusArea);
            cameraParameter = camera.getParameters();
            cameraParameter.setMeteringAreas(focuAreas);
            camera.setParameters(cameraParameter);
            setCameraDisplayOrientation(activity, cameraID, camera);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        }
    }

    private void setCameraId(int id) {
        cameraID = id;
    }

    private void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;

            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
            // compensate the mirror
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }


    private Camera getBackCamera() {
        Camera camera = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();
        // get the number of camera

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            // get camerainfo of the Idth camera

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
            // CAMERA_FACING_FRONT or CAMERA_FACING_BACK
            {
                try {
                    camera = Camera.open(camIdx);
                    setCameraId(camIdx);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return camera;
    }

    private final class FpGetAutofocusCallback implements Camera.AutoFocusCallback {

        @Override
        public void onAutoFocus(boolean success, Camera camera) {

            if (success) {
                isFocus = true;
                camera.setOneShotPreviewCallback(tfCallback);
                //successful focus,set isFocus true.
            } else {
                isFocus = false;
                camera.setOneShotPreviewCallback(tfCallback);
                //failing focus,set isFocus true.
            }
        }
    }

    private final class TakeFrameCallback implements Camera.PreviewCallback {
        private int count = 0;

        public void setCount(int a) {
            count = a;
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            count++;
            if (count == 3) {


                Camera.Parameters parameters = camera.getParameters();

                int imageFormat = parameters.getPreviewFormat();
                int previewWidth = parameters.getPreviewSize().width;
                int previewHeight = parameters.getPreviewSize().height;

                //将帧格式特定数据格式的数据经过转换为BitmapFactory可处理的格式
                Rect rect = new Rect(0, 0, previewWidth, previewHeight);
                YuvImage yuvImg = new YuvImage(data, imageFormat, previewWidth, previewHeight, null);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
                yuvImg.compressToJpeg(rect, 100, bos);
                byte[] tmp = bos.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int fingerprintImageHeight = bitmap.getHeight();
                int fingerprintImageWidth = bitmap.getWidth();
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                //截取取景框中的指纹图像，并将得到的bitmap旋转
                bitmap = Bitmap.createBitmap(bitmap,
                        (fingerprintImageWidth / 2) - 250, (fingerprintImageHeight / 2) - 200, 500, 400, matrix, true);

                Message msgStop = Message.obtain();
                msgStop.what = STOP;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                takeFrameHandler.sendMessage(msgStop);
            } else {
                //发送继续拍照的消息，这段代码必须放在照片保存完成之后！因为camera.setOneShotPreviewCallback(tfCallback);这句代码是异步的。
                Message msgContinue = Message.obtain();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                msgContinue.what = CONTINUE;
                takeFrameHandler.sendMessage(msgContinue);

            }
        }
    }

}

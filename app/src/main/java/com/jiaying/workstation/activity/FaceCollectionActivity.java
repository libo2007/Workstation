package com.jiaying.workstation.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.jiaying.workstation.R;
import com.jiaying.workstation.utils.SetTopView;

import android.hardware.Camera;

import java.io.IOException;

/*
人脸采集
 */
public class FaceCollectionActivity extends BaseActivity implements
        SurfaceHolder.Callback {
    private SurfaceView mCameraPreview;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_face_collection);
        new SetTopView(this, R.string.title_activity_face_collection, false);
        mCameraPreview = (SurfaceView) findViewById(R.id.sv_camera);
        mSurfaceHolder = mCameraPreview.getHolder();

        //人脸采集后就到登记成功页面
        new Handler().postDelayed(new runnable(), 3000);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.checkCameraHardware(this) && (mCamera == null)) {
            mCamera = getCamera();
            if (mSurfaceHolder != null) {
                setStartPreview(mCamera, mSurfaceHolder);
            }
        }
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /**
     * 初始化相机
     *
     * @return camera
     */
    private Camera getCamera() {
        Camera camera;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            camera = null;
        }
        return camera;
    }

    //预览
    private void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            // camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    是否支持摄像头
     */
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (mSurfaceHolder.getSurface() == null) {
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setStartPreview(mCamera, mSurfaceHolder);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        releaseCamera();

    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private class runnable implements Runnable {
        @Override
        public void run() {
            Toast.makeText(FaceCollectionActivity.this, "人脸采集成功", Toast.LENGTH_SHORT).show();
        }
    }
}

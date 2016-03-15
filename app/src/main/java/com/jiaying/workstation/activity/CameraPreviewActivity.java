package com.jiaying.workstation.activity;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.jiaying.workstation.R;
import com.jiaying.workstation.engine.FaceCollector;
import com.jiaying.workstation.engine.ProxyFaceCollector;
import com.jiaying.workstation.interfaces.IfaceCollector;
import com.jiaying.workstation.utils.SetTopView;
import com.jiaying.workstation.utils.ToastUtils;

/**
 * 作者：lenovo on 2016/3/12 10:07
 * 邮箱：353510746@qq.com
 * 功能：
 */
public class CameraPreviewActivity extends BaseActivity implements IfaceCollector.OnFaceCollectCallback {
    SurfaceView sfvBottom;


    SurfaceView sfvTop;
    ImageView ivUI, ivFinger;
    SurfaceViewTopCallback sfvTopCallback;
    Button btnOk, btnAgain, btnFlash;
    Bitmap bitmapUI;
    String src, name, path, loginPath, succName, nameTemp;

    private IfaceCollector ifaceCollector;
    private ProxyFaceCollector proxyFaceCollector;

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
        new SetTopView(this, R.string.face_collect, true);
        Log.e("ERROR", "===3.2===");
        //底层的sfvBottom用于显示实时的预览画面，ivUI在第二层显示矩形框，sfvTop在最顶层显示动画效果，ivFinger，用于显示获取到的指纹
        sfvBottom = (SurfaceView) findViewById(R.id.sfvFrame);

        ivUI = (ImageView) findViewById(R.id.ivUI);
        ivFinger = (ImageView) findViewById(R.id.ivFinger);
        sfvTop = (SurfaceView) findViewById(R.id.sfvAnimation);

        Log.e("ERROR", "===3.3===");

        sfvTopCallback = new SurfaceViewTopCallback();
        sfvTop.getHolder().addCallback(sfvTopCallback);

        Log.e("ERROR", "===3.4===");
        //将最顶层的SurfaeView设置为透明,在该surface上可以画一些动画效果
        sfvTop.setZOrderOnTop(true);
        sfvTop.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        Log.e("ERROR", "===3.5===");
        //get the
        this.btnOk = (Button) this.findViewById(R.id.buttonOk);
        this.btnAgain = (Button) this.findViewById(R.id.buttonAgain);
        this.btnFlash = (Button) this.findViewById(R.id.buttonFlash);
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
        //预览框去掉
//        bitmapUI = BitmapFactory.decodeResource(this.getResources(), R.mipmap.camera_preview_ui1, options);
        Log.e("ERROR", "===3.8===");
        //获得全局的对象，该全局对象保存位图以降低不同的Activity之间传递位图的复杂度


        Log.e("ERROR", "===3.9===");
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                //用户名以及来源
                name = bundle.getString("name").trim();
                src = bundle.getString("src").trim();

                //如果是登陆和匹配，则有该路径为已注册的指纹特征的路径
                if (src.equals("login"))
                    path = bundle.getString("path").trim();
            }
        }
        Log.e("ERROR", "===3.10===");

        //采浆人脸
        ifaceCollector = FaceCollector.getInstance(this, sfvBottom);
        proxyFaceCollector = ProxyFaceCollector.getInstance(ifaceCollector);
//        proxyFaceCollector.open();
//        proxyFaceCollector.collect();
        proxyFaceCollector.setOnColectCallback(this);
    }

    @Override
    public void loadData() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.camera_preview, menu);
        return true;
    }


    private final class SurfaceViewTopCallback implements SurfaceHolder.Callback {

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


    private final class BtnCallback implements OnClickListener {

        @Override
        public void onClick(View btn) {
            int id = btn.getId();
            switch (id) {
                case R.id.buttonOk: {
                    ToastUtils.showToast(CameraPreviewActivity.this,R.string.face_collect_sucess);
                    finish();
//                    cameraParameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                    camera.setParameters(cameraParameter);
//                    isFlash = false;
//                    Intent intent=new Intent();
//                    intent.putExtra("name", name);//用户名
//                    intent.putExtra("src", src);//来源
//                    intent.putExtra("path", path);//当来源为登陆或者匹配是，为已提取特征的文件名
//                    intent.setClass(CameraPreviewActivity.this, MainActivity.class);
//                    startActivityForResult(intent,2);
                    //ivFinger.setVisibility(View.INVISIBLE);
                    //camera.autoFocus(fpGetAfCallback);
                }
                break;
                case R.id.buttonAgain: {
//                    if(!isFlash){
//                        cameraParameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//                        camera.setParameters(cameraParameter);
//                        CameraPreviewActivity.this.btnFlash.setText(R.string.flash_on);
//                        isFlash = true;
//                    }
                    ivFinger.setVisibility(View.INVISIBLE);
//                    camera.autoFocus(fpGetAfCallback);
                    proxyFaceCollector.collect();
                }
                break;
                case R.id.buttonFlash: {
//                    if(!isFlash){
//                        cameraParameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//                        camera.setParameters(cameraParameter);
//                        CameraPreviewActivity.this.btnFlash.setText(R.string.flash_on);
//                        isFlash = true;
//                    }
//                    else{
//                        cameraParameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                        camera.setParameters(cameraParameter);
//                        CameraPreviewActivity.this.btnFlash.setText(R.string.flash_off);
//                        isFlash = false;
//                    }
                }
                break;
            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        switch (resultCode) {
            case 1:
                //successful
//                if(src.equals("register")){
//                    succName = name+"_register_minu.txt";
//                    Intent dataintent = new Intent();
//                    dataintent.putExtra("name", succName);
//                    CameraPreviewActivity.this.setResult(SUCCESS,dataintent);
//                }else{
//                    CameraPreviewActivity.this.setResult(SUCCESS,data);
//                }
//                CameraPreviewActivity.this.finish();
                break;
            case 0:
                //failing
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCollect(Bitmap bitmap) {
        if (bitmap != null) {
            ivFinger.setVisibility(View.VISIBLE);
            ivFinger.setImageBitmap(bitmap);
        }
    }
}

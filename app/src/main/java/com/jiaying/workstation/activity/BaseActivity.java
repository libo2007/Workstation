package com.jiaying.workstation.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jiaying.workstation.R;

/**
 * activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initVariables();
        loadData();
    }

    //初始化变量，包括Intent带的数据和Activity内的变量
    public abstract void initVariables();

    //加载layout布局文件，初始化控件，为控件挂上时间方法
    public abstract void initView();

    // 调用服务器API加载数据
    public abstract void loadData();

}

package com.jiaying.workstation.activity;

import android.os.Bundle;

import com.jiaying.workstation.R;
import com.jiaying.workstation.utils.SetTopView;

/**
 * 体检项目_询问
 */
public class PhysicalExamAskActivity extends BaseActivity {
    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_physical_exam_ask);
        new SetTopView(this,R.string.title_activity_physical_exam_ask,true);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}

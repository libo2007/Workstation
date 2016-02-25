package com.jiaying.workstation.activity;

import android.os.Bundle;

import com.jiaying.workstation.R;
import com.jiaying.workstation.utils.SetTopView;

/**
 * 体检中
 */
public class PhysicalExamingActivity extends BaseActivity {
    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_physical_examing);
        new SetTopView(this,R.string.title_activity_physical_examing,true);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}

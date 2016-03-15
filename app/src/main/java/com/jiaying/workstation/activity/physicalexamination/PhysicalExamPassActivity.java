package com.jiaying.workstation.activity.physicalexamination;

import android.os.Bundle;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.BaseActivity;
import com.jiaying.workstation.utils.SetTopView;

/**
 * 体检通过的
 */
public class PhysicalExamPassActivity extends BaseActivity {
    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_physical_exam_pass);
        new SetTopView(this,R.string.title_activity_physical_pass,true);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}

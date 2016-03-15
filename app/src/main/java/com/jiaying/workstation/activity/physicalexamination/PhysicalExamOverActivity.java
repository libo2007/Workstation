package com.jiaying.workstation.activity.physicalexamination;

import android.os.Bundle;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.BaseActivity;
import com.jiaying.workstation.utils.SetTopView;

/**
 * 体检中
 */
public class PhysicalExamOverActivity extends BaseActivity {
    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_physical_exam_over);
        new SetTopView(this,R.string.title_activity_physical_over,true);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}

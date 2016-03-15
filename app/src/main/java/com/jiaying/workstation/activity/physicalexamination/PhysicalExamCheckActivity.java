package com.jiaying.workstation.activity.physicalexamination;

import android.os.Bundle;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.BaseActivity;
import com.jiaying.workstation.utils.SetTopView;

/**
 * 体检项目_体格检查
 */
public class PhysicalExamCheckActivity extends BaseActivity {
    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_physical_exam_check);
        new SetTopView(this,R.string.title_activity_physical_exam_check,true);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}

package com.jiaying.workstation.activity.physicalexamination;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.BaseActivity;
import com.jiaying.workstation.utils.SetTopView;

/**
 * 体检项目_不能献浆或者暂不能献浆的备注
 */
public class PhysicalExamNoteActivity extends BaseActivity {
    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_physical_exam_note);
        new SetTopView(this,R.string.title_activity_physical_exam_note,true);

    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}

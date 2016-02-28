package com.jiaying.workstation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jiaying.workstation.R;
import com.jiaying.workstation.utils.SetTopView;

/**
 * 体检项目_询问
 */
public class PhysicalExamAskActivity extends BaseActivity {
    private Button note_btn;

    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_physical_exam_ask);
        new SetTopView(this, R.string.title_activity_physical_exam_ask, true);
        note_btn = (Button) findViewById(R.id.note_btn);
        note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //备注
                Intent intent= new Intent(PhysicalExamAskActivity.this, PhysicalExamNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}

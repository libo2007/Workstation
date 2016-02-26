package com.jiaying.workstation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jiaying.workstation.R;
import com.jiaying.workstation.utils.SetTopView;

/**
 * 体检项目页面
 */
public class PhysicalExamActivity extends BaseActivity {
    private Button ask_btn;
    private Button check_btn;
    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_physical_exam);
        new SetTopView(this,R.string.title_activity_physical_exam,true);
        ask_btn = (Button) findViewById(R.id.ask_btn);
        ask_btn.setOnClickListener(new AskClickListener());

        check_btn = (Button) findViewById(R.id.check_btn);
        check_btn.setOnClickListener(new CheckClickListener());
    }

    //询问
    private class AskClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent it = new Intent(PhysicalExamActivity.this,PhysicalExamAskActivity.class);
            startActivity(it);
        }
    }
    //体格检查
    private class CheckClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent it = new Intent(PhysicalExamActivity.this,PhysicalExamCheckActivity.class);
            startActivity(it);
        }
    }
    @Override
    public void loadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}

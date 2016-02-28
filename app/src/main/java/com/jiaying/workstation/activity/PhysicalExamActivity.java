package com.jiaying.workstation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Type;
import android.view.View;
import android.widget.Button;

import com.jiaying.workstation.R;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;
import com.jiaying.workstation.utils.SetTopView;

/**
 * 体检项目页面
 */
public class PhysicalExamActivity extends BaseActivity {
    private Button ask_btn;
    private Button check_btn;
    private Button submit_btn;

    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_physical_exam);
        new SetTopView(this, R.string.title_activity_physical_exam, true);
        ask_btn = (Button) findViewById(R.id.ask_btn);
        ask_btn.setOnClickListener(new AskClickListener());

        check_btn = (Button) findViewById(R.id.check_btn);
        check_btn.setOnClickListener(new CheckClickListener());
        submit_btn = (Button) findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new SubmitClickListener());
    }

    //询问
    private class AskClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent it = new Intent(PhysicalExamActivity.this, PhysicalExamAskActivity.class);
            startActivity(it);
        }
    }

    //体格检查
    private class CheckClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent it = new Intent(PhysicalExamActivity.this, PhysicalExamCheckActivity.class);
            startActivity(it);
        }
    }

    //提交审核
    private class SubmitClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //1.判断是否填写完毕 2.填写完毕了再提交审核-2.1.献浆员打指纹  2.医生打指纹 3.返回体检结果
            Intent it = new Intent(PhysicalExamActivity.this, FingerprintActivity.class);
            it.putExtra(IntentExtra.EXTRA_TYPE, TypeConstant.TYPE_PHYSICAL_EXAM_SUBMIT_XJ);
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

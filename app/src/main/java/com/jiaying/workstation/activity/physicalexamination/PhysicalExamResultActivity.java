package com.jiaying.workstation.activity.physicalexamination;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.BaseActivity;
import com.jiaying.workstation.activity.DispatchStateListActivity;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;
import com.jiaying.workstation.utils.SetTopView;

/**
 * 体检审核结果展示
 */
public class PhysicalExamResultActivity extends BaseActivity {
    private boolean isPass = false;//是否通过体检

    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_physical_exam_result);
        new SetTopView(this, R.string.title_activity_physical_exam_result, true);
        showResultDialog();
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void showResultDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.physical_not_pass);
        builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                physicalExam();
            }
        });
        builder.setNegativeButton(R.string.physsical_again, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                physicalExam();
            }
        });
        builder.create().show();
    }

    //进入待体检列表
    private void physicalExam() {
        Intent it = new Intent(PhysicalExamResultActivity.this, DispatchStateListActivity.class);
        it.putExtra(IntentExtra.EXTRA_STATE, TypeConstant.STATE_PHYSICAL_EXAM_TODO);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
        finish();
    }
}

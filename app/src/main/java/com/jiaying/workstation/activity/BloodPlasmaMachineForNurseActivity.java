package com.jiaying.workstation.activity;

import android.os.Bundle;

import com.jiaying.workstation.R;
import com.jiaying.workstation.utils.SetTopView;

/**
 * 护士分配浆机
 */
public class BloodPlasmaMachineForNurseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_plasma_machine_for_nurse);
        new SetTopView(this, R.string.title_activity_pulp_machine_for_nurse, true);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void initVariables() {

    }
}
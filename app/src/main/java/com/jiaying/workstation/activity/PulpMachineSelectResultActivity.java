package com.jiaying.workstation.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jiaying.workstation.R;
import com.jiaying.workstation.utils.SetTopView;

public class PulpMachineSelectResultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
    setContentView(R.layout.activity_pulp_machine_select_result);
        new SetTopView(this,R.string.title_activity_pulp_machine_select_result,true);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void initVariables() {

    }

}

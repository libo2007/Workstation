package com.jiaying.workstation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jiaying.workstation.R;
import com.jiaying.workstation.adapter.NurseAdapter;
import com.jiaying.workstation.entity.Nurse;
import com.jiaying.workstation.utils.SetTopView;

import java.util.ArrayList;
import java.util.List;

/**
 * 护士分配浆机
 */
public class LoginActivity extends BaseActivity {
    private GridView mGridView;
    private NurseAdapter mAdapter;
    private List<Nurse> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        new SetTopView(this, R.string.title_activity_pulp_machine_for_nurse, false);
        mGridView = (GridView) findViewById(R.id.gridview);
        mList = new ArrayList<Nurse>();
        mAdapter = new NurseAdapter(mList,this);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //选择护士后就指纹认证
                Intent it = new Intent(LoginActivity.this,FingerprintActivity.class);
                startActivity(it);
            }
        });
        for(int i = 0;i < 30;i++){
            Nurse nurse = new Nurse();
            nurse.setName("护士" + i);
            nurse.setWorkid("1232" + i);
            mList.add(nurse);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadData() {

    }






}
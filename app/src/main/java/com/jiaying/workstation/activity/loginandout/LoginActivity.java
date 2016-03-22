package com.jiaying.workstation.activity.loginandout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.BaseActivity;
import com.jiaying.workstation.activity.sensor.FingerprintActivity;
import com.jiaying.workstation.adapter.NurseAdapter;
import com.jiaying.workstation.entity.NurseEntity;
import com.jiaying.workstation.utils.DealFlag;
import com.jiaying.workstation.utils.SetTopView;

import java.util.ArrayList;
import java.util.List;

/**
 * 护士分配浆机
 */
public class LoginActivity extends BaseActivity {
    private GridView mGridView;
    private NurseAdapter mAdapter;
    private List<NurseEntity> mList;
    private DealFlag login_deal_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login_deal_flag = new DealFlag();
    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        new SetTopView(this, R.string.title_activity_pulp_machine_for_nurse, false);
        mGridView = (GridView) findViewById(R.id.gridview);
        mList = new ArrayList<NurseEntity>();
        mAdapter = new NurseAdapter(mList, this);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //选择护士后就指纹认证
                if (login_deal_flag.isFirst()) {
                    Intent it = new Intent(LoginActivity.this, FingerprintActivity.class);
                    startActivity(it);
                }
            }
        });
        for (int i = 0; i < 30; i++) {
            NurseEntity nurseEntity = new NurseEntity();
            nurseEntity.setName("护士" + i);
            nurseEntity.setWorkid("1232" + i);
            mList.add(nurseEntity);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        login_deal_flag.reset();
    }
}
package com.jiaying.workstation.activity.plasmacollection;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.BaseActivity;
import com.jiaying.workstation.adapter.PlasmaMachineSelectAdapter;
import com.jiaying.workstation.entity.PlasmaMachineEntity;
import com.jiaying.workstation.utils.SetTopView;

import java.util.ArrayList;
import java.util.List;

public class SelectPlasmaMachineActivity extends BaseActivity {
    private GridView mGridView;
    private List<PlasmaMachineEntity> mList;
    private PlasmaMachineSelectAdapter mAdapter;
    private TextView sure_txt;//确定分配

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void initView() {
        setContentView(R.layout.activity_pulp_machine_select);
        new SetTopView(this, R.string.title_activity_pulp_machine_select, true);
        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(SelectPlasmaMachineActivity.this,SelectPlasmaMachineResultActivity.class);
                startActivity(it);
            }
        });
        mList = new ArrayList<PlasmaMachineEntity>();
        mAdapter = new PlasmaMachineSelectAdapter(mList, this);
        mGridView.setAdapter(mAdapter);

        for (int i = 0; i < 10; i++) {
            PlasmaMachineEntity machine = new PlasmaMachineEntity();
            if (i % 2 == 0) {
                machine.setCheck(true);
            } else {
                machine.setCheck(false);
            }
            machine.setId(i + "");
            machine.setNumber(i + "号");
            mList.add(machine);
        }
        mAdapter.notifyDataSetChanged();

        sure_txt = (TextView) findViewById(R.id.sure_txt);
        sure_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectSize = 0;
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).isCheck()) {
                        selectSize++;
                    }
                }

            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public void initVariables() {

    }


}

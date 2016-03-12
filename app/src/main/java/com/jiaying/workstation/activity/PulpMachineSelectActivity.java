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
import android.widget.ListView;
import android.widget.TextView;

import com.jiaying.workstation.R;
import com.jiaying.workstation.adapter.PulpMachineSelectAdapter;
import com.jiaying.workstation.entity.PulpMachine;
import com.jiaying.workstation.utils.SetTopView;

import java.util.ArrayList;
import java.util.List;

public class PulpMachineSelectActivity extends BaseActivity {
    private ListView mListView;
    private List<PulpMachine> mList;
    private PulpMachineSelectAdapter mAdapter;
    private TextView sure_txt;//确定分配

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void initView() {
        setContentView(R.layout.activity_pulp_machine_select);
        new SetTopView(this, R.string.title_activity_pulp_machine_select, true);
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(PulpMachineSelectActivity.this,PulpMachineSelectResultActivity.class);
                startActivity(it);
            }
        });
        mList = new ArrayList<PulpMachine>();
        mAdapter = new PulpMachineSelectAdapter(mList, this);
        mListView.setAdapter(mAdapter);

        for (int i = 0; i < 10; i++) {
            PulpMachine machine = new PulpMachine();
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

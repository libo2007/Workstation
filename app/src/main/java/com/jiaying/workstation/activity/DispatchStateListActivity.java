package com.jiaying.workstation.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.jiaying.workstation.R;
import com.jiaying.workstation.adapter.DispathStateListAdapter;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;
import com.jiaying.workstation.entity.User;
import com.jiaying.workstation.utils.MyLog;
import com.jiaying.workstation.utils.SetTopView;

import java.util.ArrayList;
import java.util.List;

/**
 * 调度状态列表（包括登记完成，体检中，采浆中等的列表显示页）
 */
public class DispatchStateListActivity extends BaseActivity {
    private static final String TAG = "DispatchStateListActivity";
    private ListView mListView;
    private List<User> mList;
    private DispathStateListAdapter mAdapter;
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initVariables() {
        state = getIntent().getIntExtra(IntentExtra.EXTRA_STATE, 0);
        MyLog.e(TAG, "state:" + state);
    }

    @Override
    public void initView() {

        setContentView(R.layout.activity_dispatch_state_list);
        new SetTopView(this, R.string.title_activity_dispatch_state, true);
        mListView = (ListView) findViewById(R.id.listview);
        mList = new ArrayList<User>();
        mAdapter = new DispathStateListAdapter(mList, this);
        mListView.setAdapter(mAdapter);
        mAdapter.setmState(state);
        switch (state) {
            case TypeConstant.STATE_REG_OVER:
                //登记完成
                new SetTopView(this, R.string.title_activity_register_over, true);
                break;
            case TypeConstant.STATE_PHYSICAL_EXAM_TODO:
                //待体检
                new SetTopView(this, R.string.title_activity_physical_todo, true);
                break;
            case TypeConstant.STATE_PHYSICAL_EXAMING:
                //体检中
                new SetTopView(this, R.string.title_activity_physical_examing, true);
                break;
            case TypeConstant.STATE_PHYSICAL_EXAM_PASS:
                //体检通过
                new SetTopView(this, R.string.title_activity_physical_pass, true);
                break;
            case TypeConstant.STATE_PHYSICAL_EXAM_NOT_PASS:
                //体检未通过
                new SetTopView(this, R.string.title_activity_physical_not_pass, true);
                break;
            case TypeConstant.STATE_BLOODPLASMA_COLLECTION_TODO:
                //待采浆
                new SetTopView(this, R.string.title_activity_bloodplasma_collection_todo, true);
                break;
            case TypeConstant.STATE_BLOODPLASMA_COLLECTING:
                //采浆中
                new SetTopView(this, R.string.title_activity_bloodplasma_collecting, true);
                break;
            case TypeConstant.STATE_BLOODPLASMA_COLLECTION_OVER:
                //采浆完成
                new SetTopView(this, R.string.title_activity_bloodplasma_collection_over, true);
                break;
        }
    }

    @Override
    public void loadData() {
        //测试数据
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("李三");
            user.setId("证件号：5110231987****0574");
            mList.add(user);
        }
        mAdapter.notifyDataSetChanged();
    }


}

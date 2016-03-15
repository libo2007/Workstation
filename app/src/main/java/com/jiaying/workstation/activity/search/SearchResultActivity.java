package com.jiaying.workstation.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.BaseActivity;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;
import com.jiaying.workstation.utils.SetTopView;

/*
查询结果展示
 */
public class SearchResultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initVariables() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_search_result);
        new SetTopView(this, R.string.title_activity_serch_result, true);
    }

    @Override
    public void loadData() {

    }
}


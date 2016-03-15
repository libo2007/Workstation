package com.jiaying.workstation.activity.plasmacollection;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.BaseActivity;
import com.jiaying.workstation.activity.DispatchStateListActivity;
import com.jiaying.workstation.constant.Constants;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;
import com.jiaying.workstation.interfaces.OnCountDownTimerFinishCallback;
import com.jiaying.workstation.utils.CountDownTimerUtil;
import com.jiaying.workstation.utils.SetTopView;
import com.jiaying.workstation.utils.ToastUtils;

//浆机分配给浆员的结果
public class SelectPlasmaMachineResultActivity extends BaseActivity implements OnCountDownTimerFinishCallback {
    private CountDownTimerUtil countDownTimerUtil;
    private TextView time_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_pulp_machine_select_result);
        new SetTopView(this, R.string.title_activity_pulp_machine_select_result, false);
        time_txt = (TextView) findViewById(R.id.time_txt);

        //倒计时开始
        countDownTimerUtil = CountDownTimerUtil.getInstance(time_txt, this);
        countDownTimerUtil.start(Constants.COUNT_DOWN_TIME_5S);
        countDownTimerUtil.setOnCountDownTimerFinishCallback(this);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void initVariables() {

    }

    @Override
    public void onFinish() {
        ToastUtils.showToast(this, R.string.identify_time_out);
        Intent it = new Intent(SelectPlasmaMachineResultActivity.this, DispatchStateListActivity.class);
        it.putExtra(IntentExtra.EXTRA_STATE, TypeConstant.STATE_BLOODPLASMA_COLLECTION_TODO);
        startActivity(it);
        finish();
    }
}

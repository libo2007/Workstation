package com.jiaying.workstation.activity.plasmacollection;

import android.content.Intent;
import android.os.Bundle;
import android.softfan.dataCenter.DataCenterClientService;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.widget.TextView;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.BaseActivity;
import com.jiaying.workstation.activity.DispatchStateListActivity;
import com.jiaying.workstation.constant.Constants;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;
import com.jiaying.workstation.thread.ObservableZXDCSignalListenerThread;
import com.jiaying.workstation.interfaces.OnCountDownTimerFinishCallback;
import com.jiaying.workstation.utils.CountDownTimerUtil;
import com.jiaying.workstation.utils.MyLog;
import com.jiaying.workstation.utils.SetTopView;
import com.jiaying.workstation.utils.ToastUtils;

import java.util.Date;
import java.util.HashMap;

//浆机分配给浆员的结果
public class SelectPlasmaMachineResultActivity extends BaseActivity implements OnCountDownTimerFinishCallback {
    private static final String TAG = "SelectPlasmaMachineResultActivity";
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

        sendToTcpIpServer();
    }

    @Override
    public void onFinish() {
        ToastUtils.showToast(this, R.string.identify_time_out);
        Intent it = new Intent(SelectPlasmaMachineResultActivity.this, DispatchStateListActivity.class);
        it.putExtra(IntentExtra.EXTRA_STATE, TypeConstant.STATE_BLOODPLASMA_COLLECTION_TODO);
        startActivity(it);
        finish();
    }

    //将浆员信息发送到服务器
    private void sendToTcpIpServer(){
        DataCenterClientService clientService = ObservableZXDCSignalListenerThread.getClientService();
       if(clientService != null){
           DataCenterTaskCmd retcmd = new DataCenterTaskCmd();
//       retcmd.setSelfNotify(this);
           retcmd.setCmd("faceRecognition");
           retcmd.setHasResponse(true);
           retcmd.setLevel(2);
           HashMap<String, Object> values = new HashMap<String, Object>();
           values.put("face", "face");
           values.put("face_w", "face_w");
           values.put("face_h", "face_h");
           values.put("faceType", "faceType");
           values.put("date", new Date(System.currentTimeMillis()));
           retcmd.setValues(values);
           clientService.getApDataCenter().addSendCmd(retcmd);
       }else{
           MyLog.e(TAG,"clientService==null");
       }

    }
}

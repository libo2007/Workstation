package com.jiaying.workstation.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.DispatchStateListActivity;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;

/**
 * 调度
 */
public class DispatchFragment extends Fragment {

    private Button register_over_btn;
    private Button physical_examing_btn;
    private Button physical_exam_todo_btn;
    private Button physical_exam_pass_btn;
    private Button physical_exam_not_pass_btn;
    private Button blood_plasma_todo_btn;
    private Button blood_plasma_collecting_btn;
    private Button blood_plasma_collect_over_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dispatch, container, false);
        register_over_btn = (Button) view.findViewById(R.id.register_over_btn);
        register_over_btn.setOnClickListener(new ClickListener());
        physical_examing_btn = (Button) view.findViewById(R.id.physical_examing_btn);
        physical_examing_btn.setOnClickListener(new ClickListener());
        physical_exam_todo_btn = (Button) view.findViewById(R.id.physical_exam_todo_btn);
        physical_exam_todo_btn.setOnClickListener(new ClickListener());
        physical_exam_pass_btn = (Button) view.findViewById(R.id.physical_exam_pass_btn);
        physical_exam_pass_btn.setOnClickListener(new ClickListener());
        physical_exam_not_pass_btn = (Button) view.findViewById(R.id.physical_exam_not_pass_btn);
        physical_exam_not_pass_btn.setOnClickListener(new ClickListener());
        blood_plasma_todo_btn = (Button) view.findViewById(R.id.blood_plasma_todo_btn);
        blood_plasma_todo_btn.setOnClickListener(new ClickListener());
        blood_plasma_collecting_btn = (Button) view.findViewById(R.id.blood_plasma_collecting_btn);
        blood_plasma_collecting_btn.setOnClickListener(new ClickListener());
        blood_plasma_collect_over_btn = (Button) view.findViewById(R.id.blood_plasma_collect_over_btn);
        blood_plasma_collect_over_btn.setOnClickListener(new ClickListener());
        return view;
    }

    //点击事件
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent it = new Intent(getActivity(), DispatchStateListActivity.class);
            switch (v.getId()) {
                case R.id.register_over_btn:
                    //登记完成
//                    it = new Intent(getActivity(), RegisterOverActivity.class);
                    it.putExtra(IntentExtra.EXTRA_STATE, TypeConstant.STATE_REG_OVER);
                    break;
                case R.id.physical_exam_todo_btn:
                    //待体检
//                    it = new Intent(getActivity(), PhysicalExamTodoActivity.class);
                    it.putExtra(IntentExtra.EXTRA_STATE, TypeConstant.STATE_PHYSICAL_EXAM_TODO);
                    break;
                case R.id.physical_examing_btn:
                    //体检中
//                    it = new Intent(getActivity(), PhysicalExamingActivity.class);
                    it.putExtra(IntentExtra.EXTRA_STATE, TypeConstant.STATE_PHYSICAL_EXAMING);
                    break;
                case R.id.physical_exam_pass_btn:
                    //体检通过的
//                    it = new Intent(getActivity(), PhysicalExamPassActivity.class);
                    it.putExtra(IntentExtra.EXTRA_STATE, TypeConstant.STATE_PHYSICAL_EXAM_PASS);
                    break;
                case R.id.physical_exam_not_pass_btn:
                    //体检没有通过的
//                    it = new Intent(getActivity(), PhysicalExamNotPassActivity.class);
                    it.putExtra(IntentExtra.EXTRA_STATE, TypeConstant.STATE_PHYSICAL_EXAM_NOT_PASS);
                    break;
                case R.id.blood_plasma_todo_btn:
                    //待采浆
                    it.putExtra(IntentExtra.EXTRA_STATE, TypeConstant.STATE_BLOODPLASMA_COLLECTION_TODO);
                    break;

                case R.id.blood_plasma_collecting_btn:
                    //采浆中
                    it.putExtra(IntentExtra.EXTRA_STATE, TypeConstant.STATE_BLOODPLASMA_COLLECTING);
                    break;
                case R.id.blood_plasma_collect_over_btn:
                    //采浆完成
                    it.putExtra(IntentExtra.EXTRA_STATE, TypeConstant.STATE_BLOODPLASMA_COLLECTION_OVER);
                    break;
                default:
                    break;
            }

            if (it != null) {
                startActivity(it);
            }
        }
    }


}

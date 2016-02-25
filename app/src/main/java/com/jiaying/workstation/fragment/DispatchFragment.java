package com.jiaying.workstation.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.IdentityCardActivity;
import com.jiaying.workstation.activity.PhysicalExamTodoActivity;
import com.jiaying.workstation.activity.PhysicalExamingActivity;
import com.jiaying.workstation.activity.RegisterOverActivity;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;

/**
 * 调度
 */
public class DispatchFragment extends Fragment {

    private View register_over_btn;
    private View physical_examing_btn;
    private View physical_exam_todo_btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dispatch, container, false);
        register_over_btn = view.findViewById(R.id.register_over_btn);
        register_over_btn.setOnClickListener(new ClickListener());
        physical_examing_btn = view.findViewById(R.id.physical_examing_btn);
        physical_examing_btn.setOnClickListener(new ClickListener());
        physical_exam_todo_btn = view.findViewById(R.id.physical_exam_todo_btn);
        physical_exam_todo_btn.setOnClickListener(new ClickListener());
        return view;
    }

    //点击事件
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.e("TAG","click");
            Intent it = null;
            switch (v.getId()) {
                case R.id.register_over_btn:
                    //登记完成
                    it = new Intent(getActivity(), RegisterOverActivity.class);
                    break;
                case R.id.physical_exam_todo_btn:
                    //待体检
                    it = new Intent(getActivity(), PhysicalExamTodoActivity.class);
                    break;
                case R.id.physical_examing_btn:
                    //体检中
                    it = new Intent(getActivity(), PhysicalExamingActivity.class);
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

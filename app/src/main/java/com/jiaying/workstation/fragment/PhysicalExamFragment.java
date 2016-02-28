package com.jiaying.workstation.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.BloodPlasmaMachineForNurseActivity;
import com.jiaying.workstation.activity.DispatchStateListActivity;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;

/**
 * 调度
 */
public class PhysicalExamFragment extends Fragment {
    private Button physical_exam_btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_physical_exam, container, false);
        physical_exam_btn = (Button) view.findViewById(R.id.physical_exam_btn);
        physical_exam_btn.setOnClickListener(new ClickListener());
        return view;
    }


    //体检
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent it = new Intent(getActivity(), DispatchStateListActivity.class);
            it.putExtra(IntentExtra.EXTRA_STATE, TypeConstant.STATE_PHYSICAL_EXAM_TODO);
            startActivity(it);
        }
    }


}

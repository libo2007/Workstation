package com.jiaying.workstation.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.sensor.IdentityCardActivity;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;

/**
 * 等级
 */
public class RegisterFragment extends Fragment {
    private Button register_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        register_btn = (Button) view.findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new ClickListener());
        return view;
    }

    //登记 1.身份证 2.指纹 3.头像
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent it = new Intent(getActivity(), IdentityCardActivity.class);
            it.putExtra(IntentExtra.EXTRA_TYPE, TypeConstant.TYPE_REG);
            startActivity(it);
        }
    }


}

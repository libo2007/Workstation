package com.jiaying.workstation.com.jiaying.workstation.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.PulpMachineForNurseActivity;

/**
 * 采浆
 */
public class PlasmaCollectionFragment extends Fragment {
    private Button nurse_login_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plasma_collection, container, false);
        nurse_login_btn = (Button) view.findViewById(R.id.nurse_login_btn);
        nurse_login_btn.setOnClickListener(new goNuserLoginListener());
        return view;
    }

    //护士登录浆机
    private class goNuserLoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent it = new Intent(getActivity(), PulpMachineForNurseActivity.class);
            startActivity(it);
        }
    }
}

package com.jiaying.workstation.com.jiaying.workstation.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.FingerprintActivity;
import com.jiaying.workstation.activity.PulpMachineForNurseActivity;

/**
 * 采浆
 */
public class PlasmaCollectionFragment extends Fragment {
    private Button nurse_login_btn;
    //献浆按钮
    private Button pulp_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plasma_collection, null);
        nurse_login_btn = (Button) view.findViewById(R.id.nurse_login_btn);
        nurse_login_btn.setOnClickListener(new PulpListener());
        pulp_btn = (Button) view.findViewById(R.id.pulp_btn);
        pulp_btn.setOnClickListener(new PulpListener());
        return view;
    }


    //献浆
    private class PulpListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent it = new Intent(getActivity(), FingerprintActivity.class);
            startActivity(it);
        }
    }
}

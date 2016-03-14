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
 * 查询
 */
public class SearchFragment extends Fragment {
    private Button search_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        search_btn = (Button) view.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new ClickListener());
        return view;
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent it = new Intent(getActivity(), IdentityCardActivity.class);
            it.putExtra(IntentExtra.EXTRA_TYPE, TypeConstant.TYPE_SEARCH);
            startActivity(it);
        }
    }


}

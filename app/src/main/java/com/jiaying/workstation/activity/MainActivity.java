package com.jiaying.workstation.activity;

import android.app.FragmentManager;
import android.os.Bundle;

import com.jiaying.workstation.R;
import com.jiaying.workstation.com.jiaying.workstation.fragment.BloodPlasmaCollectionFragment;

/**
 * 主界面包括（建档，登记，体检，献浆四大部分）
 */
public class MainActivity extends BaseActivity {
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new BloodPlasmaCollectionFragment()).commit();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void initVariables() {

    }
}

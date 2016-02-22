package com.jiaying.workstation.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.jiaying.workstation.R;
import com.jiaying.workstation.fragment.BloodPlasmaCollectionFragment;
import com.jiaying.workstation.fragment.RegisterFragment;

/**
 * 主界面包括（建档，登记，体检， 采浆四大部分）
 */
public class MainActivity extends BaseActivity {
    private FragmentManager fragmentManager;

    private RadioGroup mGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, new BloodPlasmaCollectionFragment()).commit();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setTitle(R.string.app_name);
        }
        mGroup = (RadioGroup) findViewById(R.id.group);
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btn_1:

                        break;
                    case R.id.btn_2:
                        //登记
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, new RegisterFragment()).commit();
                        break;
                    case R.id.btn_3:

                        break;
                    case R.id.btn_4:
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, new BloodPlasmaCollectionFragment()).commit();
                        break;
                }
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public void initVariables() {

    }
}

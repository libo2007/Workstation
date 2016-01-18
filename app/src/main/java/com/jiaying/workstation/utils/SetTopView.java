package com.jiaying.workstation.utils;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.workstation.R;

/**
 * 作者：李波 on 2016/1/18 20:52
 * 邮箱：353510746@qq.com
 * 功能：标题栏
 */
public class SetTopView implements View.OnClickListener {
    private Activity mActivity;
    private TextView mTitle;
    private ImageView mBackView;

    public SetTopView(Activity activity, String title_text_string) {
        mActivity = activity;
        mTitle = (TextView) mActivity.findViewById(R.id.title_text);
        mBackView = (ImageView) mActivity.findViewById(R.id.back_img);
        mTitle.setText(title_text_string);
        mBackView.setOnClickListener(this);
    }

    public SetTopView(Activity activity, int title_text_id) {
        mActivity = activity;
        mTitle = (TextView) mActivity.findViewById(R.id.title_text);
        mBackView = (ImageView) mActivity.findViewById(R.id.back_img);
        mTitle.setText(activity.getResources().getString(title_text_id));
        mBackView.setOnClickListener(this);
    }

    /**
     * @param activity
     * @param title_text_id
     * @param backisvisible 返回键是否可见
     */
    public SetTopView(Activity activity, int title_text_id,
                      boolean backisvisible) {
        mActivity = activity;
        mTitle = (TextView) mActivity.findViewById(R.id.title_text);
        mBackView = (ImageView) mActivity.findViewById(R.id.back_img);
        mTitle.setText(activity.getResources().getString(title_text_id));
        mBackView.setOnClickListener(this);
        mBackView.setVisibility(backisvisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_img) {
            mActivity.finish();
        }

    }
}

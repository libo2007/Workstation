package com.jiaying.workstation.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.workstation.R;
import com.jiaying.workstation.activity.DispatchStateListActivity;
import com.jiaying.workstation.activity.sensor.FingerprintActivity;
import com.jiaying.workstation.constant.IntentExtra;
import com.jiaying.workstation.constant.TypeConstant;
import com.jiaying.workstation.entity.User;
import com.jiaying.workstation.utils.MyLog;

import java.util.List;

/**
 * 作者：lenovo on 2016/1/20 00:16
 * 邮箱：353510746@qq.com
 * 功能：调度状态列表adapter
 */
public class DispathStateListAdapter extends BaseAdapter {
    private static final String TAG = "DispathStateListAdapter";
    private List<User> mList;
    private Context mContext;

    private int mState;

    public DispathStateListAdapter(List<User> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public void setmState(int mState) {
        this.mState = mState;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dispatch_state_list_item, null);
            holder = new MyHolder();
            holder.head_image = (ImageView) convertView.findViewById(R.id.head_image);
            holder.name_txt = (TextView) convertView.findViewById(R.id.name_txt);
            holder.id_txt = (TextView) convertView.findViewById(R.id.id_txt);
            holder.green_left_txt = (TextView) convertView.findViewById(R.id.green_left_txt);
            holder.green_right_txt = (TextView) convertView.findViewById(R.id.green_right_txt);
            holder.call_btn = (Button) convertView.findViewById(R.id.call_btn);
            holder.state_btn = (Button) convertView.findViewById(R.id.state_btn);
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }
        holder.name_txt.setText(mList.get(position).getName());
        holder.id_txt.setText(mList.get(position).getId());


        holder.state_btn.setTag(position);
        switch (mState) {
            case TypeConstant.STATE_REG_OVER:
                //登记完成
                holder.state_btn.setText(mContext.getString(R.string.go_physical_todo));
                break;
            case TypeConstant.STATE_PHYSICAL_EXAM_TODO:
                //待体检
                holder.state_btn.setText(mContext.getString(R.string.go_physical));
                break;
            case TypeConstant.STATE_PHYSICAL_EXAMING:
                //体检中
                holder.state_btn.setVisibility(View.INVISIBLE);
                break;
            case TypeConstant.STATE_PHYSICAL_EXAM_PASS:
                //体检通过-》进入待采浆
                holder.state_btn.setText(mContext.getString(R.string.go_bloodplasma_todo));
                break;
            case TypeConstant.STATE_PHYSICAL_EXAM_NOT_PASS:
                //体检未通过
                holder.green_left_txt.setVisibility(View.INVISIBLE);
                holder.green_right_txt.setVisibility(View.INVISIBLE);
                holder.state_btn.setVisibility(View.INVISIBLE);
                break;
            case TypeConstant.STATE_BLOODPLASMA_COLLECTION_TODO:
                //待采浆-》采浆
                holder.green_left_txt.setVisibility(View.INVISIBLE);
                holder.green_right_txt.setVisibility(View.INVISIBLE);
                holder.state_btn.setText(mContext.getString(R.string.go_bloodplasma));
                break;
            case TypeConstant.STATE_BLOODPLASMA_COLLECTING:
                //采浆中
                holder.green_left_txt.setText("5号设备");
                holder.green_right_txt.setVisibility(View.INVISIBLE);
                holder.state_btn.setText(mContext.getString(R.string.change_mechine));
                break;
            case TypeConstant.STATE_BLOODPLASMA_COLLECTION_OVER:
                //采浆完成
                holder.green_left_txt.setVisibility(View.INVISIBLE);
                holder.green_right_txt.setVisibility(View.INVISIBLE);
                holder.state_btn.setVisibility(View.INVISIBLE);
                break;
        }
        holder.state_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int poz = (int) v.getTag();
                MyLog.e(TAG, "mState:" + mState);
                Intent it = null;
                switch (mState) {
                    case TypeConstant.STATE_REG_OVER:
                        //登记完成,进入待体检
                        it = new Intent(mContext, DispatchStateListActivity.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        it.putExtra(IntentExtra.EXTRA_STATE, TypeConstant.STATE_PHYSICAL_EXAM_TODO);
                        break;
                    case TypeConstant.STATE_PHYSICAL_EXAM_TODO:
                        //待体检，进入体检
                        it = new Intent(mContext, FingerprintActivity.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        it.putExtra(IntentExtra.EXTRA_TYPE, TypeConstant.TYPE_PHYSICAL_EXAM);
                        break;

                    case TypeConstant.STATE_PHYSICAL_EXAM_PASS:
                        //体检通过进入待采浆
                        it = new Intent(mContext, DispatchStateListActivity.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        it.putExtra(IntentExtra.EXTRA_STATE, TypeConstant.STATE_BLOODPLASMA_COLLECTION_TODO);
                        break;
                    case TypeConstant.STATE_BLOODPLASMA_COLLECTION_TODO:
                        //待采浆进入采浆中，首先需要指纹
                        it = new Intent(mContext, FingerprintActivity.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        it.putExtra(IntentExtra.EXTRA_TYPE, TypeConstant.TYPE_BLOODPLASMACOLLECTION);
                        it.putExtra(IntentExtra.EXTRA_STATE, TypeConstant.STATE_BLOODPLASMA_COLLECTING);
                        break;
                }
                if (it != null) {
                    mContext.startActivity(it);
                    ((Activity) mContext).finish();
                }


            }
        });
        return convertView;
    }

    private class MyHolder {
        ImageView head_image;
        TextView name_txt;
        TextView id_txt;
        Button state_btn;
        Button call_btn;
        TextView green_left_txt;
        TextView green_right_txt;
    }
}

package com.jiaying.workstation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiaying.workstation.R;
import com.jiaying.workstation.entity.PlasmaMachineEntity;

import java.util.List;

/**
 * 作者：lenovo on 2016/1/20 00:16
 * 邮箱：353510746@qq.com
 * 功能：采浆机分配
 */
public class PlasmaMachineSelectAdapter extends BaseAdapter {
    private List<PlasmaMachineEntity> mList;
    private Context mContext;

    public PlasmaMachineSelectAdapter(List<PlasmaMachineEntity> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.pulp_machine_select_item, null);
            holder = new MyHolder();
            holder.num_txt = (TextView) convertView.findViewById(R.id.num_txt);
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }
        holder.num_txt.setText(mList.get(position).getId());
        if(mList.get(position).isCheck()){
            holder.num_txt.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        }else{
            holder.num_txt.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        return convertView;
    }

    private class MyHolder {
        TextView num_txt;
    }
}

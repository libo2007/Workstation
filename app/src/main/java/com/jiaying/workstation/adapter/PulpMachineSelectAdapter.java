package com.jiaying.workstation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaying.workstation.R;
import com.jiaying.workstation.entity.Nurse;
import com.jiaying.workstation.entity.PulpMachine;

import java.util.List;

/**
 * 作者：lenovo on 2016/1/20 00:16
 * 邮箱：353510746@qq.com
 * 功能：采浆机分配
 */
public class PulpMachineSelectAdapter extends BaseAdapter {
    private List<PulpMachine> mList;
    private Context mContext;

    public PulpMachineSelectAdapter(List<PulpMachine> mList, Context mContext) {
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
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.pulp_machine_select_item,null);
            holder = new MyHolder();
            holder.select_image = (ImageView) convertView.findViewById(R.id.select_image);
            holder.id_txt = (TextView) convertView.findViewById(R.id.id_txt);
            convertView.setTag(holder);
        }else{
            holder = (MyHolder) convertView.getTag();
        }
        holder.id_txt.setText(mList.get(position).getNumber());
       if(mList.get(position).isCheck()){
           holder.select_image.setImageResource(R.mipmap.xuanzhong_ic);
       }else{
           holder.select_image.setImageResource(R.mipmap.xuanzhongno_ic);
       }
        holder.select_image.setTag(position);
        holder.select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int poz = (int) v.getTag();
                boolean checked = mList.get(poz).isCheck();
                mList.get(poz).setCheck(!checked);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    private class MyHolder{
        ImageView select_image;
        TextView id_txt;
    }
}

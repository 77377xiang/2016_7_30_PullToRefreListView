package com.xiang.pulltorefrelistviewproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ListAdapter extends BaseAdapter {
    private List<Persen> persens;
    private Context context;

    public ListAdapter(Context context, List<Persen> persens) {
        this.context = context;
        this.persens = persens;
    }

    @Override
    public int getCount() {
        return persens.size();
    }

    @Override
    public Object getItem(int position) {
        return persens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHouder holder=null;
        if (convertView==null){
            holder=new viewHouder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_list,null);
            holder.text1=(TextView)convertView.findViewById(R.id.text1);
            holder.text2=(TextView)convertView.findViewById(R.id.text2);
            convertView.setTag(holder);

        }else {
            holder = (viewHouder) convertView.getTag();
        }
        Persen persen=persens.get(position);
        holder.text1.setText(persen.getName());
        holder.text2.setText(persen.getAge());
        return convertView;

    }
    class viewHouder{
        TextView text1;
        TextView text2;
    }
}

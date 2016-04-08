package com.example.administrator.sjassistant.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.view.ChangeNumberDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 */
public class ContactAdapter extends BaseAdapter {

    private List<Person> datalist;
    private Context context;

    public ContactAdapter(Context context,List<Person> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @Override
    public int getCount() {
        return this.datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return this.datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_more,null);
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.number = (TextView)convertView.findViewById(R.id.phoneNumber);
            viewHolder.delete = (ImageView)convertView.findViewById(R.id.delete_contacts);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeNumberDialog d = new ChangeNumberDialog(context);
                        d.setFlag(1);
                        d.show();
                        d.setContentText("删除XXX此次的电话会议");
                        d.setOnDeleteClickListener(new ChangeNumberDialog.OnDeleteClickListener() {
                            @Override
                            public void onDelete(int i) {
                                if (i == 1) {
                                    Log.d("tag", "position" + position);
                                    removeView(position);
                                }
                            }
                        });

            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView number;
        ImageView delete;
    }

    public void removeView(int position) {
        this.datalist.remove(position);
        notifyDataSetChanged();
    }

    public void addView(Person person) {
        this.datalist.add(person);
        notifyDataSetChanged();
    }
}

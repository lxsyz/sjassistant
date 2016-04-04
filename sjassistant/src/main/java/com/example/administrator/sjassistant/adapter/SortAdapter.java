package com.example.administrator.sjassistant.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.SortModel;

import java.util.List;

/**
 * Created by Administrator on 2016/4/4.
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer{

    private List<SortModel> list = null;
    private Context mContext;

    public SortAdapter(Context context,List<SortModel> list) {
        this.mContext = context;
        this.list = list;
    }

    public void updateListView(List<SortModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        SortModel sortModel = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item,null);
            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.name);
            viewHolder.tvLetter = (TextView)convertView.findViewById(R.id.catalog);
            viewHolder.group = (TextView)convertView.findViewById(R.id.group);
            viewHolder.phone = (ImageView)convertView.findViewById(R.id.phone);
            //viewHolder.line_view = convertView.findViewById(R.id.lineview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        int section = getSectionForPosition(position);

        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(sortModel.getSortLetter());

        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);

        }

        final int pos = position;

        viewHolder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("tag", "clicked on listview item icon_01 position = " + getItemId(pos));
            }
        });
        viewHolder.tvTitle.setText(this.list.get(position).getName());

        return convertView;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i =0;i <getCount();i++) {
            String str = list.get(i).getSortLetter();
            char first = str.toUpperCase().charAt(0);
            if (first == sectionIndex) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetter().charAt(0);
    }

    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        TextView group;
        ImageView phone;
        View line_view;
    }
}

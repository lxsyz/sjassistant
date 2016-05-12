package com.example.administrator.sjassistant.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.SortModel;

import java.util.List;

/**
 * Created by Administrator on 2016/4/7.
 */
public class AddContactAdapter extends BaseAdapter implements SectionIndexer {

    private List<SortModel> list = null;
    private Context mContext;

    public AddContactAdapter(Context context,List<SortModel> list) {
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
        Log.d("position",position+" ");
        SortModel sortModel = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_add,null);
            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.name);
            viewHolder.tvLetter = (TextView)convertView.findViewById(R.id.catalog);
            viewHolder.group = (TextView)convertView.findViewById(R.id.group);
            viewHolder.add = (ImageView)convertView.findViewById(R.id.add);
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

        if (sortModel.getChecked() == 0) {
            viewHolder.add.setImageResource(R.drawable.radio_unchecked);
        } else {
            viewHolder.add.setImageResource(R.drawable.radio_checked);
        }

        viewHolder.tvTitle.setText(this.list.get(position).getName());
        viewHolder.group.setText(this.list.get(position).getGroup());
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
        ImageView add;
        View line_view;
    }
}

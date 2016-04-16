package com.example.administrator.sjassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.Bill;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/14.
 */
public class DetailExpandableAdapter extends BaseExpandableListAdapter {


    private Context mContext;
    private ArrayList<Object> mList;
    private LayoutInflater mInflater;

    class GroupViewHolder {
        TextView tv_content;
        ImageView iv_indicator;
        View group_vi;
    }

    class ChildViewHolder {
        TextView tv_name;
        View child_vi;
    }

    public DetailExpandableAdapter(Context mContext,ArrayList<Object> list) {
        this.mContext = mContext;
        this.mList = list;

        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getGroupCount() {

        return this.mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 3;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
//        Object group = mList.get(groupPosition);
//        Object child = null;
//
//        if (group instanceof Bill) {
//            Bill b = (Bill)group;
//
//        }
        return "分组项目信息";
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new GroupViewHolder();
            convertView = mInflater.inflate(R.layout.group_item,null);

            viewHolder.tv_content = (TextView)convertView.findViewById(R.id.group_name);
            viewHolder.iv_indicator = (ImageView)convertView.findViewById(R.id.group_indicator);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder)convertView.getTag();
        }


        if (isExpanded) {
            viewHolder.iv_indicator.setImageResource(R.drawable.up_arrow);
        } else {
            viewHolder.iv_indicator.setImageResource(R.drawable.down_arrow);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = mInflater.inflate(R.layout.child_item,null);

            childViewHolder.tv_name = (TextView)convertView.findViewById(R.id.child_tv);
            childViewHolder.child_vi = convertView.findViewById(R.id.child_vi);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        childViewHolder.tv_name.setText("分组信息");

        if (isLastChild) {
            childViewHolder.child_vi.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.child_vi.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

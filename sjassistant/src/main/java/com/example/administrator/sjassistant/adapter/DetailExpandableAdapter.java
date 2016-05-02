package com.example.administrator.sjassistant.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/14.
 */
public class DetailExpandableAdapter extends BaseExpandableListAdapter {


    private Context mContext;
    //private ArrayList<Object> mList;
    List<Map<String,String>> mList = new ArrayList<Map<String, String>>();
    private LayoutInflater mInflater;

    class GroupViewHolder {
        TextView tv_content;
        ImageView iv_indicator;
        View group_vi;
    }

    class ChildViewHolder {
        TextView tv_name;
        View child_vi;
        View line;
    }

    public DetailExpandableAdapter(Context mContext,List<Map<String,String>> list) {
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
        Log.d("response",mList.get(groupPosition).size() + " ");
        return this.mList.get(groupPosition).size() - 2;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition).get("title") + ":  "
                + mList.get(groupPosition).get("value");
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
        Map<String,String> map = new HashMap<>();
        map = mList.get(groupPosition);


        return map.get("data"+(childPosition + 1)) + ":  "
                + map.get("value"+(childPosition + 1));
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

        viewHolder.tv_content.setText(getGroup(groupPosition)+" ");
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
            childViewHolder.line = convertView.findViewById(R.id.line);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        childViewHolder.tv_name.setText(getChild(groupPosition,childPosition)+" ");

        if (isLastChild) {
            childViewHolder.child_vi.setVisibility(View.VISIBLE);
            childViewHolder.line.setVisibility(View.GONE);
        } else {
            childViewHolder.line.setVisibility(View.VISIBLE);
            childViewHolder.child_vi.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

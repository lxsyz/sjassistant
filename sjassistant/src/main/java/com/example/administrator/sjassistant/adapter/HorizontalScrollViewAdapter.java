package com.example.administrator.sjassistant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.administrator.sjassistant.R;

import java.util.List;

public class HorizontalScrollViewAdapter
{

	private Context mContext;
	private LayoutInflater mInflater;

	private List<String> mDatas;

	public HorizontalScrollViewAdapter(Context context)
	{
		this.mContext = context;
		mInflater = LayoutInflater.from(context);

	}

	public int getCount()
	{
		return mDatas.size();
	}

	public Object getItem(int position)
	{
		return mDatas.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		String s = mDatas.get(position);
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.item_scroll, parent, false);
			viewHolder.text = (TextView)convertView.findViewById(R.id.item_data);
			convertView.setTag(viewHolder);
		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.text.setText(s);
		return convertView;
	}


	
	private class ViewHolder
	{
		TextView text;
	}

}

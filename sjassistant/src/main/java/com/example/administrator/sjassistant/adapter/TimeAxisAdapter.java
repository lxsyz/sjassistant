package com.example.administrator.sjassistant.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;

public class TimeAxisAdapter extends BaseAdapter {

	private List<HashMap<String, Object>> list;

	private Context context;

	private static class ViewHolder {
		private TextView tvContent;
		private ImageView guiji;
		private TextView text;
		private View top_line;
		private ImageView icon_image;
	}

	public TimeAxisAdapter(Context context, List<HashMap<String, Object>> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
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

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.time_item, null);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.name);
			viewHolder.guiji = (ImageView)convertView.findViewById(R.id.guiji);
			viewHolder.text = (TextView)convertView.findViewById(R.id.text);
			viewHolder.top_line = convertView.findViewById(R.id.top_line);
			viewHolder.icon_image = (ImageView)convertView.findViewById(R.id.img_icon);


			viewHolder.tvContent.setText(list.get(position).get("content")
					.toString());
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();

		}

		if (position != 0) {
			//viewHolder.short_line.setVisibility(View.GONE);
			//viewHolder.top_line.setVisibility(View.VISIBLE);
			viewHolder.guiji.setVisibility(View.GONE);
			viewHolder.text.setVisibility(View.GONE);

		} else {
			//viewHolder.short_line.setVisibility(View.VISIBLE);
			//viewHolder.top_line.setVisibility(View.GONE);
			viewHolder.text.setVisibility(View.VISIBLE);
			viewHolder.guiji.setVisibility(View.VISIBLE);
		}

		return convertView;
	}
}

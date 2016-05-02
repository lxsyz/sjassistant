package com.example.administrator.sjassistant.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.activity.MainActivity;
import com.example.administrator.sjassistant.bean.BillDetailList;
import com.example.administrator.sjassistant.bean.ListLog;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import okhttp3.Call;
import okhttp3.Request;

public class TimeAxisAdapter extends BaseAdapter {

	private List<ListLog> list;

	private Context context;

	private static class ViewHolder {
		private TextView name;
		private ImageView guiji;
		private TextView text;
		private View top_line;
		private ImageView icon_image;
		private ImageView iv_person;
		private TextView time;
		private TextView apartment;

        private TextView dealOpinion;
        private LinearLayout dealOpinion_layout;
	}

	public TimeAxisAdapter(Context context, List<ListLog> list) {
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
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.name);
			viewHolder.guiji = (ImageView)convertView.findViewById(R.id.guiji);
			viewHolder.text = (TextView)convertView.findViewById(R.id.text);
			viewHolder.top_line = convertView.findViewById(R.id.top_line);
			viewHolder.icon_image = (ImageView)convertView.findViewById(R.id.img_icon);
			viewHolder.time = (TextView)convertView.findViewById(R.id.dealTime);
			viewHolder.iv_person = (ImageView)convertView.findViewById(R.id.iv_person);
			viewHolder.apartment = (TextView)convertView.findViewById(R.id.dealResult);
            viewHolder.dealOpinion = (TextView)convertView.findViewById(R.id.dealOpinion);
            viewHolder.dealOpinion_layout = (LinearLayout)convertView.findViewById(R.id.deal_opinion_layout);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();

		}
		viewHolder.name.setText(list.get(position).getDealPerson());
		viewHolder.time.setText(list.get(position).getDealTime());
		viewHolder.apartment.setText(list.get(position).getDealResult());

        String dealResult = list.get(position).getDealResult();

        if (dealResult.equals("提交审批")) {
            viewHolder.apartment.setTextColor(context.getResources().getColor(R.color.postInspect));
        } else if (dealResult.equals("已审批")){
            viewHolder.apartment.setTextColor(Color.parseColor("#ECA6A7"));
        } else if (dealResult.equals("审批并提交")){
            viewHolder.apartment.setTextColor(Color.parseColor("#E8BB5B"));
        } else if (dealResult.equals("等待我的审批")){
			viewHolder.apartment.setTextColor(context.getResources().getColor(R.color.waitInspect));
		} else {
			viewHolder.apartment.setTextColor(context.getResources().getColor(R.color.postInspect));
		}

		if (dealResult.equals("等待我的审批")) {
			viewHolder.icon_image.setImageResource(R.drawable.unfinished);
		} else {
			viewHolder.icon_image.setImageResource(R.drawable.inspect_finish);
		}

        String userImg = list.get(position).getUserImg();
        if (TextUtils.isEmpty(userImg)) {
            viewHolder.iv_person.setImageResource(R.drawable.customer_de);
        } else {
            final ViewHolder finalViewHolder = viewHolder;
            OkHttpUtils
                    .get()
                    .url(userImg)
                    .build()
                    .execute(new BitmapCallback() {
                        @Override
                        public void onError(Call Call, Exception e) {
                            Log.d("error",e.getMessage());
                            ErrorUtil.NetWorkToast(context);
                        }

                        @Override
                        public void onResponse(Bitmap bitmap) {
                            finalViewHolder.iv_person.setImageBitmap(bitmap);
                        }
                    });
        }

        String dealOpinion = list.get(position).getDealOpinion();

        if (TextUtils.isEmpty(dealOpinion)) {
            viewHolder.dealOpinion_layout.setVisibility(View.GONE);
        } else {
            dealOpinion = "审批理由: "+dealOpinion;
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(dealOpinion);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#67C5AA")),6,dealOpinion.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.dealOpinion.setText(spannableStringBuilder);
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

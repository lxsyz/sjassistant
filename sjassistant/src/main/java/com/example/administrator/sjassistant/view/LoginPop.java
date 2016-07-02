package com.example.administrator.sjassistant.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.database.Dao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/19.
 */
public class LoginPop extends PopupWindow {

    private View main;
    private ListView listView;

    private List<String> datalist = new ArrayList<>();

    private Context context;

    public LoginPop(Context context, AdapterView.OnItemClickListener onItemClickListener,int paramInt1, int paramInt2) {
        super(context);
        this.context = context;
        main = LayoutInflater.from(context).inflate(R.layout.login_pop,null);
        listView = (ListView)main.findViewById(R.id.list);

        if (onItemClickListener != null) {
            listView.setOnItemClickListener(onItemClickListener);
        }

        setContentView(main);

        setFocusable(true);
        setOutsideTouchable(true);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.AnimContacts);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));
    }

    public void setData(final List<String> data) {
        this.datalist = data;

        listView.setAdapter(new CommonAdapter<String>(context,this.datalist,R.layout.item_login_pop) {
            @Override
            public void convert(ViewHolder holder, final String s) {
                holder.setText(R.id.text,s);
                holder.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dao.getInstance(context).delete(s);
                        datalist.remove(s);
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }


}

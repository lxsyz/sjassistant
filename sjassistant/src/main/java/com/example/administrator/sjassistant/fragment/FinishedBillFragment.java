package com.example.administrator.sjassistant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Bill;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 */
public class FinishedBillFragment extends Fragment {

    private ListView list;


    private List<Bill> datalist = new ArrayList<Bill>();

    public FinishedBillFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_finished_bill,null);
        list = (ListView)v.findViewById(R.id.finished_list);

        datalist = (ArrayList<Bill>)getArguments().getSerializable("data");

        if (datalist == null || datalist.size() == 0) {
            list.setVisibility(View.GONE);
            TextView textView = (TextView)v.findViewById(R.id.prompt);
            textView.setVisibility(View.VISIBLE);
        }
        else if (datalist != null) {
            list.setAdapter(new CommonAdapter<Bill>(getActivity(), datalist, R.layout.item_finished_bill) {
                @Override
                public void convert(ViewHolder holder, Bill bill) {
                    String title = bill.getUserCode() + "的单据需要你审批";
                    holder.setText(R.id.id_title, title);
                    holder.setText(R.id.id_type_value, bill.getBillType());
                    holder.setText(R.id.id_time_value, bill.getDealTime());
                    holder.setText(R.id.read_flag, bill.getDealResult());

                    if (TextUtils.isEmpty(bill.getDealResult()) || bill.getDealResult().equals("null")) {
                        holder.setText(R.id.read_flag, "未读");
                        ((TextView) holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.unread));
                    } else if (bill.getDealResult().equals("未读") || bill.getDealResult().equals("退回未提交")) {
                        ((TextView) holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.unread));
                    } else if (bill.getDealResult().equals("通过未提交") || bill.getDealResult().equals("已读")) {
                        ((TextView) holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.read));
                    }
                }
            });
        }



        return v;

    }
}

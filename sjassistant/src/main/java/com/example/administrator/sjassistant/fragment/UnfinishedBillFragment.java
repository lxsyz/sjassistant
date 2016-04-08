package com.example.administrator.sjassistant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 */
public class UnfinishedBillFragment extends Fragment {

    private ListView list;

    private List<String> datalist = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_unfinished_bill,null);

        list = (ListView) v.findViewById(R.id.unfinished_list);
        datalist.add("sd");datalist.add("sd");
        list.setAdapter(new CommonAdapter<String>(getActivity(),datalist,R.layout.item_finished_bill) {
            @Override
            public void convert(ViewHolder holder, String s) {
                holder.getView(R.id.iv_inspect).setVisibility(View.GONE);
            }
        });
        return v;
    }
}

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
import java.util.StringTokenizer;

/**
 * Created by Administrator on 2016/4/6.
 */
public class FinishedBillFragment extends Fragment {

    private ListView list;

    private List<String> datalist = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_finished_bill,null);
        list = (ListView)v.findViewById(R.id.finished_list);

        datalist.add("ad");datalist.add("ads");

        list.setAdapter(new CommonAdapter<String>(getActivity(),datalist,R.layout.item_finished_bill) {
            @Override
            public void convert(ViewHolder holder, String s) {

            }
        });

        return v;

    }
}

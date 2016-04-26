package com.example.administrator.sjassistant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.InspectPerson;
import com.example.administrator.sjassistant.bean.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class InspectReasonActivity extends BaseActivity {

    private List<InspectPerson> datalist = new ArrayList<InspectPerson>();

    private ListView list;
    private EditText inspectReason;

    private CommonAdapter<InspectPerson> commonAdapter;

    private List<String> resultList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_inspect_reason);

        setTopText("审批理由");

        inspectReason = (EditText)findViewById(R.id.inspect_reason);
        list = (ListView)findViewById(R.id.inspect_list);



    }

    @Override
    protected void onResume() {
        super.onResume();

        commonAdapter = new CommonAdapter<InspectPerson>(InspectReasonActivity.this,datalist,R.layout.item_inspect_reason) {
            @Override
            public void convert(ViewHolder holder, InspectPerson inspectPerson) {
                holder.setText(R.id.user_name,inspectPerson.getName());

                if (inspectPerson.isCheckState()) {
                    holder.setImageResource(R.id.iv_checked,R.drawable.radio_checked);
                } else {
                    holder.setImageResource(R.id.iv_checked,R.drawable.radio_unchecked);
                }
            }
        };


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView iv = (ImageView)view.findViewById(R.id.iv_checked);
                InspectPerson inspectPerson = datalist.get(position);

                if (inspectPerson.isCheckState()) {
                    inspectPerson.setCheckState(false);
                    iv.setImageResource(R.drawable.radio_unchecked);
                    commonAdapter.notifyDataSetChanged();
                } else {
                    inspectPerson.setCheckState(true);
                    iv.setImageResource(R.drawable.radio_checked);
                    commonAdapter.notifyDataSetChanged();
                }

            }
        });
    }
}

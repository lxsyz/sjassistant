package com.example.administrator.sjassistant.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class InspectReasonActivity extends BaseActivity {

    private List<Person> inspectPerson = new ArrayList<Person>();

    private ListView list;
    private EditText inspectReason;

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
}

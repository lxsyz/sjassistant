package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.ContactAdapter;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.ChangeNumberDialog;
import com.example.administrator.sjassistant.view.MyDialog;
import com.example.administrator.sjassistant.view.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 */
public class MoreContact extends BaseActivity implements View.OnClickListener {

    private ListView listView;

    private List<Person> datalist = new ArrayList<Person>();


    private RelativeLayout change_layout;

    private ImageView iv_start,add;
    private EditText number,content;


    private TextView dialog_ok,dialog_cancel,master;

    ContactAdapter adapter;
    int result = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();

        setTopText("多方通话");
        setCenterView(R.layout.activity_more_contact);

        listView = (ListView)findViewById(R.id.person);

        iv_start = (ImageView)findViewById(R.id.iv_start);
        number = (EditText)findViewById(R.id.number);
        add = (ImageView)findViewById(R.id.add);
        change_layout = (RelativeLayout)findViewById(R.id.change_layout);
        master = (TextView)findViewById(R.id.master);

        Person p = new Person();
        p.setName("张  三");
        p.setPhoneNumber("13003152436");
        datalist.add(p);
        p.setName("李 四");
        p.setPhoneNumber("13003152436");
        datalist.add(p);

        adapter = new ContactAdapter(this,datalist);

        listView.setAdapter(adapter);
        OperatorUtil.setListViewHeight(listView);
        change_layout.setOnClickListener(this);
        iv_start.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        ChangeNumberDialog dialog2 = new ChangeNumberDialog(this);
        switch (v.getId()) {
            case R.id.iv_start:
                intent = new Intent(MoreContact.this,StartChattingActivity.class);
                startActivity(intent);
                break;
            case R.id.add:
                if (!TextUtils.isEmpty(number.getText().toString())) {

                } else {
                    intent = new Intent(MoreContact.this,AddChatContact.class);
                    intent.putExtra("count",adapter.getCount());
                    startActivity(intent);
                }

                if (adapter.getCount() >= 7) {

                }

                break;
            case R.id.change_layout:
                dialog2.show();
                dialog2.setFlag(0);
                dialog2.setOnItemClickListener(new ChangeNumberDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(String str) {
                        if (OperatorUtil.isPhoneNumber(str))
                            master.setText(str);
                        else {
                            ToastUtil.showShort(MoreContact.this,"手机号码格式不正确");
                            master.setText("");
                        }
                    }
                });

                break;

        }
    }
}

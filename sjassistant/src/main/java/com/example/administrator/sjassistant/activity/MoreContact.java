package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.ContactAdapter;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.view.ChangeNumberDialog;
import com.example.administrator.sjassistant.view.MyDialog;
import com.example.administrator.sjassistant.view.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 */
public class MoreContact extends BaseActivity implements View.OnClickListener {

    private MyListView listView;

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

        listView = (MyListView)findViewById(R.id.person);

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

//        listView.setAdapter(new CommonAdapter<Person>(this, datalist, R.layout.item_more) {
//            @Override
//            public void convert(final ViewHolder holder, Person person) {
//                holder.getView(R.id.delete_contacts).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ChangeNumberDialog d = new ChangeNumberDialog(MoreContact.this);
//
//                        d.setFlag(1);
//                        d.show();
//                        d.setOnDeleteClickListener(new ChangeNumberDialog.OnDeleteClickListener() {
//                            @Override
//                            public void onDelete(int i) {
//                                result = i;
//                                Log.d("tag", "result: " + result);
//                                if (result == 1) {
//                                    Log.d("tag", "result: " + result+"position"+holder.getPosition());
//                                    removeItem(holder.getPosition());
//                                }
//                            }
//                        });
//
//                    }
//                });
//            }
//        });

        listView.setAdapter(adapter);

        change_layout.setOnClickListener(this);
        iv_start.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        MyDialog dialog = new MyDialog(this);
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
                        master.setText(str);
                    }
                });

                break;

        }
    }
}

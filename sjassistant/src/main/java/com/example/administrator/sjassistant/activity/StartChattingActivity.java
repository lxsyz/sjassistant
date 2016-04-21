package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.RecyclerAdapter;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.view.ChangeNumberDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 */
public class StartChattingActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<Person> datalist = new ArrayList<Person>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0;i < 7;i++) {
            Person p = new Person();
            p.setName("李 四");
            p.setPhoneNumber("13003152436");
            datalist.add(0, p);
        }

        adapter = new RecyclerAdapter(datalist);

        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new RecyclerAdapter.OnItemClickListener() {

            @Override
            public void onClick(View v, final Object object) {
                if (object.equals("add")) {
                    Intent intent = new Intent(StartChattingActivity.this,AddChatContact.class);
                    intent.putExtra("count",adapter.getItemCount());
                    startActivity(intent);
                } else {
                    ChangeNumberDialog d = new ChangeNumberDialog(StartChattingActivity.this);
                    d.setFlag(1);
                    d.show();
                    d.setContentText("删除XXX此次的电话会议");
                    d.setOnDeleteClickListener(new ChangeNumberDialog.OnDeleteClickListener() {
                        @Override
                        public void onDelete(int i) {
                            if (i == 1) {
                                adapter.removePerson((Person)object);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();

        setTopText("通话时长");
        setCenterView(R.layout.activity_start_chatting);

        recyclerView = (RecyclerView)findViewById(R.id.id_recyclerview);

        GridLayoutManager manager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(manager);




        Person add = new Person();
        datalist.add(add);



    }
}

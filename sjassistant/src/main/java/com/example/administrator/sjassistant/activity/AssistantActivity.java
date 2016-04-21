package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Assitant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/3.
 */
public class AssistantActivity extends BaseActivity implements View.OnClickListener {
    private ImageView search,delete;
    private EditText ed_name;

    private ListView lv;
    private RelativeLayout watch_all_layout;
    private List<Assitant> datalist = new ArrayList<Assitant>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_assistant);
        setTopText("小助理");

        search = (ImageView)findViewById(R.id.search);
        ed_name = (EditText)findViewById(R.id.search_content);
        delete = (ImageView)findViewById(R.id.delete_word);

        ed_name.setHint("搜索");
        search.setOnClickListener(this);
        ed_name.setOnClickListener(this);
        delete.setOnClickListener(this);

        lv = (ListView)findViewById(R.id.lv);

        Assitant assitant = new Assitant();
        assitant.setPostman("张三");
        assitant.setPostTime("3月5日 下午12:49");
        assitant.setTitle("关于开展什么什么什么的通知");
        assitant.setType("员工天地");
        datalist.add(assitant);

        lv.setAdapter(new CommonAdapter<Assitant>(this, datalist, R.layout.item_assistant) {
            @Override
            public void convert(ViewHolder holder, Assitant assitant) {
                holder.setText(R.id.assistant_title, assitant.getTitle());
                holder.setText(R.id.assistant_time, assitant.getPostTime());
                holder.setText(R.id.assistant_subtime, assitant.getPostTime());

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AssistantActivity.this,AssistantDetail.class);
                Assitant a = (Assitant)lv.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("assistant",a);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.search:
                if (!TextUtils.isEmpty(ed_name.getText().toString())) {
                    //intent = new Intent(getActivity(), SearchResultActivity.class);
                    //intent.putExtra("name",ed_name.getText().toString());
                    //startActivity(intent);
                }
                break;
            case R.id.delete_word:
                ed_name.setText("");
                break;

        }
    }
}

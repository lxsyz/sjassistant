package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.MessageInform;
import com.example.administrator.sjassistant.swipemenulistview.SwipeMenu;
import com.example.administrator.sjassistant.swipemenulistview.SwipeMenuCreator;
import com.example.administrator.sjassistant.swipemenulistview.SwipeMenuItem;
import com.example.administrator.sjassistant.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MessageActivity extends BaseActivity implements View.OnClickListener {

    private SwipeMenuListView message_list;

    private ImageView search,delete;
    private EditText ed_name;

    private LinearLayout message_layout;

    private List<MessageInform> datalist = new ArrayList<MessageInform>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        message_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MessageActivity.this,MessageDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("detail",datalist.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_message);
        setTopText("消息、通知");

        search = (ImageView)findViewById(R.id.search);
        ed_name = (EditText)findViewById(R.id.search_content);
        delete = (ImageView)findViewById(R.id.delete_word);

        ed_name.setHint("搜索消息通知");
        search.setOnClickListener(this);
        ed_name.setOnClickListener(this);
        delete.setOnClickListener(this);



        message_list = (SwipeMenuListView)findViewById(R.id.message_list);

        MessageInform message1 = new MessageInform();
        message1.setTime("2016-04-02");
        message1.setTitle("关于开展平均工作的通知");
        message1.setUsername("张三");
        datalist.add(message1);

        MessageInform message2 = new MessageInform();
        message2.setTime("2016-04-02");
        message2.setTitle("关于开展平均工作的通知");
        message2.setUsername("李四");
        datalist.add(message2);

        message_list.setAdapter(new CommonAdapter<MessageInform>(this, datalist, R.layout.item_message) {
            @Override
            public void convert(ViewHolder holder, MessageInform message) {
                holder.setText(R.id.username,message.getUsername());
                holder.setText(R.id.message_title, message.getTitle());
                holder.setText(R.id.message_time, message.getTime());
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());

                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);

                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        message_list.setMenuCreator(creator);

        message_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        MessageInform item = datalist.get(position);
                        // 调用服务器的方法delete
                        //delete(item.getCarPlateNo());
                        break;
                }
            }
        });




    }

    @Override
    public void onClick(View v) {
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

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}

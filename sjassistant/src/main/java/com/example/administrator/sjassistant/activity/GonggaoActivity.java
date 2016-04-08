package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.GongGao;
import com.example.administrator.sjassistant.swipemenulistview.SwipeMenu;
import com.example.administrator.sjassistant.swipemenulistview.SwipeMenuCreator;
import com.example.administrator.sjassistant.swipemenulistview.SwipeMenuItem;
import com.example.administrator.sjassistant.swipemenulistview.SwipeMenuListView;
import com.example.administrator.sjassistant.util.OperatorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/2.
 */
public class GonggaoActivity extends BaseActivity implements View.OnClickListener {

    private SwipeMenuListView gonggao_list;

    private ImageView search,delete;
    private EditText ed_name;

    private List<GongGao> datalist = new ArrayList<GongGao>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_gonggao);
        setTopText("公告");

        search = (ImageView)findViewById(R.id.search);
        ed_name = (EditText)findViewById(R.id.search_content);
        delete = (ImageView)findViewById(R.id.delete_word);

        ed_name.setHint("搜索公告");
        search.setOnClickListener(this);
        ed_name.setOnClickListener(this);
        delete.setOnClickListener(this);

        gonggao_list = (SwipeMenuListView)findViewById(R.id.gonggao_list);

        GongGao gongGao1 = new GongGao();
        gongGao1.setTime("2016-04-02");
        gongGao1.setTitle("关于开展平均工作的通知哈哈哈哈哈哈哈哈hahaahahahahahaha");
        datalist.add(gongGao1);

        GongGao gongGao2 = new GongGao();
        gongGao2.setTime("2016-04-02");
        gongGao2.setTitle("关于开展平均工作的通知哈哈哈哈哈哈哈哈hahaahahahahahaha");
        datalist.add(gongGao2);

        gonggao_list.setAdapter(new CommonAdapter<GongGao>(this, datalist, R.layout.item_gonggao) {
            @Override
            public void convert(ViewHolder holder, GongGao gongGao) {
                holder.setText(R.id.gonggao_title, gongGao.getTitle());
                holder.setText(R.id.gonggao_time, gongGao.getTime());
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
                deleteItem.setIcon(R.drawable.ic_delete2);
                deleteItem.setIcon(deleteItem.zoomDrawable(100,100));
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        gonggao_list.setMenuCreator(creator);

        gonggao_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {

                if (index == 0) {
                    GongGao item = datalist.get(position);
                    // 调用服务器的方法delete
                    //delete(item.getCarPlateNo());
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

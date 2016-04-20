package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

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

//        MessageInform message1 = new MessageInform();
//        message1.setTime("2016-04-02");
//        message1.setTitle("关于开展平均工作的通知");
//        message1.setUsername("张三");
//        datalist.add(message1);
//
//        MessageInform message2 = new MessageInform();
//        message2.setTime("2016-04-02");
//        message2.setTitle("关于开展平均工作的通知");
//        message2.setUsername("李四");
//        datalist.add(message2);






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

    @Override
    protected void onResume() {
        super.onResume();

        getMessage();



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

    /*
     * 获取消息
     */
    private void getMessage() {
        datalist.clear();

        String url = Constant.SERVER_URL + "message/showMessage";

        OkHttpUtils.post()
                .url(url)
                .addParams("userCode",Constant.username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error",e.getMessage()+" ");
                        ErrorUtil.NetWorkToast(MessageActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response+" ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            if (statusCode == 0) {
                                JSONObject data = object.getJSONObject("data");
                                JSONArray list = data.getJSONArray("list");
                                int len = list.length();
                                for (int i = 0; i < len; i++) {
                                    JSONObject o = list.getJSONObject(i);
                                    MessageInform messageInform = new MessageInform();
                                    messageInform.setId(o.getInt("id"));
                                    messageInform.setMessagePublishtime(o.getString("MessagePublishtime"));
                                    messageInform.setMessagePublisher(o.getString("MessagePublisher"));
                                    messageInform.setMessageTitle(o.getString("messageTitle"));
                                    messageInform.setHeadImg(o.getString("headImg"));
                                    datalist.add(messageInform);
                                }

                                message_list.setAdapter(new CommonAdapter<MessageInform>(MessageActivity.this, datalist, R.layout.item_message) {
                                    @Override
                                    public void convert(final ViewHolder holder, MessageInform message) {

                                        holder.setText(R.id.username,message.getMessagePublisher());
                                        holder.setText(R.id.message_title, message.getMessageTitle());
                                        holder.setText(R.id.message_time, message.getMessagePublishtime());
                                        OkHttpUtils.get()
                                                .url(message.getHeadImg())
                                                .build().execute(new BitmapCallback() {
                                            @Override
                                            public void onError(Call call, Exception e) {
                                                ErrorUtil.NetWorkToast(MessageActivity.this);
                                            }

                                            @Override
                                            public void onResponse(Bitmap response) {
                                                holder.setImageBitmap(R.id.user_photo,response);
                                            }
                                        });

                                    }
                                });

                            } else {
                                ToastUtil.show(MessageActivity.this,"服务器异常");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}

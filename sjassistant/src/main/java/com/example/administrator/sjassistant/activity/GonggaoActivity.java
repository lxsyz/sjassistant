package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.MyPromptDialog;
import com.zhy.http.okhttp.OkHttpUtils;
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
public class GonggaoActivity extends BaseActivity implements View.OnClickListener {

    private SwipeMenuListView GongGao_list;

    private ImageView search,delete;
    private EditText ed_name;

    private List<GongGao> datalist = new ArrayList<GongGao>();

    private CommonAdapter<GongGao> commonAdapter;

    private MyPromptDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (TextUtils.isEmpty(Constant.username)) {
//            SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
//            Constant.username = sp.getString("username", null);
//        }
        Log.d("activity", "gonggao  oncreate");

        GongGao_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GonggaoActivity.this, GonggaoDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("detail", datalist.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        pd = new MyPromptDialog(this);
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

        GongGao_list = (SwipeMenuListView)findViewById(R.id.gonggao_list);


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

        showNotes();



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

        GongGao_list.setMenuCreator(creator);

        GongGao_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {

                if (index == 0) {
                    GongGao item = datalist.get(position);
                    // 调用服务器的方法delete
                    delete(item.getId(), position);
                }
            }
        });

        ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterGongGao(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /*
     * 显示公告
     */
    private void showNotes() {
        datalist.clear();

        String url = Constant.SERVER_URL + "notes/showNotes";


        OkHttpUtils.post()
                .url(url)
                .addParams("userCode",Constant.username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(GonggaoActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response + " ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");

                            if (statusCode == 0) {
                                JSONObject data = object.getJSONObject("data");
                                JSONArray list = data.getJSONArray("list");
                                int len = list.length();
                                for (int i = 0; i < len; i++) {
                                    JSONObject o = list.getJSONObject(i);
                                    GongGao GongGao = new GongGao();
                                    GongGao.setId(o.getInt("id"));
                                    GongGao.setNoteTitle(o.getString("noteTitle"));
                                    GongGao.setNoteDetail(o.getString("noteDetail"));
                                    GongGao.setNotePublisher(o.getString("notePublisher"));
                                    GongGao.setNotePublishtime(o.getString("notePublishtime"));
                                    datalist.add(GongGao);
                                }

                                commonAdapter = new CommonAdapter<GongGao>(GonggaoActivity.this, datalist, R.layout.item_gonggao) {
                                    @Override
                                    public void convert(ViewHolder holder, GongGao GongGao) {
                                        holder.setText(R.id.gonggao_title, GongGao.getNoteTitle());
                                        holder.setText(R.id.gonggao_time, GongGao.getNotePublishtime());
                                    }
                                };

                                GongGao_list.setAdapter(commonAdapter);
                            } else {
                                ToastUtil.show(GonggaoActivity.this, "服务器异常");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    
    /*
     * 搜素公告
     */
    private void filterGongGao(String text) {
        List<GongGao> filterDataList = new ArrayList<GongGao>();

        if (TextUtils.isEmpty(text)) {
            filterDataList = datalist;
        } else {
            filterDataList.clear();
            for (GongGao object : datalist) {
                String title = object.getNoteTitle();
                String time = object.getNotePublishtime();
                String publisher = object.getNotePublisher();



                if (title.contains(text)
                        || time.contains(text)
                        || publisher.contains(text)) {
                    filterDataList.add(object);
                }
            }
        }

        commonAdapter.updateListView(filterDataList);
    }

    /*
     * 删除公告
     */
    private void delete(int id, final int position) {
        String url = Constant.SERVER_URL + "notes/deleteNotes";

        Log.d("response",id+" ");
        OkHttpUtils.get()
                .url(url)
                .addParams("id",String.valueOf(id))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(GonggaoActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response+" ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            if (statusCode == 0) {
                                datalist.remove(position);
                                commonAdapter.updateListView(datalist);
                            } else {
                                ToastUtil.show(GonggaoActivity.this, "服务器异常");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}

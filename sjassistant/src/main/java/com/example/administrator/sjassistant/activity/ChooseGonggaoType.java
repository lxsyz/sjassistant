package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.GonggaoType;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/3.
 */
public class ChooseGonggaoType extends BaseActivity {

    private ListView type_list;

    private List<GonggaoType> datalist = new ArrayList<GonggaoType>();

    private int request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_choose_type);
        setTopText("公告类型");

        type_list = (ListView)findViewById(R.id.type_list);
        //datalist.add("hha");

        type_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.findViewById(R.id.iv_checked).setVisibility(View.VISIBLE);
                GonggaoType res = (GonggaoType) type_list.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra("result",res);
                setResult(2, intent);
                ChooseGonggaoType.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getGonggaoTypes();
    }

    /*
     * 获取公告类型列表
     */
    private void getGonggaoTypes() {

        datalist.clear();
        String url = Constant.SERVER_URL + "notes/showNotesType";

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(ChooseGonggaoType.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response + " ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");

                            Log.d("statusCode",statusCode+" ");
                            if (statusCode == 0) {
                                JSONObject data = object.getJSONObject("data");
                                JSONArray list = data.getJSONArray("list");

                                int len = list.length();

                                for (int i = 0;i < len;i++) {
                                    JSONObject o = list.getJSONObject(i);
                                    GonggaoType type = new GonggaoType();
                                    type.setId(o.getInt("id"));
                                    type.setCode(o.getString("code"));
                                    type.setName(o.getString("name"));
                                    type.setType(o.getString("type"));
                                    datalist.add(type);
                                }

                                type_list.setAdapter(new CommonAdapter<GonggaoType>(ChooseGonggaoType.this, datalist, R.layout.item_type) {
                                    @Override
                                    public void convert(ViewHolder holder,  GonggaoType type) {
                                        holder.setText(R.id.apartment_name,type.getName());


                                    }
                                });


                            } else {
                                ToastUtil.show(ChooseGonggaoType.this, "服务器异常");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}

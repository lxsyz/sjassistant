package com.example.administrator.sjassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.Bill;
import com.example.administrator.sjassistant.fragment.FinishedBillFragment;
import com.example.administrator.sjassistant.fragment.UnfinishedBillFragment;
import com.example.administrator.sjassistant.util.AppManager;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.ServerConfigUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by Administrator on 2016/4/27.
 */
public class InspectActivity extends FragmentActivity implements View.OnClickListener,UnfinishedBillFragment.DataListener ,FinishedBillFragment.FinishDataListener{

    ViewPager viewPager;
    PagerSlidingTabStrip tabs;
//    TabPageIndicator indicator;

    private ImageView bt_left,bt_right;
    private TextView tv_center;

    FinishedBillFragment finishedBillFragment;
    UnfinishedBillFragment unfinishedBillFragment;


    private List<Bill> datalist = new ArrayList<Bill>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inspect);
        initWindow();
        AppManager.getInstance().addActivity(this);

        SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        Constant.username = sp.getString("username", null);

        if (TextUtils.isEmpty(sp.getString("server_address", null))) {
            Constant.SERVER_URL = Constant.TEST_SERVER_URL;
            ServerConfigUtil.setServerConfig("219.234.5.13", "8080");
        } else {
            ServerConfigUtil.setServerConfig(this);
        }

        viewPager = (ViewPager)findViewById(R.id.pager);

        bt_left = (ImageView)findViewById(R.id.bt_left);
        tv_center = (TextView)findViewById(R.id.tv_center);
        bt_right = (ImageView)findViewById(R.id.bt_right);
        bt_right.setImageResource(R.drawable.search_white);
        tv_center.setText("搜索审批单据");
        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);



        tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabs.setIndicatorColor(Color.parseColor("#38B994"));
        tabs.setBackgroundResource(R.color.white);
        tabs.setIndicatorHeight(OperatorUtil.dp2px(this,2));
        //tabs.setDividerColor(R.color.white);

        //tabs.setTextColorResource(R.drawable.background_tab_text);
        //tabs.setActivateTextColor(R.color.background_text_selected);
        //tabs.setDeactivateTextColor(R.color.background_text);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;
            case R.id.bt_right:
                intent = new Intent(InspectActivity.this,SearchBillActivity.class);
                intent.putExtra("data",(ArrayList)datalist);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDataFinish(List<Bill> list) {
        datalist.addAll(list);
    }

    public void addData(List<Bill> list) {
        datalist.addAll(list);
    }

    @Override
    public void FinishBill(List<Bill> list) {
        datalist.addAll(list);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        String[] title = {"已审批单据","未审批单据"};

        FragmentManager fm;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    finishedBillFragment = new FinishedBillFragment();

                    return finishedBillFragment;
                case 1:
                    unfinishedBillFragment = new UnfinishedBillFragment();
                    return unfinishedBillFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return title.length;
        }

        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("response","activity resume");
        datalist.clear();
        //unfinshDatalist.clear();
        //if (finishedBillFragment == null || unfinishedBillFragment == null) {
        //    getUnfinishedWork();
         //   getFinished();
        //}
    }

    /*
         * 获取未完成
         */
    private void getUnfinishedWork() {

        String url = Constant.SERVER_URL + "bill/show";

        OkHttpUtils.post()
                .url(url)
                .addParams("userCode", Constant.username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(InspectActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response + " ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            JSONObject data = object.getJSONObject("data");
                            JSONArray list = data.getJSONArray("list");
                            if (statusCode == 0) {
                                Gson gson = new Gson();
                                int len = list.length();
                                if (len != 0) {
                                    List<Bill> unfinishedList = new ArrayList<Bill>();
                                    unfinishedList = gson.fromJson(list.toString(), new TypeToken<List<Bill>>() {
                                    }.getType());

                                    datalist.addAll(unfinishedList);

                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("data", (ArrayList) unfinishedList);
                                    unfinishedBillFragment.setArguments(bundle);

                                }



                            } else {
                                ToastUtil.showShort(InspectActivity.this, "服务器异常");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getFinished() {
        String url = Constant.SERVER_URL + "bill/showFinish";

        OkHttpUtils.post()
                .url(url)
                .addParams("userCode", Constant.username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(InspectActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response + " ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            JSONObject data = object.getJSONObject("data");
                            JSONArray list = data.getJSONArray("list");
                            if (statusCode == 0) {
                                Gson gson = new Gson();
                                int len = list.length();
                                if (len != 0) {
                                    List<Bill> finishedList = new ArrayList<Bill>();
                                    finishedList = gson.fromJson(list.toString(), new TypeToken<List<Bill>>() {
                                    }.getType());
                                    datalist.addAll(finishedList);


                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("data",(ArrayList) finishedList);
                                    finishedBillFragment.setArguments(bundle);
                                }

                            } else {
                                ToastUtil.showShort(InspectActivity.this, "服务器异常");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}

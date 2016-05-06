package com.example.administrator.sjassistant.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.SortAdapter;
import com.example.administrator.sjassistant.bean.ContactsPerson;
import com.example.administrator.sjassistant.bean.GroupDetail;
import com.example.administrator.sjassistant.bean.GroupPerson;
import com.example.administrator.sjassistant.bean.SortModel;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.PinyinComparator;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.SideBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/4.
 */
public class ContactsDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView search,delete;
    private EditText ed_name;

    private ListView sortListView;
    private SideBar sideBar;

    private SortAdapter adapter;

    private List<SortModel> datalist = new ArrayList<SortModel>();

    private PinyinComparator pinyinComparator;


    private String customer_name;
    private String group_name;
    private String id;  //小组id
    //from 1  2  3 分别代表来自  公司  小组  客户
    private int from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("response","contacts activity create");

        if (savedInstanceState != null) {
            customer_name = savedInstanceState.getString("customer_name");
            group_name = savedInstanceState.getString("group_name");
            id = savedInstanceState.getString("id");
            from = savedInstanceState.getInt("from", 0);
        } else {
            customer_name = getIntent().getStringExtra("customer_name");
            group_name = getIntent().getStringExtra("group_name");
            id = getIntent().getStringExtra("id");
            from = getIntent().getIntExtra("from", 0);
        }

        switch (from) {
            case 0:
                ToastUtil.showShort(ContactsDetailActivity.this,"服务器异常");
                break;
            case 1:
                break;
            case 2:
                if (group_name != null) {
                    setTopText(group_name);
                } else {
                    setTopText(" ");
                }
                getGroup();
                break;
            case 3:
                if (customer_name != null) {
                    setTopText(customer_name);
                } else {
                    setTopText(" ");
                }
                getContacts();
                break;
        }

    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_contacts_detail);


        search = (ImageView)findViewById(R.id.search);
        ed_name = (EditText)findViewById(R.id.search_content);
        delete = (ImageView)findViewById(R.id.delete_word);
        search.setOnClickListener(this);
        ed_name.setOnClickListener(this);
        delete.setOnClickListener(this);


        pinyinComparator = new PinyinComparator();

        sortListView = (ListView)findViewById(R.id.contacts_list);
        sideBar = (SideBar)findViewById(R.id.sidebar);

        sideBar.setmOnTouching(new SideBar.OnTouchingLetterChangeListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                Log.d("tag","touch");
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        //获取数据
        //datalist = dealData(getResources().getStringArray(R.array.test));




        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("position",position+" ");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                if (!TextUtils.isEmpty(ed_name.getText().toString())) {

//                    intent = new Intent(this, SearchResultActivity.class);
//                    intent.putExtra("name",ed_name.getText().toString());
//                    startActivity(intent);
                }
                break;
            case R.id.delete_word:
                ed_name.setText("");
                break;
        }
    }


    /*
     * 获取联系人
     */
    private void getContacts() {
        String url = Constant.SERVER_URL + "phoneBook/showCustomerUser";
        Log.d("customer_name",customer_name);
        OkHttpUtils.post()
                .url(url)
                .addParams("userCode", Constant.username)
                .addParams("customerName",customer_name)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error",e.getMessage());
                        ErrorUtil.NetWorkToast(ContactsDetailActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            if (statusCode == 0) {
                                Gson gson = new Gson();
                                JSONObject data = object.optJSONObject("data");
                                if (data != null && data.length() != 0) {
                                    JSONArray list = data.optJSONArray("list");
                                    if (list.length() != 0) {
                                        List<ContactsPerson> contactsPersonsList = new ArrayList<>();
//
                                        contactsPersonsList = gson.fromJson(list.toString(), new TypeToken<List<ContactsPerson>>() {
                                        }.getType());
                                        datalist = dealData(contactsPersonsList);
                                        Collections.sort(datalist, pinyinComparator);
                                        adapter = new SortAdapter(ContactsDetailActivity.this, datalist);
                                        sortListView.setAdapter(adapter);
                                    }
                                }
                            } else {
                                ToastUtil.showShort(ContactsDetailActivity.this, "获取用户列表失败");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /*
     * 获取小组联系人
     */
    private void getGroup() {
        Log.d("response","group");

        String url = Constant.SERVER_URL + "phoneBook/showGroupUser";
        Log.d("response",id);
        OkHttpUtils.get()
                .url(url)
                .addParams("id",id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage());
                        ErrorUtil.NetWorkToast(ContactsDetailActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            if (statusCode == 0) {
                                Gson gson = new Gson();
                                JSONObject data = object.optJSONObject("data");
                                if (data != null && data.length() != 0) {
                                    JSONArray list = data.optJSONArray("list");
                                    if (list.length() != 0) {
                                        Log.d("response", "list0->0" + list.length());
                                        List<GroupDetail> groupDetailList = new ArrayList<GroupDetail>();
//                                        for (int i = 0;i < list.length();i++) {
//                                            GroupDetail groupDetail = new GroupDetail();
//                                            groupDetail.setTrueName(list.getJSONObject(i).optString("trueName"));
//                                            groupDetail.setPhone(list.getJSONObject(i).optString("phone"));
//                                            groupDetailList.add(groupDetail);
//                                        }
                                        groupDetailList = gson.fromJson(list.toString(), new TypeToken<List<GroupDetail>>() {
                                        }.getType());
                                        datalist = dealGroupData(groupDetailList);
                                        Collections.sort(datalist, pinyinComparator);
                                        adapter = new SortAdapter(ContactsDetailActivity.this, datalist);
                                        sortListView.setAdapter(adapter);
                                    }
                                }
                            } else {
                                ToastUtil.showShort(ContactsDetailActivity.this, "获取小组列表失败");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /*
     * 处理数据，获取首字母
     */
    private List<SortModel> dealGroupData(List<GroupDetail> data){
        List<SortModel> mSortList = new ArrayList<SortModel>();
        for (int i = 0; i < data.size(); i++) {
            String name = data.get(i).getTrueName();
            SortModel sortModel = new SortModel();
            sortModel.setName(name);
            sortModel.setPhoneNumber(data.get(i).getPhone());
            sortModel.setGroup(data.get(i).getDeptName());
            //汉字转换成拼音
            String pinyin = OperatorUtil.getFirstChar(name);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetter(sortString.toUpperCase());
            } else {
                sortModel.setSortLetter("#");
            }

            mSortList.add(sortModel);
        }

        return mSortList;
    }

    /*
     * 处理数据，获取首字母
     */
    private List<SortModel> dealData(List<ContactsPerson> data){
        List<SortModel> mSortList = new ArrayList<SortModel>();
        for (int i = 0; i < data.size(); i++) {
            String name = data.get(i).getLinkName();
            SortModel sortModel = new SortModel();
            sortModel.setName(name);
            sortModel.setPhoneNumber(data.get(i).getLinkPhone());
            sortModel.setGroup(data.get(i).getCustomerName());
            //汉字转换成拼音
            String pinyin = OperatorUtil.getFirstChar(name);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetter(sortString.toUpperCase());
            } else {
                sortModel.setSortLetter("#");
            }

            mSortList.add(sortModel);
        }

        return mSortList;
    }

    /*
     * 搜索
     */
    private void filterData(String filter) {


        List<SortModel> filterDataList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filter)) {
            filterDataList = datalist;
        } else {
            filterDataList.clear();
            for (SortModel sortModel : datalist) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(
                        filter.toString().toUpperCase()
                ) != -1 || OperatorUtil.getFirstChar(name).toUpperCase().startsWith(filter.toString().toUpperCase())) {
                    filterDataList.add(sortModel);
                }
            }
        }

        Collections.sort(filterDataList, pinyinComparator);
        adapter.updateListView(filterDataList);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (customer_name != null)
            outState.putString("customer_name",customer_name);
        if (group_name != null)
            outState.putString("group_name",group_name);
        if (from != 0)
            outState.putInt("from", from);
        if (id != null)
            outState.putString("id",id);

    }
}

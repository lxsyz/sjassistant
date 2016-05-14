package com.example.administrator.sjassistant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.AddContactAdapter;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.bean.MyContacts;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.bean.SortModel;
import com.example.administrator.sjassistant.util.AppManager;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.PinyinComparator;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.MyDialog;
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
 * Created by Administrator on 2016/4/7.
 */
public class AddChatContact extends Activity implements View.OnClickListener {

    private ImageView search,delete;
    private EditText ed_name;

    private TextView bt_left,bt_right,tv_center;
    private ImageView bt_left2;

    private String name;

    private ListView sortListView;
    private SideBar sideBar;
    private AddContactAdapter adapter;

    private List<SortModel> result = new ArrayList<>();

    private List<MyContacts> contactData = new ArrayList<MyContacts>();

    private CommonAdapter<MyContacts> contactAdapter;

    private List<SortModel> datalist = new ArrayList<SortModel>();

    private PinyinComparator pinyinComparator;

    private List<Person> personDatas = new ArrayList<>();

    private int count = 0;

    //from = 1表示来自于添加发送消息的联系人
    private int from = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addchat);
        AppManager.getInstance().addActivity(this);
        initWindow();
        initView();

        from = getIntent().getIntExtra("from",0);

        count = getIntent().getIntExtra("count",0);
        personDatas = (ArrayList<Person>)getIntent().getSerializableExtra("data");
        if (count != 0) {
            bt_right.setText("确定(" + count + ")");
        } else bt_right.setText("确定");
    }

    private void initView() {
        search = (ImageView)findViewById(R.id.search);
        ed_name = (EditText)findViewById(R.id.search_content);
        delete = (ImageView)findViewById(R.id.delete_word);
        search.setOnClickListener(this);
        ed_name.setOnClickListener(this);
        delete.setOnClickListener(this);

        tv_center = (TextView)findViewById(R.id.tv_center);

        tv_center.setText("添加联系人");
        bt_left = (TextView)findViewById(R.id.bt_left2);
        bt_right = (TextView)findViewById(R.id.bt_right);
        bt_left.setVisibility(View.VISIBLE);

        bt_left2 = (ImageView)findViewById(R.id.bt_left);
        bt_left2.setVisibility(View.INVISIBLE);

        bt_right.setText("确定");
        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);


        pinyinComparator = new PinyinComparator();

        sortListView = (ListView)findViewById(R.id.contacts_list);
        sideBar = (SideBar)findViewById(R.id.sidebar);

        sideBar.setmOnTouching(new SideBar.OnTouchingLetterChangeListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                Log.d("tag", "touch");
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });





        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView v = (ImageView) view.findViewById(R.id.add);
                SortModel sm = (SortModel)sortListView.getItemAtPosition(position);
                Log.d("position", position + " ");
                if (sm.getChecked() == 0) {
                    v.setImageResource(R.drawable.radio_checked);
                    sm.setChecked(1);
                    count++;
                    result.add(sm);

                    //if (from == 1) {
                    //}
                    adapter.notifyDataSetChanged();
                } else {
                    v.setImageResource(R.drawable.radio_unchecked);
                    sm.setChecked(0);
                    count--;
                    result.remove(sm);

                    //if (from == 1) {
                    //}
                    adapter.notifyDataSetChanged();
                }
                if (count != 0) {
                    bt_right.setText("确定(" + count + ")");
                } else {
                    bt_right.setText("确定");
                }
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

            case R.id.bt_left2:
                onBackPressed();
                break;

            case R.id.bt_right:
                //if (from == 1) {
                    if (count > 8) {
                        MyDialog dialog = new MyDialog(AddChatContact.this, R.style.dialog_style);
                        dialog.show();
                        dialog.setMain_text("多方通话最多允许添加8个人");
                        dialog.setVisibility(View.GONE);
                        dialog.setCenterVisibility(View.VISIBLE);
                    } else {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("result",(ArrayList)result);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        this.finish();
                    }
                //}
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        getData();

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

    /*
     *
     */
    private void getData() {
        String url = Constant.SERVER_URL + "phoneBook/queryAllUser";

        OkHttpUtils.post()
                .url(url)
                //.addParams("id",String.valueOf(id))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(AddChatContact.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.optInt("statusCode", 0);

                            if (statusCode == 0) {
                                JSONObject data = object.optJSONObject("data");
                                JSONArray list = data.optJSONArray("list");
                                int len = list.length();
                                if (len != 0) {
                                    Gson gson = new Gson();
                                    contactData = gson.fromJson(list.toString(), new TypeToken<List<MyContacts>>() {
                                    }.getType());

//                                    for (MyContacts c : contactData) {
//                                        for (Person p : personDatas) {
//                                            if (c.getPhone().equals(p.getLinkPhone())) {
//                                                c.setCheckState(true);
//                                                //result.add(c);
//                                                break;
//                                            }
//                                        }
//                                    }
//

                                    datalist = dealData(contactData);
//                                    contactAdapter = new CommonAdapter<MyContacts>(AddChatContact.this, contactData, R.layout.item_add_person) {
//                                        @Override
//                                        public void convert(ViewHolder holder, MyContacts contact) {
//                                            holder.getView(R.id.right_arrow1).setVisibility(View.INVISIBLE);
//                                            holder.setText(R.id.group, contact.getRoleName());
//                                            holder.setText(R.id.name, contact.getTrueName());
//
//                                        }
//                                    };
                                    Collections.sort(datalist, pinyinComparator);
                                    adapter = new AddContactAdapter(AddChatContact.this, datalist);
                                    sortListView.setAdapter(adapter);

                                }


                            } else {
                                ToastUtil.showShort(AddChatContact.this, "服务器异常");
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
    private List<SortModel> dealData(List<MyContacts> data){
        List<SortModel> mSortList = new ArrayList<SortModel>();
        for (int i = 0; i < data.size(); i++) {
            String name = data.get(i).getTrueName();
            SortModel sortModel = new SortModel();
            sortModel.setName(name);
            sortModel.setPhoneNumber(data.get(i).getPhone());
            sortModel.setGroup(data.get(i).getDeptName());
            sortModel.setUserCode(data.get(i).getUserCode());
            sortModel.setChecked(data.get(i).isCheckState()?1:0);
            //sortModel.setCustomerDept(data.get(i).getCustomerDept());
            //sortModel.setCustomerPost(data.get(i).getCustomerPost());
            //sortModel.setCustomerType(data.get(i).getCustomerType());
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
     * 搜索联系人
     */
    private void filterData(String filter) {
        List<SortModel> filterDataList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filter)) {
            filterDataList = datalist;
        } else {
            filterDataList.clear();
            for (SortModel sortModel:datalist) {
                String name = sortModel.getName();
                if (name.toUpperCase().contains(
                        filter.toUpperCase()
                ) || OperatorUtil.getFirstChar(name).toUpperCase().startsWith(filter.toUpperCase())) {
                    filterDataList.add(sortModel);
                }
            }
        }

        Collections.sort(filterDataList,pinyinComparator);
        adapter.updateListView(filterDataList);
    }

    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}

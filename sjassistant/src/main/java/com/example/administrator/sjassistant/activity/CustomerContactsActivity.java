package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.ContactsPerson;
import com.example.administrator.sjassistant.bean.MyContacts;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.AddContactsWin;
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
 * Created by Administrator on 2016/4/2.
 */
public class CustomerContactsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView search,delete;
    private EditText ed_name;

    private ImageView bt_right;

    private AddContactsWin add_contacts;

    private ListView customer_list;
    private ListView search_list;
    //private List<String> datalist = new ArrayList<String>();

    private List<ContactsPerson> datalist = new ArrayList<>();

    private TextView text;

    private SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (add_contacts == null) {
            add_contacts = new AddContactsWin(CustomerContactsActivity.this,CustomerContactsActivity.this, OperatorUtil.dp2px(this,250),
                    OperatorUtil.dp2px(this,160));

            add_contacts.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (!hasFocus) {
                        add_contacts.dismiss();
                    }
                }
            });
        }
        add_contacts.setFocusable(true);
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_customer);
        setTopText("客户");

        search = (ImageView)findViewById(R.id.search);
        ed_name = (EditText)findViewById(R.id.search_content);
        delete = (ImageView)findViewById(R.id.delete_word);
        text = (TextView)findViewById(R.id.customer_prompt);
        search.setOnClickListener(this);
        delete.setOnClickListener(this);


        bt_right = (ImageView) layout_top.findViewById(R.id.bt_right);
        setRightButtonRes(R.drawable.more);
        setRightButton(View.VISIBLE);
        bt_right.setOnClickListener(this);

        customer_list = (ListView)findViewById(R.id.customer_list);
        search_list = (ListView)findViewById(R.id.search_list);


        customer_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactsPerson customer_name = (ContactsPerson) customer_list.getItemAtPosition(position);
                Intent intent = new Intent(CustomerContactsActivity.this,ContactsDetailActivity.class);
                intent.putExtra("from",3);
                intent.putExtra("customer_name",customer_name.getCustomerName());
                startActivity(intent);
            }
        });

        ed_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {

                    queryUserByType(ed_name.getText().toString());

                    return true;
                }
                return false;
            }
        });

//        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refresh();
//            }
//        });
    }

    private void refresh() {
        refreshLayout.setRefreshing(false);
        getCompany();
    }



    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bt_right:
                if (add_contacts.isShowing()) {
                    add_contacts.dismiss();
                } else
                    add_contacts.showAsDropDown(bt_right,0,0);
                break;
            case R.id.choose:
                intent = new Intent(CustomerContactsActivity.this,ChooseContactsActivity.class);
                startActivity(intent);
                break;
            case R.id.add:
                intent = new Intent(CustomerContactsActivity.this,AddContactsActivity.class);
                startActivity(intent);
                break;
            case R.id.search:
                if (!TextUtils.isEmpty(ed_name.getText().toString())) {

                }
                break;
            case R.id.delete_word:
                ed_name.setText("");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (add_contacts.isShowing()) {
            add_contacts.dismiss();
        }


        if (ed_name != null &&ed_name.getText().toString().trim().length() == 0)
            getCompany();

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
     * 获取公司列表
     */
    private void getCompany() {
        String url = Constant.SERVER_URL + "phoneBook/showCustomer";

        OkHttpUtils.post()
                .url(url)
                .addParams("userCode",Constant.username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(CustomerContactsActivity.this);
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
                                        text.setText("选择客户");
                                        datalist = gson.fromJson(list.toString(), new TypeToken<List<ContactsPerson>>() {
                                        }.getType());

                                    } else {
                                        text.setText("没有客户");
                                    }
                                    if (commonAdapter != null) {
                                        commonAdapter.updateListView(datalist);
                                    } else {
                                        commonAdapter = new CommonAdapter<ContactsPerson>(CustomerContactsActivity.this, datalist, R.layout.item_choose_customer) {
                                            @Override
                                            public void convert(ViewHolder holder, ContactsPerson cp) {
                                                holder.setText(R.id.customer_name, cp.getCustomerName());
                                            }
                                        };
                                        customer_list.setAdapter(commonAdapter);
                                    }
                                }
                            } else {
                                ToastUtil.showShort(CustomerContactsActivity.this, "获取用户列表失败");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /*
     * 所有联系人搜索
     */
    private void queryUserByType(String name) {

        customer_list.setVisibility(View.GONE);

        String url = Constant.SERVER_URL + "phoneBook/queryUserByType";

        OkHttpUtils.post()
                .url(url)
                .addParams("userCode",Constant.username)
                .addParams("trueName",name)
                .addParams("roleName","customer")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage()+" ");
                        ErrorUtil.NetWorkToast(CustomerContactsActivity.this);
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
                                        text.setText("选择客户");

                                        search_list.setVisibility(View.VISIBLE);
                                        List<ContactsPerson> searchlist = gson.fromJson(list.toString(), new TypeToken<List<ContactsPerson>>() {
                                        }.getType());

                                        if (searchAdapter != null) {
                                            searchAdapter.updateListView(searchlist);
                                        } else {
                                            searchAdapter = new CommonAdapter<ContactsPerson>(CustomerContactsActivity.this, searchlist, R.layout.item_company) {
                                                @Override
                                                public void convert(final ViewHolder holder, final ContactsPerson contact) {
                                                    holder.getView(R.id.phone).setVisibility(View.VISIBLE);
                                                    holder.getView(R.id.right_arrow1).setVisibility(View.GONE);
                                                    holder.setText(R.id.name, contact.getLinkName());
                                                    //holder.setText(R.id.group, contact.getCustomerDept());

                                                    holder.getView(R.id.phone).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(CustomerContactsActivity.this, MoreContact.class);
                                                            Person person = new Person();
                                                            person.setLinkPhone(contact.getLinkPhone());
                                                            person.setLinkName(contact.getLinkName());
                                                            person.setUserCode(contact.getUserCode());
                                                            Bundle bundle = new Bundle();
                                                            bundle.putSerializable("person", person);
                                                            intent.putExtras(bundle);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                            };
                                            Log.d("response","visi->"+search_list.getVisibility()+"  "+searchAdapter.getCount());
                                            search_list.setAdapter(searchAdapter);
                                        }
                                    } else {
                                        search_list.setVisibility(View.GONE);
                                        text.setText("没有客户");
                                    }
                                }
                            } else {
                                ToastUtil.showShort(CustomerContactsActivity.this, "获取用户列表失败");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /*
     * 搜索
     */
    private void filterData(String tex) {

        List<ContactsPerson> filterList = new ArrayList<>();
        text.setText("选择客户");
        if (TextUtils.isEmpty(tex)) {
            filterList = datalist;
            search_list.setVisibility(View.GONE);
            customer_list.setVisibility(View.VISIBLE);

        } else {
            customer_list.setVisibility(View.VISIBLE);
            filterList.clear();
            for (ContactsPerson person:datalist) {
                if (person.getCustomerName().contains(tex)) {
                    filterList.add(person);
                }
            }
        }
        commonAdapter.updateListView(filterList);
    }

    private CommonAdapter<ContactsPerson> commonAdapter;
    private CommonAdapter<ContactsPerson> searchAdapter;
}

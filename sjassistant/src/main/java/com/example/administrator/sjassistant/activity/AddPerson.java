package com.example.administrator.sjassistant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Department;
import com.example.administrator.sjassistant.bean.Manager;
import com.example.administrator.sjassistant.util.AppManager;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.view.MyDialog;
import com.example.administrator.sjassistant.view.SideBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/25.
 */
public class AddPerson extends Activity implements View.OnClickListener {

    private ImageView search, delete;
    private EditText ed_name;

    private TextView bt_left, bt_right, tv_center;
    private ImageView bt_left2;

    private String name;

    //private ListView listview;
    //private SideBar sideBar;
    private ListView apartment_list;
    private ListView manager_list;

    private List<Integer> result = new ArrayList<Integer>();

    private List<Manager> managerData = new ArrayList<Manager>();

    private List<Department> departmentData = new ArrayList<Department>();
    private CommonAdapter<Manager> managerAdapter;
    private CommonAdapter<Department> departmentAdapter;

    private int count = 0;

    //from = 1表示来自于添加发送消息的联系人
    private int from = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_person);
        AppManager.getInstance().addActivity(this);
        initWindow();
        initView();

        from = getIntent().getIntExtra("from", 0);
        count = getIntent().getIntExtra("count", 0);
        if (count != 0) {
            bt_right.setText("确定(" + count + ")");
        } else bt_right.setText("确定");

    }

    private void initView() {
        search = (ImageView) findViewById(R.id.search);
        ed_name = (EditText) findViewById(R.id.search_content);
        delete = (ImageView) findViewById(R.id.delete_word);
        search.setOnClickListener(this);
        ed_name.setOnClickListener(this);
        delete.setOnClickListener(this);

        tv_center = (TextView) findViewById(R.id.tv_center);

        apartment_list = (ListView)findViewById(R.id.apartment_list);
        manager_list = (ListView)findViewById(R.id.manager_list);

        tv_center.setText("添加联系人");
        bt_left = (TextView) findViewById(R.id.bt_left2);
        bt_right = (TextView) findViewById(R.id.bt_right);
        bt_left.setVisibility(View.VISIBLE);

        bt_left2 = (ImageView) findViewById(R.id.bt_left);
        bt_left2.setVisibility(View.INVISIBLE);

        bt_right.setText("确定");
        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);

        Department m = new Department();
        m.setName("全部分所");departmentData.add(m);
        Department m1 = new Department();
        m1.setName("北京分所");departmentData.add(m1);
        Manager m2 = new Manager();
        m2.setName("哈哈");managerData.add(m2);
        Manager m3 = new Manager();
        m3.setName("啊啊");managerData.add(m3);

        departmentAdapter = new CommonAdapter<Department>(this,departmentData,R.layout.item_add_person) {
            @Override
            public void convert(ViewHolder holder, Department department) {
                holder.getView(R.id.iv_checked).setVisibility(View.GONE);
                holder.getView(R.id.group).setVisibility(View.GONE);

                holder.setText(R.id.name,department.getName());
            }
        };

        apartment_list.setAdapter(departmentAdapter);
        setListViewHeight(apartment_list);
        managerAdapter = new CommonAdapter<Manager>(this,managerData,R.layout.item_add_person) {
            @Override
            public void convert(ViewHolder holder, Manager manager) {
                holder.getView(R.id.right_arrow1).setVisibility(View.INVISIBLE);

                holder.setText(R.id.name,manager.getName());

            }
        };

        manager_list.setAdapter(managerAdapter);
        setListViewHeight(manager_list);
        manager_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Manager m = (Manager) manager_list.getItemAtPosition(position);
                ImageView iv = (ImageView) view.findViewById(R.id.iv_checked);

                if (m.isCheckState()) {
                    m.setCheckState(false);
                    iv.setImageResource(R.drawable.radio_unchecked);
                    count--;
                } else {
                    m.setCheckState(true);
                    iv.setImageResource(R.drawable.radio_checked);
                    count++;
                }
                managerAdapter.notifyDataSetChanged();
                if (count != 0) {
                    bt_right.setText("确定(" + count + ")");
                } else {
                    bt_right.setText("确定");
                }
            }
        });

//        adapter = new CommonAdapter<Manager>(this, da, R.layout.item_add_person) {
//            @Override
//            public void convert(ViewHolder holder, Manager s) {
//                holder.setText(R.id.name, s.getName());
//
//                if (holder.getPosition() == 1) {
//                    holder.getView(R.id.iv_checked).setVisibility(View.GONE);
//                    holder.getView(R.id.my_view).setVisibility(View.VISIBLE);
//                    holder.getView(R.id.line).setVisibility(View.GONE);
//                    holder.getView(R.id.right_arrow1).setVisibility(View.VISIBLE);
//                } else if (holder.getPosition() > 1){
//                    holder.getView(R.id.iv_checked).setVisibility(View.VISIBLE);
//                    holder.getView(R.id.my_view).setVisibility(View.GONE);
//                    holder.getView(R.id.right_arrow1).setVisibility(View.INVISIBLE);
//                    holder.getView(R.id.line).setVisibility(View.VISIBLE);
//                } else if (holder.getPosition() == da.size() - 1){
//                    holder.getView(R.id.iv_checked).setVisibility(View.VISIBLE);
//                    holder.getView(R.id.my_view).setVisibility(View.GONE);
//                    holder.getView(R.id.right_arrow1).setVisibility(View.INVISIBLE);
//                    holder.getView(R.id.line).setVisibility(View.GONE);
//                }
//                else {
//                    holder.getView(R.id.line).setVisibility(View.VISIBLE);
//                    holder.getView(R.id.iv_checked).setVisibility(View.GONE);
//                    holder.getView(R.id.my_view).setVisibility(View.GONE);
//                    holder.getView(R.id.right_arrow1).setVisibility(View.VISIBLE);
//                }
//            }
//        };

//        listview.setAdapter(adapter);
//
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Manager m = (Manager)listview.getItemAtPosition(position);
//                ImageView iv = (ImageView)view.findViewById(R.id.iv_checked);
//                if (position > 1) {
//                    if (m.isCheckState()) {
//                        m.setCheckState(false);
//                        iv.setImageResource(R.drawable.radio_unchecked);
//                        count--;
//                    } else {
//                        m.setCheckState(true);
//                        iv.setImageResource(R.drawable.radio_checked);
//                        count++;
//                    }
//                    adapter.notifyDataSetChanged();
//                    if (count != 0) {
//                        bt_right.setText("确定(" + count + ")");
//                    } else {
//                        bt_right.setText("确定");
//                    }
//                } else {
//                    Intent intent = new Intent(AddPerson.this, AddChatContact.class);
//                    intent.putExtra("name", name);
//                    startActivity(intent);
//                }
//            }
//        });
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
                //filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /*
     * 获取部门  人员选择
     */
    private void getData() {
        String url = Constant.SERVER_URL + "message/queryPerson";

        OkHttpUtils.post()
                .url(url)
                .addParams("id","1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error",e.getMessage()+" ");
                        ErrorUtil.NetWorkToast(AddPerson.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("resposne","response");
                    }
                });
    }



    /*
     * 搜索联系人
     */


    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_word:
                ed_name.setText("");
                break;

            case R.id.bt_left2:
                onBackPressed();
                break;

            case R.id.bt_right:
                if (from == 1) {
                    StringBuilder sb = new StringBuilder();

                    if (result.size() > 0) {
                        boolean need = false;

                        for (Integer i : result) {
                            if (need) {
                                sb.append(",");
                            }
                            //sb.append(datalist.get(i));
                            need = true;
                        }
                    }
                    Intent intent = new Intent(AddPerson.this,PostMessageActivity.class);
                    intent.putExtra("result",sb.toString());
                    setResult(1, intent);
                    this.finish();
                }
                else {
                    if (count > 8) {
                        MyDialog dialog = new MyDialog(AddPerson.this, R.style.dialog_style);
                        dialog.show();
                        dialog.setMain_text("多方通话最多允许添加8个人");
                        dialog.setVisibility(View.GONE);
                        dialog.setCenterVisibility(View.VISIBLE);
                    } else {
                        this.finish();
                    }
                }
                break;
        }
    }



    /**
     * 设置listview高度的方法
     * @param listView
     */
    public void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);   //获得每个子item的视图
            listItem.measure(0, 0);   //先判断写入的widthMeasureSpec和heightMeasureSpec是否和当前的值相等，如果不等，重新调用onMeasure()
            totalHeight += listItem.getMeasuredHeight();   //累加不解释
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));   //加上每个item之间的距离
        listView.setLayoutParams(params);
    }
}

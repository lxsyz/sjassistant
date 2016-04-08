package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.view.AddContactsWin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/2.
 */
public class CustomerContactsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView search,delete;
    private EditText ed_name;

    private ImageView bt_right;

    private AddContactsWin add_contacts;

    private ListView customer_list;
    private List<String> datalist = new ArrayList<String>();

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
        search.setOnClickListener(this);
        delete.setOnClickListener(this);


        bt_right = (ImageView) layout_top.findViewById(R.id.bt_right);
        setRightButtonRes(R.drawable.more);
        setRightButton(View.VISIBLE);
        bt_right.setOnClickListener(this);

        customer_list = (ListView)findViewById(R.id.customer_list);

        datalist.add("1");
        datalist.add("2");
        datalist.add("3");
        datalist.add("4");
        datalist.add("5");

        customer_list.setAdapter(new CommonAdapter<String>(this, datalist, R.layout.item_choose_customer) {
            @Override
            public void convert(ViewHolder holder, String s) {

            }
        });

        customer_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) customer_list.getItemAtPosition(position);
                Intent intent = new Intent(CustomerContactsActivity.this,ContactsDetailActivity.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });

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

    @Override
    protected void onResume() {
        super.onResume();
        if (add_contacts.isShowing()) {
            add_contacts.dismiss();
        }
    }
}

package com.example.administrator.sjassistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.activity.CompanyActivity;
import com.example.administrator.sjassistant.activity.CustomerContactsActivity;
import com.example.administrator.sjassistant.activity.SearchResultActivity;

/**
 * Created by Administrator on 2016/3/28.
 */
public class ContactsFragment extends Fragment implements View.OnClickListener {

    private LinearLayout search_layout,company_layout,customer_layout,group_layout;
    private ImageView search,delete;
    private EditText ed_name;

    private ImageView btn_left,btn_right;
    private TextView title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts,container,false);

        initView(rootView);

        return rootView;
    }

    private void initView(View root) {
        btn_left = (ImageView)root.findViewById(R.id.bt_left);
        title = (TextView)root.findViewById(R.id.tv_center);
        btn_left.setVisibility(View.INVISIBLE);
        title.setText("联系人");

        company_layout = (LinearLayout)root.findViewById(R.id.company_layout);
        customer_layout = (LinearLayout)root.findViewById(R.id.customer_layout);
        group_layout = (LinearLayout)root.findViewById(R.id.group_layout);

        search_layout = (LinearLayout)root.findViewById(R.id.search_layout);
        search = (ImageView)search_layout.findViewById(R.id.search);
        ed_name = (EditText)search_layout.findViewById(R.id.search_content);
        delete = (ImageView)search_layout.findViewById(R.id.delete_word);

        search.setOnClickListener(this);
        ed_name.setOnClickListener(this);
        delete.setOnClickListener(this);
        search_layout.setOnClickListener(this);
        company_layout.setOnClickListener(this);
        customer_layout.setOnClickListener(this);
        group_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {

            case R.id.search:
                if (!TextUtils.isEmpty(ed_name.getText().toString())) {
                    intent = new Intent(getActivity(), SearchResultActivity.class);
                    intent.putExtra("name",ed_name.getText().toString());
                    startActivity(intent);
                }
                break;
            case R.id.delete_word:
                ed_name.setText("");
                break;
            case R.id.company_layout:
                intent = new Intent(getActivity(), CompanyActivity.class);
                startActivity(intent);
                break;
            case R.id.customer_layout:
                intent = new Intent(getActivity(), CustomerContactsActivity.class);
                startActivity(intent);
                break;
            case R.id.group_layout:
                break;
        }


    }
}

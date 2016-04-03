package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.view.AddContactsWin;

/**
 * Created by Administrator on 2016/4/2.
 */
public class CustomerContactsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView bt_right;

    private AddContactsWin add_contacts;



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


        bt_right = (ImageView) layout_top.findViewById(R.id.bt_right);
        setRightButtonRes(R.drawable.more);
        setRightButton(View.VISIBLE);
        bt_right.setOnClickListener(this);
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

                break;
        }
    }



}

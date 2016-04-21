package com.example.administrator.sjassistant.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.administrator.sjassistant.R;

import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class SearchBillActivity extends BaseActivity implements View.OnClickListener {

    //private TabLayout tabLayout;

    private List<String> list_title;

    private ImageView search,delete;
    private EditText ed_name;

    private TextView bt_right_text;

    private ImageView bt_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_search_bill);
        setTopText("搜索审批单据");

        search = (ImageView)findViewById(R.id.search);
        ed_name = (EditText)findViewById(R.id.search_content);
        delete = (ImageView)findViewById(R.id.delete_word);

        ed_name.setHint("输入搜索关键字");
        search.setOnClickListener(this);
        ed_name.setOnClickListener(this);
        delete.setOnClickListener(this);

        bt_right_text = (TextView)findViewById(R.id.bt_right_text);
        bt_right = (ImageView)findViewById(R.id.bt_right);

        bt_right.setVisibility(View.INVISIBLE);
        bt_right_text.setVisibility(View.VISIBLE);

        bt_right_text.setText("搜索");

        bt_right_text.setOnClickListener(this);
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
            case R.id.bt_right_text:
                break;

        }
    }
}

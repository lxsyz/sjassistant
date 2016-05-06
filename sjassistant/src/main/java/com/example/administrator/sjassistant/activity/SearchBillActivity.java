package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Bill;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class SearchBillActivity extends BaseActivity implements View.OnClickListener {

    //private TabLayout tabLayout;

    private ImageView search,delete;
    private EditText ed_name;

    private TextView bt_right_text;

    private ImageView bt_right;

    private List<Bill> datalist = new ArrayList<>();

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        datalist = (ArrayList<Bill>)getIntent().getSerializableExtra("data");
        if (datalist == null) {
            datalist = new ArrayList<>();
        }
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_search_bill);
        setTopText("搜索审批单据");

        search = (ImageView)findViewById(R.id.search);
        ed_name = (EditText)findViewById(R.id.search_content);
        delete = (ImageView)findViewById(R.id.delete_word);
        listView = (ListView)findViewById(R.id.listview);

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
                if (TextUtils.isEmpty(ed_name.getText().toString())) {
                    return;
                }

                filterData(ed_name.getText().toString());
                break;

        }
    }

    /*
     * 搜索
     */
    private void filterData(String text) {
        List<Bill> filterData = new ArrayList<>();
        if (TextUtils.isEmpty(text)) {
            filterData = datalist;
        } else {
            for (Bill bill : datalist) {
                if (bill.getDealTime().contains(text)
                        || bill.getDealResult().contains(text)
                        || bill.getUserCode().contains(text)
                        || bill.getBillType().contains(text)) {
                    filterData.add(bill);
                }
            }
        }

        listView.setAdapter(new CommonAdapter<Bill>(SearchBillActivity.this,datalist,R.layout.item_a) {

            @Override
            public void convert(ViewHolder holder, Bill bill) {
                String title = bill.getUserCode() + "的单据需要你审批";
                holder.setText(R.id.id_title, title);
                holder.setText(R.id.id_type_value, bill.getBillType());
                holder.setText(R.id.id_time_value, bill.getDealTime());
                holder.setText(R.id.read_flag, bill.getDealResult());

                if (TextUtils.isEmpty(bill.getDealResult()) || bill.getDealResult().equals("null")) {
                    holder.setText(R.id.read_flag, "未读");
                    ((TextView) holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.unread));
                } else if (bill.getDealResult().equals("未读") || bill.getDealResult().equals("退回未提交")) {
                    ((TextView) holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.unread));
                } else if (bill.getDealResult().equals("通过未提交") || bill.getDealResult().equals("已读")) {
                    ((TextView) holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.read));
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bill bill = (Bill)listView.getItemAtPosition(position);
                Intent intent = new Intent(SearchBillActivity.this,BillInspectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bill",bill);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}

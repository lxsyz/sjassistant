package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Bill;
import com.example.administrator.sjassistant.bean.BillDetail;
import com.example.administrator.sjassistant.util.OperatorUtil;

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
                    if (TextUtils.isEmpty(ed_name.getText().toString())) {
                        return;
                    }

                    filterData(ed_name.getText().toString());
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
        Log.d("response",datalist.size()+" ");
        List<Bill> filterData = new ArrayList<>();
        if (TextUtils.isEmpty(text)) {
            filterData.addAll(datalist);
        } else {
            for (Bill bill : datalist) {
                if (!TextUtils.isEmpty(bill.getDealTime()) && bill.getDealTime().contains(text)) {
                    filterData.add(bill);
                    continue;
                }
                if (!TextUtils.isEmpty(bill.getDealResult()) && bill.getDealResult().contains(text)) {
                    filterData.add(bill);
                    continue;
                }

                if (!TextUtils.isEmpty(bill.getUserCode()) && bill.getUserCode().contains(text)) {
                    filterData.add(bill);
                    continue;
                }
                if (!TextUtils.isEmpty(bill.getBillType()) && bill.getBillType().contains(text)) {
                    filterData.add(bill);
                    continue;
                }
            }
        }

        listView.setAdapter(new CommonAdapter<Bill>(SearchBillActivity.this, filterData, R.layout.item_a) {

            @Override
            public void convert(ViewHolder holder, Bill bill) {
                String title = bill.getUserCode() + "的单据需要你审批";
                holder.setText(R.id.id_title, title);

                if (bill.isDeal()) {
                    holder.getView(R.id.iv_inspect).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.iv_inspect).setVisibility(View.GONE);
                }

                holder.getView(R.id.read_flag).setVisibility(View.INVISIBLE);
                List<BillDetail> billDetails = bill.getListbf();
                //待办中的不定条目部分
                LinearLayout container = holder.getView(R.id.container);
                container.removeAllViews();
                if (billDetails != null) {
                    int len = billDetails.size();

                    RelativeLayout rll = new RelativeLayout(SearchBillActivity.this);
                    TextView billType = new TextView(SearchBillActivity.this);
                    String billType_text = "单据类型:";
                    String value = bill.getBillTypeChina();

                    billType.setMaxEms(5);
                    billType.setText(billType_text);
                    billType.setTextColor(Color.parseColor("#88444444"));
                    RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    billType.setLayoutParams(lp1);

                    TextView billValue = new TextView(SearchBillActivity.this);
                    billValue.setText(value);
                    billValue.setTextColor(Color.parseColor("#414141"));
                    RelativeLayout.LayoutParams rlp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    rlp1.leftMargin = OperatorUtil.dp2px(SearchBillActivity.this, 70);
                    billValue.setLayoutParams(rlp1);

                    rll.addView(billType);
                    rll.addView(billValue);
                    container.addView(rll);
                    for (int i = 0; i < len; i++) {
                        RelativeLayout rl = new RelativeLayout(SearchBillActivity.this);
                        TextView displayName = new TextView(SearchBillActivity.this);
                        String displatName_text;
                        String displayKey_text;

                        displatName_text = billDetails.get(i).getDisplayName();
                        displayKey_text = billDetails.get(i).getDisplayValue();

                        if (!TextUtils.isEmpty(displatName_text)) {
                            displatName_text += ":";
                            displayName.setText(displatName_text);
                        } else {
                            displayName.setText("");
                        }


                        displayName.setTextColor(Color.parseColor("#88444444"));
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                        displayName.setLayoutParams(lp);

                        TextView displayKey = new TextView(SearchBillActivity.this);


                        if (!TextUtils.isEmpty(displayKey_text)) {
                            displayKey_text += ":";
                            displayKey.setText(displayKey_text);
                        } else {
                            displayKey.setText("");
                        }

                        displayKey.setTextColor(Color.parseColor("#414141"));

                        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        rlp.leftMargin = OperatorUtil.dp2px(SearchBillActivity.this, 70);
                        displayKey.setLayoutParams(rlp);

                        rl.addView(displayName);
                        rl.addView(displayKey);

                        container.addView(rl);
                    }
                } else {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, OperatorUtil.dp2px(SearchBillActivity.this, 40));
                    lp.addRule(RelativeLayout.BELOW, R.id.id_title);
                    lp.topMargin = OperatorUtil.dp2px(SearchBillActivity.this, 8);
                    container.setLayoutParams(lp);
                    addBillType(container, bill);
                }
        }
    });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bill bill = (Bill) listView.getItemAtPosition(position);
                Intent intent = new Intent(SearchBillActivity.this, BillInspectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bill", bill);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        Log.d("response","onresume");
        Log.d("response", datalist.size() + " ");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d("response","destroy");
        super.onDestroy();
    }

    private void addBillType(LinearLayout container,Bill bill) {
        RelativeLayout rll = new RelativeLayout(SearchBillActivity.this);
        TextView billType = new TextView(SearchBillActivity.this);
        String billType_text = "单据类型:";
        String value = bill.getBillTypeChina();

        billType.setMaxEms(5);
        billType.setText(billType_text);
        billType.setTextColor(Color.parseColor("#88444444"));
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        billType.setLayoutParams(lp1);

        TextView billValue = new TextView(SearchBillActivity.this);
        billValue.setText(value);
        billValue.setTextColor(Color.parseColor("#414141"));
        RelativeLayout.LayoutParams rlp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlp1.leftMargin = OperatorUtil.dp2px(SearchBillActivity.this, 70);
        billValue.setLayoutParams(rlp1);

        rll.addView(billType);
        rll.addView(billValue);
        container.addView(rll);
    }
}

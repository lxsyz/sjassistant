package com.example.administrator.sjassistant.activity;

import android.content.Intent;
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
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Bill;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/5.
 */
public class UnfinishedWorkActivity extends BaseActivity implements View.OnClickListener {

    private ImageView search,delete;
    private EditText ed_name;

    private ListView lv;

    private List<Bill> datalist = new ArrayList<Bill>();

    private TextView read_flag;

    private CommonAdapter commonAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();

        setCenterView(R.layout.activity_unfinished_work);
        setTopText("待办工作");

        search = (ImageView)findViewById(R.id.search);
        ed_name = (EditText)findViewById(R.id.search_content);
        delete = (ImageView)findViewById(R.id.delete_word);
        search.setOnClickListener(this);
        delete.setOnClickListener(this);

        lv = (ListView)findViewById(R.id.unfinished_list);
        read_flag = (TextView)findViewById(R.id.read_flag);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bill bill = (Bill)lv.getItemAtPosition(position);
                Intent intent = new Intent(UnfinishedWorkActivity.this,BillInspectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bill",bill);
                intent.putExtras(bundle);
                startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();

        getUnfinishedWork();


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
     * 获取待办
     */
    private void getUnfinishedWork() {
        datalist.clear();

        String url = Constant.SERVER_URL + "bill/show";

        OkHttpUtils.post()
                .url(url)
                .addParams("userCode", Constant.username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(UnfinishedWorkActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response+" ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            JSONObject data = object.getJSONObject("data");
                            JSONArray list = data.getJSONArray("list");
                            if (statusCode == 0) {
                                int len = list.length();
                                if (len != 0 ) {
                                    for (int i = 0;i < len;i++) {
                                        Bill bill = new Bill();
                                        JSONObject o = list.getJSONObject(i);
                                        bill.setId(o.getInt("id"));
                                        bill.setBillId(o.getInt("billId"));
                                        bill.setBillType(o.getString("billType"));
                                        bill.setUserCode(o.getString("userCode"));
                                        bill.setDealTime(o.getString("dealTime"));
                                        bill.setDealResult(o.getString("dealResult"));
                                        datalist.add(bill);
                                    }
                                }
                                commonAdapter = new CommonAdapter<Bill>(UnfinishedWorkActivity.this, datalist, R.layout.item_a) {
                                    @Override
                                    public void convert(ViewHolder holder, Bill bill) {
                                        String title = bill.getUserCode() + "的单据需要你审批";
                                        holder.setText(R.id.id_title,title);
                                        holder.setText(R.id.id_type_value,bill.getBillType());
                                        holder.setText(R.id.id_time_value,bill.getDealTime());
                                        holder.setText(R.id.read_flag,bill.getDealResult());

                                        if (TextUtils.isEmpty(bill.getDealResult()) || bill.getDealResult().equals("null")){
                                            holder.setText(R.id.read_flag,"未读");
                                            ((TextView)holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.unread));
                                        } else if (bill.getDealResult().equals("未读") || bill.getDealResult().equals("退回未提交")) {
                                            ((TextView)holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.unread));
                                        } else if (bill.getDealResult().equals("通过未提交") || bill.getDealResult().equals("通过")) {
                                            ((TextView)holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.read));
                                        }
                                    }
                                };
                                lv.setAdapter(commonAdapter);
                            } else {
                                ToastUtil.showShort(UnfinishedWorkActivity.this,"服务器异常");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



    }

    /*
     * 搜索待办
     */
    private void filterData(String text) {
        List<Bill> filterList = new ArrayList<Bill>();

        if (TextUtils.isEmpty(text)) {
            filterList = datalist;
        } else {
            for (Bill b : datalist) {
                String dealTime = b.getDealTime();
                String billType = b.getBillType();
                String userCode = b.getUserCode();
                String dealResult = b.getDealResult();
                if (dealTime.contains(text) ||
                        billType.contains(text) ||
                        userCode.contains(text) ||
                        dealResult.contains(text)) {
                    filterList.add(b);
                }
            }
        }

        commonAdapter.updateListView(filterList);
    }

}

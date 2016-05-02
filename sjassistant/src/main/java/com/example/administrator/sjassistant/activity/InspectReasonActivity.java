package com.example.administrator.sjassistant.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.InspectPerson;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/5.
 */
public class InspectReasonActivity extends BaseActivity implements View.OnClickListener {

    private List<InspectPerson> datalist = new ArrayList<InspectPerson>();

    private ListView list;
    private EditText inspectReason;
    private Button button;

    private CommonAdapter<InspectPerson> commonAdapter;

    private List<String> resultList = new ArrayList<String>();

    private int billId;

    private LinearLayout prompt_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("activity",savedInstanceState+" ");
        if (savedInstanceState != null) {
            billId = savedInstanceState.getInt("billId");
            datalist = (List<InspectPerson>) savedInstanceState.getSerializable("inspectPersons");
        } else {
            billId = getIntent().getIntExtra("billId", -1);
            datalist = (ArrayList<InspectPerson>) getIntent().getSerializableExtra("inspectPersons");
        }

        if (datalist == null) {
            datalist = new ArrayList<>();
        }
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_inspect_reason);

        setTopText("审批理由");

        inspectReason = (EditText)findViewById(R.id.inspect_reason);
        list = (ListView)findViewById(R.id.inspect_list);
        button = (Button)findViewById(R.id.confirm);
        prompt_layout = (LinearLayout)findViewById(R.id.prompt_layout);


        button.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        datalist.clear();
        if (datalist.size() != 0) {
            commonAdapter = new CommonAdapter<InspectPerson>(InspectReasonActivity.this, datalist, R.layout.item_inspect_reason) {
                @Override
                public void convert(ViewHolder holder, InspectPerson inspectPerson) {
                    holder.setText(R.id.user_name, inspectPerson.getUsername());

                    if (inspectPerson.isCheckState()) {
                        holder.setImageResource(R.id.iv_checked, R.drawable.radio_checked);
                    } else {
                        holder.setImageResource(R.id.iv_checked, R.drawable.radio_unchecked);
                    }
                }
            };
            list.setAdapter(commonAdapter);
        } else {
            list.setVisibility(View.INVISIBLE);
            prompt_layout.setVisibility(View.INVISIBLE);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView iv = (ImageView) view.findViewById(R.id.iv_checked);
                InspectPerson inspectPerson = datalist.get(position);

                if (inspectPerson.isCheckState()) {
                    inspectPerson.setCheckState(false);
                    iv.setImageResource(R.drawable.radio_unchecked);
                    resultList.remove(inspectPerson.getUsername());
                    commonAdapter.notifyDataSetChanged();
                } else {
                    inspectPerson.setCheckState(true);
                    iv.setImageResource(R.drawable.radio_checked);
                    resultList.add(inspectPerson.getUsername());
                    commonAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                postReason();
                break;
        }
    }

    private void postReason() {
        String url = Constant.SERVER_URL + "bill/commitBill";

        OkHttpUtils.post()
                .url(url)
                .addParams("billId", String.valueOf(billId))
                .addParams("approver",getApprover())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error",e.getMessage());
                        ErrorUtil.NetWorkToast(InspectReasonActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);
                        InspectReasonActivity.this.finish();
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("activity","saveInstance");
        outState.putInt("billId",billId);
        outState.putSerializable("inspectPersons",(ArrayList<InspectPerson>)datalist);

    }

    private String getApprover() {
        StringBuilder sb = new StringBuilder();

        if (resultList.size() > 0) {
            boolean need = false;

            for (String s : resultList) {
                if (need) {
                    sb.append(",");
                }
                sb.append(s);
                need = true;
            }
        }
        return sb.toString();
    }
}

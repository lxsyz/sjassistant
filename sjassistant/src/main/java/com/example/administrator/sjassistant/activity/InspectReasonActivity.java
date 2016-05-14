package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.FilterCondition;
import com.example.administrator.sjassistant.bean.InspectPerson;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.w3c.dom.Text;

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

    /*
     * 选中的下一环节审批人的名字
     */
    private List<String> resultList = new ArrayList<String>();

    private int billId;
    private String billType;
    private LinearLayout prompt_layout;
    private RelativeLayout next_name_layout,next_role_layout;
    private TextView next_role_text,next_name_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("activity",savedInstanceState+" ");
        if (savedInstanceState != null) {
            billId = savedInstanceState.getInt("billId");
            datalist = (List<InspectPerson>) savedInstanceState.getSerializable("inspectPersons");
            billType = savedInstanceState.getString("billType");
        } else {
            billId = getIntent().getIntExtra("billId", -1);
            datalist = (ArrayList<InspectPerson>) getIntent().getSerializableExtra("inspectPersons");
            billType = getIntent().getStringExtra("billType");
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

        next_name_layout = (RelativeLayout)findViewById(R.id.next_name_layout);
        next_role_layout = (RelativeLayout)findViewById(R.id.next_role_layout);

        next_name_text = (TextView)findViewById(R.id.next_name_text);
        next_role_text = (TextView)findViewById(R.id.next_role_text);

        next_name_layout.setOnClickListener(this);
        next_role_layout.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        Intent intent = null;
        switch (v.getId()) {
            case R.id.confirm:
                postReason();
                break;
            case R.id.next_name_layout:
                intent = new Intent(InspectReasonActivity.this,EditActivity.class);
                intent.putExtra("top","选择下一环节名称").putExtra("result",4).
                        putExtra("billType",billType)
                        .putExtra("billId",billId);
                startActivityForResult(intent, 4);
                break;
            case R.id.next_role_layout:
                intent = new Intent(InspectReasonActivity.this,EditActivity.class);
                intent.putExtra("top","选择下一环节角色").putExtra("result",5);
                startActivityForResult(intent,4);
                break;
        }
    }

    private void postReason() {
        String url = Constant.SERVER_URL + "bill/commitBill";
        Log.d("approver",getApprover()+" ");
        OkHttpUtils.post()
                .url(url)
                .addParams("billId", String.valueOf(billId))
                .addParams("dealOpinion",inspectReason.getText().toString())
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
                        ToastUtil.showShort(InspectReasonActivity.this,"提交成功");
                        InspectReasonActivity.this.finish();
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("activity", "saveInstance");
        outState.putInt("billId", billId);
        outState.putSerializable("inspectPersons", (ArrayList<InspectPerson>) datalist);
        outState.putString("billType",billType);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FilterCondition fc = null;
        switch (resultCode) {
            case 4:
                fc = (FilterCondition)data.getSerializableExtra("result");
                next_name_text.setText(fc.getName());
                break;
            case 5:
                fc = (FilterCondition)data.getSerializableExtra("result");
                next_role_text.setText(fc.getName());
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

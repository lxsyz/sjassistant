package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.FilterCondition;
import com.example.administrator.sjassistant.bean.InspectPerson;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.CircleImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

    private String showStep;
    private String showRole;
    private LinearLayout prompt_layout;
    private RelativeLayout next_name_layout,next_role_layout;
    private TextView next_role_text,next_name_text;

    //下一环节code
    private String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            billId = savedInstanceState.getInt("billId");
            datalist = (List<InspectPerson>) savedInstanceState.getSerializable("inspectPersons");
            showRole = savedInstanceState.getString("showRole");
            showStep = savedInstanceState.getString("showStep");
            billType = savedInstanceState.getString("billType");
        } else {
            billId = getIntent().getIntExtra("billId", -1);
            datalist = (ArrayList<InspectPerson>) getIntent().getSerializableExtra("inspectPersons");
            showRole = getIntent().getStringExtra("showRole");
            showStep = getIntent().getStringExtra("showStep");
            billType = getIntent().getStringExtra("billType");
        }

        Log.d("response",showRole+" ");

        if (showRole.equals("0")) {
            next_role_layout.setVisibility(View.VISIBLE);
        } else {
            next_role_layout.setVisibility(View.GONE);
        }

        if (showStep.equals("0")) {
            next_name_layout.setVisibility(View.VISIBLE);
        } else {
            next_name_layout.setVisibility(View.GONE);
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

        showAuditor();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView iv = (ImageView) view.findViewById(R.id.iv_checked);
                InspectPerson inspectPerson = datalist.get(position);

                if (inspectPerson.isCheckState()) {
                    inspectPerson.setCheckState(false);
                    iv.setImageResource(R.drawable.radio_unchecked);
                    resultList.remove(inspectPerson.getUserCode());
                    commonAdapter.notifyDataSetChanged();
                } else {
                    inspectPerson.setCheckState(true);
                    iv.setImageResource(R.drawable.radio_checked);
                    resultList.add(inspectPerson.getUserCode());
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

                if (next_name_layout.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(next_name_text.getText())) {
                        ToastUtil.showShort(this, "请选择下一环节名称");
                        return;
                    }
                }
                if (next_role_layout.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(next_role_text.getText())) {
                        ToastUtil.showShort(this, "请选择下一环节角色");
                        return;
                    }
                }

                if (datalist.size() != 0) {
                    if (resultList.size() == 0) {
                        ToastUtil.showShort(this, "请选择下一环节审批人");
                        return;
                    }
                }
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
                intent.putExtra("top","选择下一环节角色").putExtra("result",5).putExtra("billType",billType)
                        .putExtra("billId",billId);
                startActivityForResult(intent,4);
                break;
        }
    }

    /*
     * 第一次进入时获取审核人
     */
    private void showAuditor() {
        if (datalist.size() != 0) {


            if (commonAdapter != null) {
                commonAdapter.updateListView(datalist);
            } else {
                commonAdapter = new CommonAdapter<InspectPerson>(InspectReasonActivity.this, datalist, R.layout.item_inspect_reason) {
                    @Override
                    public void convert(ViewHolder holder, InspectPerson inspectPerson) {
                        if (inspectPerson.getUserCode() != null) {
                            String url = Constant.SERVER_URL + "images/" + inspectPerson.getUserCode() + ".jpg";

                            CircleImageView c = holder.getView(R.id.user_photo);
                            Glide.with(InspectReasonActivity.this).load(url)
                                    .error(R.drawable.customer_de)
                                    .into(c);
                        }
                        holder.setText(R.id.user_name, inspectPerson.getUsername());

                        if (inspectPerson.isCheckState()) {
                            holder.setImageResource(R.id.iv_checked, R.drawable.radio_checked);
                        } else {
                            holder.setImageResource(R.id.iv_checked, R.drawable.radio_unchecked);
                        }
                    }
                };
                list.setAdapter(commonAdapter);
            }
        }
        else {
//            next_name_layout.setVisibility(View.GONE);
//            next_role_layout.setVisibility(View.GONE);
            list.setVisibility(View.INVISIBLE);
            prompt_layout.setVisibility(View.INVISIBLE);
        }


//        String url = Constant.SERVER_URL + "bill/showAuditor";
//
//        OkHttpUtils.post()
//                .url(url)
//                .addParams("billId", String.valueOf(billId))
//                .addParams("billType",billType)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        Log.d("error",e.getMessage());
//                        ErrorUtil.NetWorkToast(InspectReasonActivity.this);
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("response", response);
//                        try {
//                            JSONObject object = new JSONObject(response);
//                            int statusCode = object.optInt("statusCode");
//
//                            Gson gson = new Gson();
//                            if (statusCode == 0) {
//                                JSONObject data = object.optJSONObject("data");
//                                JSONArray list2 = data.optJSONArray("listu");
//                                if (list2 != null && list2.length() != 0) {
//
//
//                                    datalist = gson.fromJson(list2.toString(), new TypeToken<List<InspectPerson>>() {
//                                    }.getType());
//
//                                    if (datalist.size() != 0) {
//                                        commonAdapter = new CommonAdapter<InspectPerson>(InspectReasonActivity.this, datalist, R.layout.item_inspect_reason) {
//                                            @Override
//                                            public void convert(ViewHolder holder, InspectPerson inspectPerson) {
//                                                if (inspectPerson.getUserCode()!=null) {
//                                                    String url = Constant.SERVER_URL + "images/" + inspectPerson.getUserCode() + ".jpg";
//
//                                                    CircleImageView c = holder.getView(R.id.user_photo);
//                                                    Glide.with(InspectReasonActivity.this).load(url)
//                                                            .error(R.drawable.customer_de)
//                                                            .into(c);
//                                                }
//                                                holder.setText(R.id.user_name, inspectPerson.getUsername());
//
//                                                if (inspectPerson.isCheckState()) {
//                                                    holder.setImageResource(R.id.iv_checked, R.drawable.radio_checked);
//                                                } else {
//                                                    holder.setImageResource(R.id.iv_checked, R.drawable.radio_unchecked);
//                                                }
//                                            }
//                                        };
//                                        list.setAdapter(commonAdapter);
//                                    } else {
//                                        next_name_layout.setVisibility(View.GONE);
//                                        next_role_layout.setVisibility(View.GONE);
//                                        list.setVisibility(View.INVISIBLE);
//                                        prompt_layout.setVisibility(View.INVISIBLE);
//                                    }
//                                }
//                            } else {
//                                ToastUtil.showShort(InspectReasonActivity.this, "服务器异常");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
    }

    /*
     * 提交审批理由
     */
    private void postReason() {
        String url = Constant.SERVER_URL + "bill/commitBill";

        if (code == null) {
            code = "";
        }
        OkHttpUtils.post()
                .url(url)
                .addParams("billId", String.valueOf(billId))
                .addParams("dealOpinion",inspectReason.getText().toString())
                .addParams("approver",getApprover())
                .addParams("billType",billType)
                .addParams("nextStep",code)
                .addParams("dealPerson",Constant.username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage());
                        ErrorUtil.NetWorkToast(InspectReasonActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        ToastUtil.showShort(InspectReasonActivity.this, "提交成功");
                        Intent intent = new Intent(InspectReasonActivity.this, UnfinishedWorkActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("activity", "saveInstance");
        outState.putInt("billId", billId);
        outState.putSerializable("inspectPersons", (ArrayList<InspectPerson>) datalist);
        outState.putString("billType", billType);
        outState.putString("showRole",showRole);
        outState.putString("showStep",showStep);
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
                code = fc.getCode();
                break;
            case 5:
                fc = (FilterCondition)data.getSerializableExtra("result");
                next_role_text.setText(fc.getName());

                if (!TextUtils.isEmpty(fc.getName())) {
                    showRolesPerson(fc.getName());
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * 显示角色人员
     */
    private void showRolesPerson(String name) {
        datalist.clear();
        String url = Constant.SERVER_URL + "bill/showRolesPerson";
        Log.d("approver",getApprover()+" ");
        OkHttpUtils.post()
                .url(url)
                .addParams("zw", name)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error",e.getMessage());
                        ErrorUtil.NetWorkToast(InspectReasonActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.optInt("statusCode");

                            Gson gson = new Gson();
                            if (statusCode == 0) {
                                JSONObject data = object.optJSONObject("data");
                                JSONArray list2 = data.optJSONArray("list");
                                if (list2 != null && list2.length() !=0) {


                                    datalist = gson.fromJson(list2.toString(), new TypeToken<List<InspectPerson>>() {
                                    }.getType());

                                    if (datalist.size() != 0) {
                                        commonAdapter = new CommonAdapter<InspectPerson>(InspectReasonActivity.this, datalist, R.layout.item_inspect_reason) {
                                            @Override
                                            public void convert(ViewHolder holder, InspectPerson inspectPerson) {
                                                String url = Constant.SERVER_URL + "images/" + inspectPerson.getUserCode()+".jpg";

                                                CircleImageView c = holder.getView(R.id.user_photo);
                                                Glide.with(InspectReasonActivity.this).load(url)
                                                        .skipMemoryCache(true)
                                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                        .error(R.drawable.customer_de)
                                                        .into(c);
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
                                        next_name_layout.setVisibility(View.GONE);
                                        next_role_layout.setVisibility(View.GONE);
                                        list.setVisibility(View.INVISIBLE);
                                        prompt_layout.setVisibility(View.INVISIBLE);
                                    }
                                }
                            } else {
                                ToastUtil.showShort(InspectReasonActivity.this, "服务器异常");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
    }
}

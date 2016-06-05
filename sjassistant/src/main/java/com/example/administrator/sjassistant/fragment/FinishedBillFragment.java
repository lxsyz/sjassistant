package com.example.administrator.sjassistant.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.activity.BillInspectActivity;
import com.example.administrator.sjassistant.activity.UnfinishedWorkActivity;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Bill;
import com.example.administrator.sjassistant.bean.BillDetail;
import com.example.administrator.sjassistant.bean.ListLog;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/6.
 */
public class FinishedBillFragment extends Fragment {

    private ListView list;
    private TextView tv;

    private List<Bill> datalist = new ArrayList<Bill>();

    public FinishedBillFragment(){}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("headline", "attach");
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            finishDataListener = (FinishDataListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_finished_bill,null);
        list = (ListView)v.findViewById(R.id.finished_list);
        tv = (TextView)v.findViewById(R.id.prompt);

//        datalist = (ArrayList<Bill>)getArguments().getSerializable("data");


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bill bill = (Bill) list.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), BillInspectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bill", bill);
                intent.putExtras(bundle);
                intent.putExtra("from",1);
                startActivity(intent);
            }
        });

        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }



    private void getFinished() {
        //datalist.clear();
        String url = Constant.SERVER_URL + "bill/showFinish";

        OkHttpUtils.post()
                .url(url)
                .addParams("userCode", Constant.username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(getActivity());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response + " ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            JSONObject data = object.getJSONObject("data");
                            JSONArray lis = data.getJSONArray("list");
                            if (statusCode == 0) {
                                Gson gson = new Gson();
                                int len = lis.length();
                                if (len != 0) {
                                    List<Bill> finishedList = new ArrayList<>();
                                    finishedList = gson.fromJson(lis.toString(), new TypeToken<List<Bill>>() {
                                    }.getType());


//                                    for (int i = 0; i < len; i++) {
//                                        List<BillDetail> listbf = new ArrayList<>();
//                                        listbf =
//                                        Bill bill = new Bill();
//                                        bill.setDealResult(lis.optJSONObject(i).optString("dealResult"));
//                                        bill.setDeal(true);
//                                        bill.setUserCode(lis.optJSONObject(i).optString("userCode"));
//                                        bill.setBillTypeChina(lis.optJSONObject(i).optString("billTypeChina"));
//                                        bill.setBillType(lis.optJSONObject(i).optString("billType"));
//                                        bill.setBillId(lis.optJSONObject(i).optInt("billId"));
//                                        bill.setDealPerson(lis.optJSONObject(i).optString("dealPerson"));
//                                        bill.setDealOpinion(lis.optJSONObject(i).optString("dealOpnion"));
//                                        bill.setListbf(lis.optJSONObject(i).opt("listbf"));
//                                    }

                                    if (finishedList.size() == 0) {
                                        list.setVisibility(View.GONE);
                                        tv.setVisibility(View.VISIBLE);
                                        //TextView textView = (TextView)v.findViewById(R.id.prompt);
                                        //textView.setVisibility(View.VISIBLE);
                                    } else {
                                        //设置状态为已审批
                                        for (Bill bill:finishedList) {
                                            bill.setDeal(true);
                                        }

                                        finishDataListener.FinishBill(finishedList);

                                        list.setAdapter(new CommonAdapter<Bill>(getActivity(), finishedList, R.layout.item_a) {
                                            @Override
                                            public void convert(ViewHolder holder, Bill bill) {
                                                String title = bill.getUserCode() + "的单据需要你审批";
                                                List<BillDetail> billDetails = bill.getListbf();

                                                holder.setText(R.id.id_title, title);
                                                //holder.setText(R.id.id_type_value, bill.getBillTypeChina());
                                                //holder.setText(R.id.id_time_value, bill.getDealTime());
                                                //holder.setText(R.id.read_flag, bill.getDealResult());
                                                holder.getView(R.id.read_flag).setVisibility(View.INVISIBLE);
                                                holder.getView(R.id.iv_inspect).setVisibility(View.VISIBLE);
                                                //待办中的不定条目部分
                                                LinearLayout container = holder.getView(R.id.container);
                                                container.removeAllViews();
                                                if (billDetails != null) {
                                                    int len = billDetails.size();

                                                    RelativeLayout rll = new RelativeLayout(getActivity());
                                                    TextView billType = new TextView(getActivity());
                                                    String billType_text = "单据类型:";
                                                    String value = bill.getBillTypeChina();

                                                    billType.setMaxEms(5);
                                                    billType.setText(billType_text);
                                                    billType.setTextColor(Color.parseColor("#88444444"));
                                                    RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                    lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                                    billType.setLayoutParams(lp1);

                                                    TextView billValue = new TextView(getActivity());
                                                    billValue.setText(value);
                                                    billValue.setTextColor(Color.parseColor("#414141"));
                                                    RelativeLayout.LayoutParams rlp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                    rlp1.leftMargin = OperatorUtil.dp2px(getActivity(), 70);
                                                    billValue.setLayoutParams(rlp1);

                                                    rll.addView(billType);
                                                    rll.addView(billValue);
                                                    container.addView(rll);
                                                    for (int i = 0; i < len; i++) {
                                                        RelativeLayout rl = new RelativeLayout(getActivity());
                                                        TextView displayName = new TextView(getActivity());
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

                                                        TextView displayKey = new TextView(getActivity());


                                                        if (!TextUtils.isEmpty(displayKey_text)) {
                                                            displayKey_text += ":";
                                                            displayKey.setText(displayKey_text);
                                                        } else {
                                                            displayKey.setText("");
                                                        }

                                                        displayKey.setTextColor(Color.parseColor("#414141"));

                                                        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                        rlp.leftMargin = OperatorUtil.dp2px(getActivity(), 70);
                                                        displayKey.setLayoutParams(rlp);

                                                        rl.addView(displayName);
                                                        rl.addView(displayKey);

                                                        container.addView(rl);
                                                    }
                                                } else {
                                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, OperatorUtil.dp2px(getActivity(), 40));
                                                    lp.addRule(RelativeLayout.BELOW, R.id.id_title);
                                                    lp.topMargin = OperatorUtil.dp2px(getActivity(), 8);
                                                    container.setLayoutParams(lp);
                                                    addBillType(container, bill);
                                                }

                                                //holder.setText(R.id.id_type_value, bill.getBillType());
                                                //holder.setText(R.id.id_time_value, bill.getDealTime());
//                                                holder.setText(R.id.read_flag, bill.getDealResult());

//                                                if (TextUtils.isEmpty(bill.getDealResult()) || bill.getDealResult().equals("null")) {
//                                                    holder.setText(R.id.read_flag, "未读");
//                                                    ((TextView) holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.unread));
//                                                } else if (bill.getDealResult().equals("未读") || bill.getDealResult().equals("退回未提交")) {
//                                                    ((TextView) holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.unread));
//                                                } else if (bill.getDealResult().equals("通过未提交") || bill.getDealResult().equals("已读")) {
//                                                    ((TextView) holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.read));
//                                                }
                                            }
                                        });
                                    }

//                                    finishedBillFragment = new FinishedBillFragment();
                                    //unfinishedBillFragment = new UnfinishedBillFragment();
//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable("data", (ArrayList) unfinishedList);
//                                    unfinishedBillFragment.setArguments(bundle);


//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable("data",(ArrayList) finishedList);
//                                    finishedBillFragment.setArguments(bundle);
                                } else {
                                    list.setVisibility(View.GONE);
                                    tv.setVisibility(View.VISIBLE);
                                }

                            } else {
                                ToastUtil.showShort(getActivity(), "服务器异常");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getFinished();
    }

    private FinishDataListener finishDataListener;

    private void addBillType(LinearLayout container,Bill bill) {
        RelativeLayout rll = new RelativeLayout(getActivity());
        TextView billType = new TextView(getActivity());
        String billType_text = "单据类型:";
        String value = bill.getBillTypeChina();

        billType.setMaxEms(5);
        billType.setText(billType_text);
        billType.setTextColor(Color.parseColor("#88444444"));
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        billType.setLayoutParams(lp1);

        TextView billValue = new TextView(getActivity());
        billValue.setText(value);
        billValue.setTextColor(Color.parseColor("#414141"));
        RelativeLayout.LayoutParams rlp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlp1.leftMargin = OperatorUtil.dp2px(getActivity(), 70);
        billValue.setLayoutParams(rlp1);

        rll.addView(billType);
        rll.addView(billValue);
        container.addView(rll);
    }

    public interface FinishDataListener {
        void FinishBill(List<Bill> list);
    }
}

package com.example.administrator.sjassistant.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.administrator.sjassistant.activity.InspectActivity;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Bill;
import com.example.administrator.sjassistant.bean.BillDetail;
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
public class UnfinishedBillFragment extends Fragment {

    private ListView lv;
    private TextView prompt;

    private List<Bill> datalist = new ArrayList<Bill>();

    private CommonAdapter<Bill> commonAdapter;

    public UnfinishedBillFragment(){}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("headline", "attach");
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mDataListener = (DataListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_unfinished_bill, null);

        lv = (ListView) v.findViewById(R.id.unfinished_list);
        prompt = (TextView)v.findViewById(R.id.prompt);
        //Bundle bundle = getArguments();

        //datalist = (ArrayList<Bill>)bundle.getSerializable("data");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bill bill = (Bill) lv.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), BillInspectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bill", bill);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    @Override
    public void onResume() {
        super.onResume();
        getUnfinishedWork();

    }

    @Override
    public void onPause() {
        super.onPause();
        //datalist.clear();
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
                        ErrorUtil.NetWorkToast(getActivity());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response + " ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            JSONObject data = object.getJSONObject("data");
                            JSONArray list = data.getJSONArray("list");
                            if (statusCode == 0) {
                                int len = list.length();
                                Gson gson = new Gson();

                                if (len != 0) {
                                    datalist = gson.fromJson(list.toString(), new TypeToken<List<Bill>>() {
                                    }.getType());

//                                    for (int i = 0; i < len; i++) {
//
//                                        Bill bill = new Bill();
//                                        JSONObject o = list.getJSONObject(i);
//                                        bill.setId(o.getInt("id"));
//                                        bill.setBillId(o.getInt("billId"));
//                                        bill.setBillType(o.getString("billType"));
//                                        bill.setUserCode(o.getString("userCode"));
//                                        bill.setDealTime(o.getString("dealTime"));
//                                        bill.setDealResult(o.getString("dealResult"));
//                                        datalist.add(bill);
//                                    }


                                    mDataListener.onDataFinish(datalist);

                                    List<Bill> temp = new ArrayList<>();

                                    if (commonAdapter != null) {
                                        temp.addAll(datalist);
                                        commonAdapter.updateListView(temp);
                                    } else {
                                        temp.addAll(datalist);
                                        commonAdapter = new CommonAdapter<Bill>(getActivity(), temp, R.layout.item_a) {
                                            @Override
                                            public void convert(ViewHolder holder, Bill bill) {
                                                String title = bill.getUserCode() + "的单据需要你审批";

                                                List<BillDetail> billDetails = bill.getListbf();

                                                holder.setText(R.id.id_title, title);
                                                //holder.setText(R.id.id_type_value, bill.getBillTypeChina());
                                                //holder.setText(R.id.id_time_value, bill.getDealTime());
                                                holder.setText(R.id.read_flag, bill.getDealResult());
                                                holder.getView(R.id.iv_inspect).setVisibility(View.GONE);
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
                                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, OperatorUtil.dp2px(getActivity(), 70));
                                                    container.setLayoutParams(lp);
                                                    addBillType(container,bill);
                                                }
                                            }
                                        };

                                        lv.setAdapter(commonAdapter);
                                    }
                                } else {
                                    lv.setVisibility(View.GONE);
                                    prompt.setVisibility(View.VISIBLE);
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

    private DataListener mDataListener;

    public void setmDataListener(DataListener mDataListener) {
        this.mDataListener = mDataListener;
    }

    public DataListener getmDataListener() {
        return mDataListener;
    }

    public interface DataListener {
        void onDataFinish(List<Bill> list);
    }
}

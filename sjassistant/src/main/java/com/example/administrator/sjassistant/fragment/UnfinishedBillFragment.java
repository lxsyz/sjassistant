package com.example.administrator.sjassistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.activity.BillInspectActivity;
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

    private CommonAdapter commonAdapter;

    public UnfinishedBillFragment(){}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_unfinished_bill, null);

        lv = (ListView) v.findViewById(R.id.unfinished_list);
        prompt = (TextView)v.findViewById(R.id.prompt);

        //Bundle bundle = getArguments();

        //datalist = (ArrayList<Bill>)bundle.getSerializable("data");
//        if (datalist != null) {
//            commonAdapter = new CommonAdapter<Bill>(getActivity(), datalist, R.layout.item_a) {
//                @Override
//                public void convert(ViewHolder holder, Bill bill) {
//                    String title = bill.getUserCode() + "的单据需要你审批";
//                    holder.setText(R.id.id_title, title);
//                    holder.setText(R.id.id_type_value, bill.getBillType());
//                    holder.setText(R.id.id_time_value, bill.getDealTime());
//                    holder.getView(R.id.read_flag).setVisibility(View.GONE);
//                    //holder.setText(R.id.read_flag, bill.getDealResult());
//
////                    if (TextUtils.isEmpty(bill.getDealResult()) || bill.getDealResult().equals("null")) {
////                        holder.setText(R.id.read_flag, "未读");
////                        ((TextView) holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.unread));
////                    } else if (bill.getDealResult().equals("未读") || bill.getDealResult().equals("退回未提交")) {
////                        ((TextView) holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.unread));
////                    } else if (bill.getDealResult().equals("通过未提交") || bill.getDealResult().equals("已读")) {
////                        ((TextView) holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.read));
////                    }
//                }
//            };
//            lv.setAdapter(commonAdapter);
//        } else {
//            lv.setVisibility(View.GONE);
//            prompt.setVisibility(View.VISIBLE);
//        }
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
        getUnfinishedWork();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (datalist.size() != 0) {

        } else {
            //getUnfinishedWork();
        }
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
                                if (len != 0) {
                                    for (int i = 0; i < len; i++) {
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

                                    if (commonAdapter != null) {
                                        commonAdapter.updateListView(datalist);
                                    } else {
                                        commonAdapter = new CommonAdapter<Bill>(getActivity(), datalist, R.layout.item_a) {
                                            @Override
                                            public void convert(ViewHolder holder, Bill bill) {
                                                String title = bill.getUserCode() + "的单据需要你审批";
                                                holder.setText(R.id.id_title, title);
                                                holder.setText(R.id.id_type_value, bill.getBillType());
                                                holder.setText(R.id.id_time_value, bill.getDealTime());
//                                                holder.setText(R.id.read_flag, bill.getDealResult());
                                                holder.getView(R.id.read_flag).setVisibility(View.GONE);
//                                                if (TextUtils.isEmpty(bill.getDealResult()) || bill.getDealResult().equals("null")) {
//                                                    holder.setText(R.id.read_flag, "未读");
//                                                    ((TextView) holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.unread));
//                                                } else if (bill.getDealResult().equals("未读") || bill.getDealResult().equals("退回未提交")) {
//                                                    ((TextView) holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.unread));
//                                                } else if (bill.getDealResult().equals("通过未提交") || bill.getDealResult().equals("通过")) {
//                                                    ((TextView) holder.getView(R.id.read_flag)).setTextColor(getResources().getColor(R.color.read));
//                                                }
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
}

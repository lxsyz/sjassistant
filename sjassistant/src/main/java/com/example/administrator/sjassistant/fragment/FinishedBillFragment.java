package com.example.administrator.sjassistant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Bill;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_finished_bill,null);
        list = (ListView)v.findViewById(R.id.finished_list);
        tv = (TextView)v.findViewById(R.id.prompt);
        //datalist = (ArrayList<Bill>)getArguments().getSerializable("data");



        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getFinished();
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
                                    List<Bill> finishedList = new ArrayList<Bill>();
                                    finishedList = gson.fromJson(lis.toString(), new TypeToken<List<Bill>>() {
                                    }.getType());
                                    //List<Bill> unfinishedList = new ArrayList<Bill>();
                                    //datalist.addAll(finishedList);

                                    Log.d("response",finishedList.size()+" haha");

                                    if (finishedList == null || finishedList.size() == 0) {
                                        list.setVisibility(View.GONE);
                                        tv.setVisibility(View.VISIBLE);
                                        //TextView textView = (TextView)v.findViewById(R.id.prompt);
                                        //textView.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        list.setAdapter(new CommonAdapter<Bill>(getActivity(), finishedList, R.layout.item_finished_bill) {
                                            @Override
                                            public void convert(ViewHolder holder, Bill bill) {
                                                String title = bill.getUserCode() + "的单据需要你审批";
                                                holder.setText(R.id.id_title, title);
                                                holder.setText(R.id.id_type_value, bill.getBillType());
                                                holder.setText(R.id.id_time_value, bill.getDealTime());
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
}

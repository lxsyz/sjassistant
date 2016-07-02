package com.example.administrator.sjassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.TimeAxisAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Bill;
import com.example.administrator.sjassistant.bean.BillDetailList;
import com.example.administrator.sjassistant.bean.InspectPerson;
import com.example.administrator.sjassistant.bean.ListLog;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.AddContactsWin;
import com.example.administrator.sjassistant.view.ChooseShareWindow;
import com.example.administrator.sjassistant.view.CircleImageView;
import com.example.administrator.sjassistant.view.ConfirmPopWin;
import com.example.administrator.sjassistant.view.InspectWindow;
import com.example.administrator.sjassistant.view.MyFixList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/5.
 */
public class BillInspectActivity extends BaseActivity implements View.OnClickListener {

    private final int QQ_ITEM = 1;
    private final int WEIXIN_ITEM = 2;

    private String APP_ID = "1105323970";
    //private static final String WEIXIN_ID = "wx979b127e5ff62391";
    private static final String WEIXIN_ID = "wxf2f1cfadc50bcb0c";
    private IWXAPI api;

    private MyFixList inspect_list,bill_list;
    private RelativeLayout bill_detail_layout,pass_layout,cancel_layout;
    private LinearLayout bottom;

    private CircleImageView iv_person;
    private TextView name,apartment,time,bill_value,bill_detail,apply_person,apply_value;

    private ChooseShareWindow chooseShareWindow;
    private ConfirmPopWin confirmPopWin;
    private InspectWindow inspectWindow;
    private LinearLayout root;


    //private CommonAdapter<Bill> commonAdapter;

    private List<BillDetailList> datalist = new ArrayList<BillDetailList>();


    private List<Person> personList = new ArrayList<>();

    private Tencent mTencent;

    //bill账单
    private Bill bill;

    private String userImg;

    private TimeAxisAdapter mTimeAxisAdapter;
    private ScrollView scrollView;

    private StringBuilder shareContent = new StringBuilder();

    private String displayLevel;
    private int from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("response","create");
        bill = (Bill) getIntent().getSerializableExtra("bill");
        from = getIntent().getIntExtra("from",0);

        if (from == 1) {
            bottom.setVisibility(View.GONE);
        } else {
            bottom.setVisibility(View.VISIBLE);
        }
        if (bill != null) {
            setTopText(bill.getUserCode() + "的单据审批");
            name.setText(bill.getUsername());
            time.setText(bill.getDealTime());


        }

        popWindowInit();
        //微信API的初始化
        api = WXAPIFactory.createWXAPI(this,WEIXIN_ID,true);
//        if (!api.isWXAppInstalled()) {
//            Toast.makeText(this, "没有安装微信", Toast.LENGTH_LONG).show();
//        }
        api.registerApp(WEIXIN_ID);
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
    }



    /*
     * 设置弹出窗
     */
    private void popWindowInit() {
        if (chooseShareWindow != null) {
            chooseShareWindow.getChooseShareWindow().getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        chooseShareWindow.closeWindow();
                    }
                }
            });
        }

        if (confirmPopWin == null) {
            confirmPopWin = new ConfirmPopWin(BillInspectActivity.this, OperatorUtil.dp2px(BillInspectActivity.this,100));

            confirmPopWin.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        confirmPopWin.dismiss();
                    }
                }
            });
        }

        if (inspectWindow == null) {
            inspectWindow = new InspectWindow(BillInspectActivity.this,this, OperatorUtil.dp2px(BillInspectActivity.this, 250),
                    OperatorUtil.dp2px(BillInspectActivity.this,120));

            inspectWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (!hasFocus) {
                        inspectWindow.dismiss();
                    }
                }
            });
        }

        //inspectWindow.setItem1Text("哈哈哈");
        inspectWindow.setFocusable(true);
        confirmPopWin.setFocusable(true);
        //confirmPopWin.setContentText("孙");
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.bt_right:
                chooseShareWindow.showChooseShareWindow(root);

                chooseShareWindow.setOnItemClickListener(new ChooseShareWindow.OnItemClickListener() {
                    @Override
                    public void onItemClick(int flag) {
                        if (flag == QQ_ITEM) {
                            shareQQ(BillInspectActivity.this);
//                            Bundle params = new Bundle();
//                            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
//                            params.putString(QQShare.SHARE_TO_QQ_TITLE, "标题");
//                            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "张三 经理");
//                            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, null);
//                            //params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"");
//                            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "审计助手");
//                            mTencent.shareToQQ(BillInspectActivity.this, params, new BaseUiListener());
                        } else if (flag == WEIXIN_ITEM) {
                            share(SendMessageToWX.Req.WXSceneSession);
                        }
                    }
                });
                break;
            case R.id.bt_right2:

                intent = new Intent(BillInspectActivity.this,MoreContact.class);

                intent.putExtra("personlist",(ArrayList)personList);
                startActivity(intent);

                break;
            case R.id.pass_layout:
                inspect(true);
                break;
            case R.id.cancel_layout:
                inspect(false);
                break;
            case R.id.bill_detail_layout:
                if (datalist.size() == 0) {
                    ToastUtil.showShort(BillInspectActivity.this,"没有更多详情了");
                    return;
                } else if (!TextUtils.isEmpty(datalist.get(0).getNextType())) {
                    intent = new Intent(BillInspectActivity.this, BillDetailActivity.class);
                    intent.putExtra("billId", bill.getBillId());
                    intent.putExtra("displayLevel", displayLevel);
                    intent.putExtra("billType", bill.getBillType());
                    //有待商榷
                    intent.putExtra("fatherId", 0);
                    startActivity(intent);
                } else {
                    ToastUtil.showShort(BillInspectActivity.this,"没有更多详情了");
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (chooseShareWindow.getChooseShareWindow().isShowing()) {
            chooseShareWindow.closeWindow();
        }
        if (confirmPopWin.isShowing()) {
            confirmPopWin.dismiss();
        }
        Log.d("response", "resume");
        if (bill != null) {
            String url = Constant.SERVER_URL + "images/" + bill.getUserCode() + ".jpg";
            Glide.with(BillInspectActivity.this).load(url)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.customer_de).into(iv_person);
        }
        getBillDetail();
    }

    /*
     * 审批通过和退回
     */
    private void inspect(final boolean isPass) {
        String url = Constant.SERVER_URL + "bill/dealBill";
        Log.d("response"," "+bill.getBillId()+"  "+Constant.username+" "+bill.getBillType());
        OkHttpUtils.post()
                .url(url)
                .addParams("billId",String.valueOf(bill.getBillId()))
                .addParams("auditPerson",Constant.username)
                .addParams("ispass",String.valueOf(isPass))
                .addParams("billType",bill.getBillType())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error",e.getMessage()+" ");
                        ErrorUtil.NetWorkToast(BillInspectActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.optInt("statusCode");

                            Gson gson = new Gson();
                            if (statusCode == 0) {
                                JSONObject data = object.optJSONObject("data");
                                String showStep = data.optString("showStep");
                                String showRole = data.optString("showRole");
                                if (!isPass) {
                                    showRole = "1";
                                    showStep = "1";
                                }
                                List<InspectPerson> inspectPersons = new ArrayList<InspectPerson>();
                                JSONArray listu = data.optJSONArray("listu");
                                if (listu != null && listu.length() != 0) {
                                    inspectPersons = gson.fromJson(listu.toString(),new TypeToken<List<InspectPerson>>(){}.getType());
                                }

                                Intent intent = new Intent(BillInspectActivity.this, InspectReasonActivity.class);

                                //intent.putExtra("inspectPersons", (ArrayList) inspectPersons);
                                intent.putExtra("billType",bill.getBillType());
                                intent.putExtra("billId", bill.getBillId());
                                intent.putExtra("showStep", showStep);
                                intent.putExtra("showRole", showRole);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("inspectPersons",(ArrayList)inspectPersons);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            } else if (statusCode == 1) {

                            } else if (statusCode == 3) {
                                //弹出错误提示
                                String message = object.getString("message");
                                int[] location = new int[2];
                                pass_layout.getLocationOnScreen(location);
                                confirmPopWin.setContentText(message);
                                //((TextView)confirmPopWin.getContentView().findViewById(R.id.prompt_content)).setText("hello there");
                                confirmPopWin.showAtLocation(pass_layout, Gravity.NO_GRAVITY, location[0] + pass_layout.getWidth() / 2, location[1] - pass_layout.getHeight() * 2);
                            } else {
                                ToastUtil.showShort(BillInspectActivity.this, "服务器异常");
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                });
    }

    /*
     * 获取单据详情
     */
    private void getBillDetail() {


        datalist.clear();
        personList.clear();
        shareContent = new StringBuilder();
        String url = Constant.SERVER_URL + "bill/showDetail";

        OkHttpUtils.post()
                .url(url)
                .addParams("displayLevel", "1")
                .addParams("billId", String.valueOf(bill.getBillId()))
                .addParams("fatherId","0")
                .addParams("billType",bill.getBillType())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("response","error");
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(BillInspectActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response+" ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.optInt("statusCode");

                            Gson gson = new Gson();
                            if (statusCode == 0) {
                                JSONObject data = object.optJSONObject("data");
                                userImg = data.optString("userImg");
                                displayLevel = data.optString("displayLevel");
                                String billShowType = data.optString("billShowType");
                                JSONArray listlog = data.optJSONArray("listlog");
                                String nextType = data.optString("nextType");

                                String department = data.optString("department");
                                if (!TextUtils.isEmpty(department)) {
                                    apartment.setText(department);
                                }

                                List<ListLog> log = gson.fromJson(listlog.toString(), new TypeToken<List<ListLog>>() {
                                }.getType());
                                int log_length = log.size();
                                if (log_length != 0) {
                                    for (int i = 0; i < log_length; i++) {
                                        if (TextUtils.isEmpty(log.get(i).getPhone()) || checkDuplicate(personList, log.get(i).getPhone())) {
                                            continue;
                                        }
                                        Person person = new Person();
                                        person.setLinkName(log.get(i).getDealPerson());
                                        person.setLinkPhone(log.get(i).getPhone());
                                        person.setUserCode(log.get(i).getUserCode());
                                        personList.add(person);
                                    }
                                }

                                JSONArray list = data.optJSONArray("list");
                                if (list != null) {
                                    int len = list.length();
                                    if (len > 0) {
                                        for (int i = 0; i < len; i++) {
                                            JSONObject o = list.optJSONObject(i);
                                            BillDetailList billDetail = new BillDetailList();
                                            billDetail.setBillId(o.optInt("billId", 0));
                                            billDetail.setRecordId(o.optInt("recordId", 0));
                                            billDetail.setDisplayName(o.optString("displayName"));
                                            billDetail.setDisplayKey(o.optString("displayKey"));
                                            billDetail.setDisplayValue(o.optString("displayValue"));
                                            billDetail.setDisplayLevel(o.optString("displayLevel"));
                                            billDetail.setFatherId(o.optInt("fatherId", 0));
                                            billDetail.setBillShowType(o.optString("billShowType"));
                                            billDetail.setId(o.optInt("id", 0));
                                            billDetail.setNextType(nextType);
                                            billDetail.setListLogs(log);
                                            shareContent.append(o.optString("displayName"));
                                            shareContent.append(": ");
                                            shareContent.append(o.optString("displayValue"));
                                            shareContent.append("\n");
                                            datalist.add(billDetail);
                                        }

                                    }

                                    if (mTimeAxisAdapter != null) {
                                        mTimeAxisAdapter.updateData(log);
                                        OperatorUtil.setListViewHeight(inspect_list);
                                    } else {

                                        mTimeAxisAdapter = new TimeAxisAdapter(getApplicationContext(), log);

                                        inspect_list.setDividerHeight(0);
                                        inspect_list.setAdapter(mTimeAxisAdapter);
                                        OperatorUtil.setListViewHeight(inspect_list);
                                    }
                                    CommonAdapter<BillDetailList> commonAdapter = new CommonAdapter<BillDetailList>(BillInspectActivity.this, datalist, R.layout.item_bill_inspect) {
                                        @Override
                                        public void convert(ViewHolder holder, BillDetailList billDetailList) {
                                            holder.setText(R.id.bill_detail, billDetailList.getDisplayName());
                                            holder.setText(R.id.bill_value, billDetailList.getDisplayValue());
                                        }
                                    };
                                    bill_list.addFooterView(new View(BillInspectActivity.this));
                                    bill_list.setAdapter(commonAdapter);
                                    OperatorUtil.setListViewHeight(bill_list);

                                    scrollView.smoothScrollTo(0, 0);
                                }

                            } else {
                                ToastUtil.showShort(BillInspectActivity.this, "服务器异常");
                            }
                        } catch (JSONException e) {
                            Log.d("response","error");
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_bill_inspect);

        setRightButtonRes(R.drawable.share);
        setRightButtonRes2(R.drawable.chat_more);
        setRightButton2(View.VISIBLE);

        inspect_list = (MyFixList)findViewById(R.id.inspect_list);
        bill_list = (MyFixList)findViewById(R.id.bill_list);

//
//        inspect_list.setFooterDividersEnabled(false);
//        bill_list.setFooterDividersEnabled(false);
        bill_detail_layout = (RelativeLayout)findViewById(R.id.bill_detail_layout);
        pass_layout = (RelativeLayout)findViewById(R.id.pass_layout);
        cancel_layout = (RelativeLayout)findViewById(R.id.cancel_layout);
        root = (LinearLayout)findViewById(R.id.root);
        bottom = (LinearLayout)findViewById(R.id.bottom);

        iv_person = (CircleImageView)findViewById(R.id.iv_person);
        time = (TextView)findViewById(R.id.time);
        name = (TextView)findViewById(R.id.name);
        apartment = (TextView)findViewById(R.id.apartment);

        scrollView = (ScrollView)findViewById(R.id.scroll);


        chooseShareWindow = new ChooseShareWindow(this);

        btn_right.setOnClickListener(this);
        bt_right2.setOnClickListener(this);
        bill_detail_layout.setOnClickListener(this);
        pass_layout.setOnClickListener(this);
        cancel_layout.setOnClickListener(this);




    }



    private class BaseUiListener implements IUiListener {



        protected void doComplete(JSONObject values) {
        }

        @Override
        public void onComplete(Object o) {
            Log.d("onComplete",o.toString()+"  ");
        }

        @Override
        public void onError(UiError uiError) {
            Log.d("onError","code:"+uiError.errorCode+"  msg:"+uiError.errorMessage);
        }

        @Override
        public void onCancel() {
            Log.d("onCacncle","cancel");
        }
    }

    /*
     * 分享到微信
     */
    public void share(int request) {
        //String text = "测试一下";
        String text = shareContent.toString();
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = request;

        api.sendReq(req);
        //finish();
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    /**
     * QQ分享纯文本
     *
     * @param mContext 上下文
     */
    public void shareQQ(Context mContext) {
        String content = shareContent.toString();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        try {
            sendIntent.setClassName("com.tencent.mobileqq","com.tencent.mobileqq.activity.JumpActivity");
            //Intent chooseIntent = Intent.createChooser(sendIntent,)
            mContext.startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查号码重复
     * @param list 审批人
     * @param phone 手机号
     * @return true 号码已存在
     */
    private boolean checkDuplicate(List<Person> list,String phone) {
        int len = list.size();
        for (int i = 0;i < len;i++) {
            Person p = list.get(i);
            if (p.getLinkPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (chooseShareWindow.getChooseShareWindow().isShowing()) {
            chooseShareWindow.closeWindow();
        } else if (confirmPopWin.isShowing()) {
            confirmPopWin.dismiss();
        } else
            super.onBackPressed();
    }
}

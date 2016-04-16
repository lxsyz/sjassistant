package com.example.administrator.sjassistant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.TimeAxisAdapter;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.view.ChooseShareWindow;
import com.example.administrator.sjassistant.view.ConfirmPopWin;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class BillInspectActivity extends BaseActivity implements View.OnClickListener {

    private final int QQ_ITEM = 1;
    private final int WEIXIN_ITEM = 2;

    private String APP_ID = "1105323970";
    private static final String WEIXIN_ID = "wx979b127e5ff62391";
    private IWXAPI api;

    private ListView list;
    private RelativeLayout bill_detail_layout,pass_layout,cancel_layout;

    private ChooseShareWindow chooseShareWindow;
    private ConfirmPopWin confirmPopWin;
    private LinearLayout root;

    private TimeAxisAdapter mTimeAxisAdapter;

    private List<HashMap<String,Object>> datalist;

    private Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            confirmPopWin = new ConfirmPopWin(BillInspectActivity.this, OperatorUtil.dp2px(BillInspectActivity.this,80));

            confirmPopWin.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        confirmPopWin.dismiss();
                    }
                }
            });
        }
        confirmPopWin.setFocusable(true);

        api = WXAPIFactory.createWXAPI(this,WEIXIN_ID,true);


        if (!api.isWXAppInstalled()) {
            Toast.makeText(this, "没有安装微信", Toast.LENGTH_LONG).show();

        }
        api.registerApp(WEIXIN_ID);
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_bill_inspect);
        setTopText("");
        setRightButtonRes(R.drawable.share);
        setRightButtonRes2(R.drawable.chat_more);
        setRightButton2(View.VISIBLE);

        list = (ListView)findViewById(R.id.inspect_list);
        bill_detail_layout = (RelativeLayout)findViewById(R.id.bill_detail_layout);
        pass_layout = (RelativeLayout)findViewById(R.id.pass_layout);
        cancel_layout = (RelativeLayout)findViewById(R.id.cancel_layout);
        root = (LinearLayout)findViewById(R.id.root);

        chooseShareWindow = new ChooseShareWindow(this);

        btn_right.setOnClickListener(this);
        bt_right2.setOnClickListener(this);
        bill_detail_layout.setOnClickListener(this);
        pass_layout.setOnClickListener(this);
        cancel_layout.setOnClickListener(this);

        list.setDividerHeight(0);

        mTimeAxisAdapter = new TimeAxisAdapter(this,getData());
        list.setAdapter(mTimeAxisAdapter);
    }

    private List<HashMap<String,Object>> getData() {
        List<HashMap<String, Object>> listChild = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("content", "Jimmy");
        listChild.add(map);
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("content", "john");
        listChild.add(map1);
//        HashMap<String, Object> map2 = new HashMap<String, Object>();
//        map2.put("content", "hhh");
//        listChild.add(map2);
//        HashMap<String, Object> map3 = new HashMap<String, Object>();
//        map3.put("content", "hhhh");
//        listChild.add(map3);
//        HashMap<String, Object> map4 = new HashMap<String, Object>();
//        map4.put("content", "5h");
//        listChild.add(map4);
//        HashMap<String, Object> map5 = new HashMap<String, Object>();
//        map5.put("content", "h");
//        listChild.add(map5);
        return listChild;
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
                            Log.d("test", "test");
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

                break;
            case R.id.pass_layout:
                int[] location = new int[2];
                pass_layout.getLocationOnScreen(location);

                confirmPopWin.setContentText(getString(R.string.warn1));
                confirmPopWin.showAtLocation(pass_layout, Gravity.NO_GRAVITY, location[0] + pass_layout.getWidth() / 2, location[1] - confirmPopWin.getPopHeight());

                break;
            case R.id.cancel_layout:
                break;
            case R.id.bill_detail_layout:
                intent = new Intent(BillInspectActivity.this,BillDetailActivity.class);
                intent.putExtra("flag",2);
                startActivity(intent);
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
        String text = "测试一下";
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

    /*
     * QQ分享纯文本
     */
    public void shareQQ(Context mContext) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "测试一下");
        sendIntent.setType("text/plain");
        try {
            sendIntent.setClassName("com.tencent.mobileqq","com.tencent.mobileqq.activity.JumpActivity");
            //Intent chooseIntent = Intent.createChooser(sendIntent,)
            mContext.startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

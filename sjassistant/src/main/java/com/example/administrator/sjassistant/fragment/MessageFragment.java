package com.example.administrator.sjassistant.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.activity.AssistantActivity;
import com.example.administrator.sjassistant.activity.GonggaoActivity;
import com.example.administrator.sjassistant.activity.MainActivity;
import com.example.administrator.sjassistant.activity.MessageActivity;
import com.example.administrator.sjassistant.activity.PostInformActivity;
import com.example.administrator.sjassistant.activity.PostMessageActivity;
import com.example.administrator.sjassistant.activity.UnfinishedWorkActivity;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.ExampleUtil;
import com.example.administrator.sjassistant.util.Notifier;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.ServerConfigUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.AddContactsWin;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MessageFragment extends Fragment implements View.OnClickListener {

    private ImageView btn_left,btn_right;
    private TextView title;
    private LinearLayout message_inform_layout,gonggao_layout,unfinishedwork_layout,assistant_layout;

    private TextView num_unfinish;

    private TextView unfinished_text,unfinished_time,
                    message_text,message_time,
                    gonggao_text,gonggao_time,
                    assistant_text,assistant_time;


    private AddContactsWin popwindow;

    private String gonggaoContent;
    private String workDate;    //待办
    private String helperDate;  //小助理
    private int workCount;      //待办数量
    private int messageCount;
    private int notesCount;     //公告数量
    private int helperCount;


    private String gonggaoDate; //
    private String messageContent;
    private String helperContent;
    private String messageDate;

    private MainReceiver receiver;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        SharedPreferences sp = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        Constant.username = sp.getString("username", null);
        if (TextUtils.isEmpty(sp.getString("server_address",null))) {
            Constant.SERVER_URL = Constant.TEST_SERVER_URL;
        } else {
            ServerConfigUtil.setServerConfig(getActivity());
        }
        Log.d("activity","messag e  oncreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);
        initView(rootView);

        if (popwindow == null) {
            popwindow = new AddContactsWin(getActivity(),this, OperatorUtil.dp2px(getActivity(), 250),
                    OperatorUtil.dp2px(getActivity(),160));

            popwindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (!hasFocus) {
                        popwindow.dismiss();
                    }
                }
            });
        }
        popwindow.setFocusable(true);
        popwindow.setItem1Text("发布公告");
        popwindow.setItem2Text("发布消息、通知");

        receiver = new MainReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(JPushInterface.ACTION_REGISTRATION_ID);
        filter.addAction(JPushInterface.ACTION_MESSAGE_RECEIVED);
        filter.addAction(JPushInterface.ACTION_NOTIFICATION_RECEIVED);
        filter.addAction(JPushInterface.ACTION_NOTIFICATION_OPENED);
        filter.addAction(JPushInterface.ACTION_RICHPUSH_CALLBACK);
        filter.addAction(JPushInterface.ACTION_CONNECTION_CHANGE);
        filter.addCategory("com.example.administrator.sjassistant");
        // 注册广播
        getActivity().registerReceiver(receiver, filter);

        return rootView;
    }

    private void initView(View rootView) {
        btn_left = (ImageView)rootView.findViewById(R.id.bt_left);
        btn_right = (ImageView)rootView.findViewById(R.id.bt_right);
        title = (TextView)rootView.findViewById(R.id.tv_center);

        btn_left.setVisibility(View.INVISIBLE);
        title.setText("消息");
        btn_right.setImageResource(R.drawable.post_message);
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(this);

        message_inform_layout = (LinearLayout)rootView.findViewById(R.id.message_inform_layout);
        gonggao_layout = (LinearLayout)rootView.findViewById(R.id.gonggao_layout);
        unfinishedwork_layout = (LinearLayout)rootView.findViewById(R.id.unfinishedwork_layout);
        assistant_layout = (LinearLayout)rootView.findViewById(R.id.assistant_layout);
        num_unfinish = (TextView)rootView.findViewById(R.id.num_unfinish);

        unfinished_text = (TextView)rootView.findViewById(R.id.unfinished_text);
        unfinished_time = (TextView)rootView.findViewById(R.id.unfinished_time);
        message_text = (TextView)rootView.findViewById(R.id.message_text);
        message_time = (TextView)rootView.findViewById(R.id.message_time);
        gonggao_text = (TextView)rootView.findViewById(R.id.gonggao_text);
        gonggao_time = (TextView)rootView.findViewById(R.id.gonggao_time);
        assistant_text = (TextView)rootView.findViewById(R.id.assistant_text);
        assistant_time = (TextView)rootView.findViewById(R.id.assistant_time);


        if (num_unfinish.getText().toString().equals("0")) {
            num_unfinish.setVisibility(View.GONE);
        }

        message_inform_layout.setOnClickListener(this);
        gonggao_layout.setOnClickListener(this);
        unfinishedwork_layout.setOnClickListener(this);
        assistant_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.message_inform_layout:
                intent = new Intent(getActivity(),MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.unfinishedwork_layout:
                intent = new Intent(getActivity(), UnfinishedWorkActivity.class);
                startActivity(intent);
                break;
            case R.id.assistant_layout:
                intent = new Intent(getActivity(),AssistantActivity.class);
                startActivity(intent);
                break;
            case R.id.gonggao_layout:
                intent = new Intent(getActivity(), GonggaoActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_right:
                if (popwindow.isShowing()) {
                    popwindow.dismiss();
                } else
                    popwindow.showAsDropDown(btn_right);
                break;

            case R.id.choose:
                intent = new Intent(getActivity(),PostInformActivity.class);
                startActivity(intent);
                break;
            case R.id.add:
                intent = new Intent(getActivity(), PostMessageActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (popwindow.isShowing()) {
            popwindow.dismiss();
        }

        showMessage();
    }

    /*
     * 展示消息 公告等
     */
    private void showMessage() {
        String url = Constant.SERVER_URL + "message/show";

        //Constant.username = username;

        OkHttpUtils.post()
                .url(url)
                .addParams("userCode",Constant.username)
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
                            Log.d("statusCode", statusCode + " ");
                            if (statusCode == 0) {

                                gonggaoContent = data.getString("notesContent");
                                workDate = data.getString("workDate");
                                //helperDate = data.getString("helperDate");
                                //helperContent = data.getString("helperContent");
                                //helperCount = data.getString("helperCount");
                                workCount = data.getInt("workCount");
                                messageCount = data.getInt("messageCount");
                                notesCount = data.getInt("notesCount");


                                gonggaoDate = data.getString("notesDate");
                                messageContent = data.getString("messageContent");

                                messageDate = data.getString("messageDate");

                                gonggao_time.setText(gonggaoDate);
                                assistant_time.setText(helperDate);
                                unfinished_time.setText(workDate+" ");
                                message_time.setText(messageDate);

                                if (notesCount == 0) {
                                    gonggao_text.setText("暂时没有新公告");
                                } else {
                                    gonggao_text.setText(gonggaoContent);
                                }
                                if (workCount == 0) {
                                    num_unfinish.setVisibility(View.GONE);
                                    unfinished_text.setText("暂时没有待办工作");
                                } else {
                                    num_unfinish.setVisibility(View.VISIBLE);
                                    num_unfinish.setText(String.valueOf(workCount));
                                    unfinished_text.setText(workCount + "个待审批单据暂未处理");
                                }
                                if (messageCount == 0) {
                                    message_text.setText("暂时没有新的消息、通知");
                                } else {
                                    message_text.setText(messageContent);
                                }

                            } else {
                                ToastUtil.show(getActivity(), "服务器异常");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        Log.d("activity","message fragment stop");
        super.onStop();
    }



    public class MainReceiver extends BroadcastReceiver {
        private static final String TAG = "JPush";
        Notifier notifier = new Notifier(getActivity());

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            //printBundle(bundle);
            Log.d("response","我的推送");
            notifier.noti();
            if (JPushInterface.ACTION_REGISTRATION_ID
                    .equals(intent.getAction())) {
                String regId = bundle
                        .getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                // send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                    .getAction())) {
                Log.d(TAG,
                        "[MyReceiver] 接收到推送下来的自定义消息: "
                                + bundle.getString(JPushInterface.EXTRA_MESSAGE));

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED
                    .equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle
                        .getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

                showMessage();

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                    .getAction())) {
                Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
                // 打开自定义的Activity
                if (MainActivity.isForeground) {
                    showMessage();
                } else {
                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtras(bundle);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
                    .getAction())) {
                Log.d(TAG,
                        "[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
                                + bundle.getString(JPushInterface.EXTRA_EXTRA));
                // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
                // 打开一个网页等..
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
                    .getAction())) {
                boolean connected = intent.getBooleanExtra(
                        JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.w(TAG, "[MyReceiver]" + intent.getAction()
                        + " connected state change to " + connected);
            } else {
                Log.d(TAG,
                        "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        }
    }

    private void processCustomMessage(Context context, Bundle bundle) {
        if (MainActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (null != extraJson && extraJson.length() > 0) {
                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);

                    }
                } catch (JSONException e) {

                }

            }
            context.sendBroadcast(msgIntent);
        }
    }
}

package com.example.administrator.sjassistant.fragment;

import android.content.Intent;
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
import com.example.administrator.sjassistant.activity.MessageActivity;
import com.example.administrator.sjassistant.activity.PostInformActivity;
import com.example.administrator.sjassistant.activity.PostMessageActivity;
import com.example.administrator.sjassistant.activity.UnfinishedWorkActivity;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.AddContactsWin;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message,container,false);
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

        Log.d("username",Constant.username+" ");
        OkHttpUtils.post()
                .url(url)
                .addParams("userCode",Constant.username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error",e.getMessage()+" ");
                        ErrorUtil.NetWorkToast(getActivity());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response + " ");
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.getInt("statusCode");
                            JSONObject data = object.getJSONObject("data");
                            Log.d("statusCode",statusCode+" ");
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
                                unfinished_time.setText(workDate);
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
                                    num_unfinish.setText(String.valueOf(workCount));
                                    unfinished_text.setText(workCount+"个待审批单据暂未处理");
                                }
                                if (messageCount == 0) {
                                    message_text.setText("暂时没有新的消息、通知");
                                } else {
                                    message_text.setText(messageContent);
                                }

                            } else {
                                ToastUtil.show(getActivity(),"服务器异常");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}

package com.example.administrator.sjassistant.bean;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.sjassistant.util.Constant;

/**
 * Created by Administrator on 2016/5/19.
 */
public class DownloadHandler extends Handler {
    private TextView btn;
    private static final int FINISH = 2;
    private static final int DOWNLOADING = 1;
    private static final int OPEN = 0;
    public void setBtn(TextView btn) {
        this.btn = btn;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case FINISH:
                btn.setText("打 开");
                //btn.setTextColor(Color.parseColor("#E8BB5B"));
                break;
            case DOWNLOADING:
                btn.setText("下载中...");
                btn.setClickable(false);
                //btn.setBackgroundColor(Color.parseColor("#969696"));
                break;
            case OPEN:
                break;
        }


    }

    //    private ProgressBar bar;
//    public void setBar(ProgressBar bar)
//    {
//        this.bar = bar;
//    }
//    @Override
//    public void handleMessage(Message msg)
//    {
//        BarInfo bi = (BarInfo) msg.obj;
//        bar.setVisibility(View.VISIBLE);
//        bar.setMax(100);
//        Log.d("response","bar"+bi.getCurrentProgress());
//        bar.setProgress(bi.getCurrentProgress());
//    }
}

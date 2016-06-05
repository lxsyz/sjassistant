package com.example.administrator.sjassistant.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.Attachment;
import com.example.administrator.sjassistant.bean.BarInfo;
import com.example.administrator.sjassistant.bean.DownloadHandler;
import com.example.administrator.sjassistant.service.DownLoadService;
import com.example.administrator.sjassistant.util.FileUtil;
import com.example.administrator.sjassistant.view.ChangeNumberDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 该文件已废弃
 */
public class DownloadActivity extends BaseActivity {

    private DownLoadService downLoadService;
    /*
     * 滑动停止
     */
    private boolean mListViewIdle = false;

    private File file;
    ListView lv;
    CommonAdapter<Attachment> commonAdapter;

    private String fileType;

    private List<Attachment> datalist = new ArrayList<>();

    private int[] progress = new int[1000];
    private int number = 0;

    private Map<Integer,BarInfo> map = new HashMap<>();
    private Map<Integer,DownloadHandler> mItemLoadHandlers = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        file = new File(Environment.getExternalStorageDirectory()+"/审计助理");
        if (file.exists()) {

        } else {
            file.mkdirs();
        }

        fileType = getIntent().getStringExtra("fileType");
        datalist = (ArrayList<Attachment>)getIntent().getSerializableExtra("datalist");
        for (int i = 0;i < datalist.size();i++) {
            String filepath = datalist.get(i).getFilePath();
            String url = "http://219.234.5.13:8080/app/message/download/aa?name="
                    + filepath
                    + "&id="
                    + datalist.get(i).getId()
                    + "&fileType="
                    + fileType;

            datalist.get(i).setUrl(url);
        }
        Intent intent = new Intent(DownloadActivity.this, DownLoadService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("datalist", (ArrayList) datalist);
        intent.putExtras(bundle);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_download);
        setTopText("附件下载");
        lv = (ListView)findViewById(R.id.list);



    }

    @Override
    protected void onResume() {
        super.onResume();

        commonAdapter = new CommonAdapter<Attachment>(DownloadActivity.this,datalist,R.layout.item_download) {
            @Override
            public void convert(ViewHolder holder, Attachment attachment) {
//                ProgressBar bar = holder.getView(R.id.progress);
                int position = holder.getPosition();
//                String filepath = attachment.getFilePath();
//
//                String url = "http://219.234.5.13:8080/app/message/download/aa?name="
//                        + filepath
//                        + "&id="
//                        + attachment.getId()
//                        + "&fileType="
//                        + fileType;
//                final File f = new File(file,filepath);
//                if (f.exists()) {
//                    Log.d("response","exist");
//                    holder.setText(R.id.filename, filepath + "已经存在");
//                    holder.getView(R.id.progress).setVisibility(View.GONE);
//                    holder.getView(R.id.open).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            FileUtil.openFile(DownloadActivity.this, f);
//                            Log.d("asd","asd");
//                        }
//                    });
//                } else {
//                    Log.d("response","no exist");
//                    holder.getView(R.id.progress).setVisibility(View.VISIBLE);
                    holder.setText(R.id.filename, attachment.getFilePath());
//                    FileUtil.downloadFile(DownloadActivity.this, url, file.getAbsolutePath(), filepath, bar);
//                }
//                listentProgress(number,bar);
//                number++;

                DownloadHandler handler = null;
//                if (!mItemLoadHandlers.containsKey(position)) {
//                    handler = new DownloadHandler();
//                    mItemLoadHandlers.put(position,handler);
//                    handler.setBar(bar);
//                } else {
//                    handler = mItemLoadHandlers.get(position);
//                    handler.setBar(bar);
//                }

                if (!map.containsKey(position))
                {// 设置 BarInfo 如果当前行已经存在就不用设置了
                    BarInfo value = new BarInfo();
                    value.setCurrentProgress(0);
                    map.put(position, value);
                }
                BarInfo bi = map.get(position);
                Log.d("response",bi.getCurrentProgress()+" ");
//                bar.setProgress(bi.getCurrentProgress());
            }
        };

        lv.setAdapter(commonAdapter);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downLoadService = ((DownLoadService.MsgBinder)service).getService();
            Log.d("response","connect");
            downLoadService.setmItemLoadHandlers(mItemLoadHandlers);
            downLoadService.startDownLoad();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {


        }
    };

    private void listentProgress(final int number, final ProgressBar bar) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress[number] < 100) {
                    progress = downLoadService.getPercent();
                    Log.d("response","numberrrr  "+number);
                    Log.d("response","progerss   "+progress[number]);
                    bar.setProgress(progress[number]);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }
}

package com.example.administrator.sjassistant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.CommonAdapter;
import com.example.administrator.sjassistant.adapter.DetailExpandableAdapter;
import com.example.administrator.sjassistant.adapter.ViewHolder;
import com.example.administrator.sjassistant.bean.BillDetailList;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.CHScrollView2;
import com.example.administrator.sjassistant.view.HListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/6.
 */
public class BillDetailActivity extends BaseActivity {

    //表示对应着哪种层级
    //private int flag=0;

    private ExpandableListView expandableListView;

    /*
     * 横竖都能滑动的listview
     */
    private ListView mListView;

    /*
     * 每屏最多四个
     */
    private final int maxCount = 4;

    /*
     * 屏幕宽度
     */
    private int mScreenWidth;

    private ListView lv;
    public HorizontalScrollView mTouchView;

    protected List<CHScrollView2> mHScrollViews = new ArrayList<CHScrollView2>();
    //private GridView gv;

    //private DetailExpandableAdapter adapter;

    private ArrayList<Object> billList = new ArrayList<Object>();
    private List<BillDetailList> billDetailLists = new ArrayList<>();

    private int billId;
    private String displayLevel;
    private String level;

    private LayoutInflater layoutInflater;

    private int cols;
    private int rows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
    }

    @Override
    protected void initView() {
        super.initView();

        setTopText("单据详细信息");

        billId = getIntent().getIntExtra("billId", -1);
        displayLevel = getIntent().getStringExtra("displayLevel");
        if (TextUtils.isEmpty(displayLevel)) {
            displayLevel = "1";
        }



    }

    @Override
    protected void onResume() {
        super.onResume();

        getData(displayLevel);


    }

    /*
     * 获取数据
     */
    private void getData(String displayLevel) {
        billDetailLists.clear();

        String url = Constant.SERVER_URL + "bill/showDetail";

        OkHttpUtils.post()
                .url(url)
                .addParams("displayLevel",displayLevel)
                .addParams("billId",String.valueOf(billId))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(BillDetailActivity.this);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.optInt("statusCode");

                            if (statusCode == 0) {
                                JSONObject data = object.optJSONObject("data");
                                level = data.optString("displayLevel");
                                String billShowType = data.optString("billShowType");
                                Gson gson = new Gson();
                                if (billShowType.equals("1")) {
                                    JSONArray list = data.optJSONArray("list");
                                    if (list != null) {
                                        int len = list.length();
                                        if (len > 0) {
                                            billDetailLists = gson.fromJson(list.toString(), new TypeToken<List<BillDetailList>>() {
                                            }.getType());
                                            showData(billDetailLists);
                                        }
                                    }
                                } else if (billShowType.equals("2")) {
                                    JSONArray list = data.optJSONArray("list");
                                    List<Map<String, String>> gridData = new ArrayList<Map<String, String>>();
                                    if (list != null) {
                                        //表格行数
                                        rows = list.length();
                                        if (rows > 0) {
                                            String[] colDatas = new String[0];
                                            int[] items = new int[0];
                                            for (int i = 0; i < rows; i++) {
                                                JSONArray sublist = new JSONArray(list.optString(i));
                                                //cols = 12;
                                                cols = sublist.length();
                                                items = new int[cols];
                                                colDatas = new String[cols];
                                                Map<String, String> map = new HashMap<String, String>();

                                                for (int j = 0; j < cols; j++) {
                                                    map.put("data" + j, sublist.optString(j));
                                                    colDatas[j] = "data" + j;
//                                                    items[j] = R.id.item_datav1;

                                                }
                                                gridData.add(map);
                                            }


                                            ScrollAdapter adapter = new ScrollAdapter(BillDetailActivity.this, gridData, R.layout.common_item_hlistview,
                                                    colDatas, items
                                                    );
                                            showData(adapter);
                                        }
                                    }
                                } else if (billShowType.equals("3")) {
                                    JSONArray list = data.optJSONArray("list");
                                    List<Map<String,String>> expandData = new ArrayList<Map<String, String>>();
                                    if (list != null) {
                                        int groupCount = list.length();
                                        if (groupCount != 0) {
                                            for (int i = 0;i < groupCount;i++) {
                                                JSONArray group = new JSONArray(list.optString(i));

                                                int childCount = group.length();
                                                Log.d("response","billId"+billId+"displayLevel"+level+"childCount "+childCount);
                                                if (childCount != 0) {
                                                    Map<String,String> map = new HashMap<String, String>();
                                                    map.put("title",new JSONArray(group.optString(0)).optString(0));
                                                    map.put("value",new JSONArray(group.optString(0)).optString(1));
                                                    if (1 >= childCount - 1) {
                                                        expandData.add(map);
                                                    } else {
                                                        for (int j = 1; j < childCount - 1; j++) {
                                                            String str = new JSONArray(group.optString(j)).optString(0);
                                                            String str2 = new JSONArray(group.optString(j)).optString(1);
                                                            if (str != null) {
                                                                //if (str.length() == 0)
                                                                //    map.put("data" + j, " ");
                                                                //else
                                                                map.put("data" + j, str);
                                                                if (TextUtils.isEmpty(str2)) {
                                                                    str2 = " ";
                                                                } else
                                                                    map.put("value" + j, str2);
                                                            }
                                                            expandData.add(map);
                                                        }
                                                    }
                                                }
                                            }

                                            DetailExpandableAdapter adapter = new DetailExpandableAdapter(BillDetailActivity.this,expandData);
                                            showData(adapter);
                                        }
                                    }
                                }
                                initItemClick();
                            } else {
                                ToastUtil.showShort(BillDetailActivity.this, "服务器异常");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /*
     * 初始点击item
     */
    private void initItemClick() {
        if(lv != null) {
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BillDetailList billDetail = (BillDetailList) lv.getItemAtPosition(position);
                    if (billDetail.getFatherId() != 0) {
                        Intent intent = new Intent(BillDetailActivity.this, BillDetailActivity.class);
                        intent.putExtra("billId", billDetail.getBillId());
                        intent.putExtra("displayLevel", level);
                        startActivity(intent);
                    }
                }
            });
        }


    }

    /*
     * 普通列表数据显示
     */
    private void showData(List<BillDetailList> list) {

        setCenterView(R.layout.activity_bill_detail);
        lv = (ListView)findViewById(R.id.list);

        lv.setAdapter(new CommonAdapter<BillDetailList>(BillDetailActivity.this,list,R.layout.list_item) {
            @Override
            public void convert(ViewHolder holder, BillDetailList billDetailList) {
                holder.setText(R.id.text,billDetailList.getDisplayName());
                holder.setText(R.id.value,billDetailList.getDisplayValue());
            }
        });
    }

    /*
     * 展示可扩展的数据行式
     */
    private void showData(DetailExpandableAdapter adapter) {
        setCenterView(R.layout.activity_expand_detail);
        expandableListView = (ExpandableListView)findViewById(R.id.expanded_list);
        expandableListView.setAdapter(adapter);
    }


    /*
     * 展示行列不固定的表格形式的项目
     */
    private void showData(ScrollAdapter adapter) {
        //setCenterView(R.layout.activity_grid_detail);
        setCenterView(R.layout.activity_hlistview);
        mListView = (ListView)findViewById(R.id.hlistview_scroll_list);
//            gv = (GridView)findViewById(R.id.gv);
//            for (int i = 0;i < 15;i++) {
//                billList.add("成交信息");
//            }
//            gv.setAdapter(new ArrayAdapter(this,R.layout.grid_item,R.id.textView,
//                    billList));
        mListView.setAdapter(adapter);
    }

    public void addHViews(final CHScrollView2 hScrollView) {
        if(!mHScrollViews.isEmpty()) {
            int size = mHScrollViews.size();
            CHScrollView2 scrollView = mHScrollViews.get(size - 1);
            final int scrollX = scrollView.getScrollX();
            //第一次满屏后，向下滑动，有一条数据在开始时未加入
            if(scrollX != 0) {
                mListView.post(new Runnable() {
                    @Override
                    public void run() {
                        //当listView刷新完成之后，把该条移动到最终位置
                        hScrollView.scrollTo(scrollX, 0);
                    }
                });
            }
        }
        mHScrollViews.add(hScrollView);
    }

    public void onScrollChanged(int l, int t, int oldl, int oldt){
        for(CHScrollView2 scrollView : mHScrollViews) {
            //防止重复滑动
            if(mTouchView != scrollView)
                scrollView.smoothScrollTo(l, t);
        }
    }


    class ScrollAdapter extends SimpleAdapter {

        private List<? extends Map<String, ?>> datas;
        private int res;
        private String[] from;
        private int[] to;
        private Context context;
        public ScrollAdapter(Context context,
                             List<? extends Map<String, ?>> data, int resource,
                             String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.context = context;
            this.datas = data;
            this.res = resource;
            this.from = from;
            this.to = to;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            LinearLayout root;
            //View[] lineViews = new View[to.length - 1];

            if(v == null) {
                v = LayoutInflater.from(context).inflate(res, null);
                root = (LinearLayout) v.findViewById(R.id.root_view);
                //第一次初始化的时候装进来
                addHViews((CHScrollView2) v.findViewById(R.id.item_chscroll_scroll));
                View[] views = new View[to.length];
                //单元格点击事件

                if (to.length <= maxCount) {
                    for (int i = 0; i < to.length; i++) {
                        TextView tv = new TextView(this.context);
                        tv.setGravity(Gravity.CENTER);

                        int width = mScreenWidth / to.length;

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,
                                ViewGroup.LayoutParams.MATCH_PARENT);
//                    View tv = v.findViewById(to[i]);
                        tv.setOnClickListener(clickListener);
                        tv.setLayoutParams(lp);
                        views[i] = tv;
                    }
                } else {
                    for (int i = 0; i < to.length; i++) {
                        TextView tv = new TextView(this.context);
                        tv.setGravity(Gravity.CENTER);

                        int width = mScreenWidth / to.length;

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(OperatorUtil.dp2px(this.context,200.0f),
                                ViewGroup.LayoutParams.MATCH_PARENT);
                        tv.setOnClickListener(clickListener);
                        tv.setLayoutParams(lp);
                        views[i] = tv;
                    }
                }

                v.setTag(views);
            } else
                root = (LinearLayout) v.findViewById(R.id.root_view);
            View[] holders = (View[]) v.getTag();


            View lineView = new View(this.context);
            lineView.setBackgroundColor(this.context.getResources().getColor(R.color.line));
            lineView.setLayoutParams(new ViewGroup.LayoutParams(OperatorUtil.dp2px(this.context, 1.2f),
                    ViewGroup.LayoutParams.MATCH_PARENT));

            int len = holders.length;
            boolean needLine = false;
            for(int i = 0 ; i < len; i++) {
                if (needLine) {
                    root.addView(lineView);

                }
                needLine = true;
                ((TextView)holders[i]).setText(this.datas.get(position).get(from[i]).toString());
                root.addView(holders[i]);
            }


            return v;
        }
    }

    //测试点击的事件
    protected View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //v.setBackgroundResource(R.drawable.linearlayout_green_round_selector);
            //Toast.makeText(BillDetailActivity.this, "点击了:" + ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BillDetailActivity.this,BillDetailActivity.class);
            intent.putExtra("displayLevel",level);
            intent.putExtra("billId",billId);
            startActivity(intent);
        }
    };


}

package com.example.administrator.sjassistant.activity;

import android.app.Activity;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.AddContactAdapter;
import com.example.administrator.sjassistant.adapter.SortAdapter;
import com.example.administrator.sjassistant.bean.SortModel;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.PinyinComparator;
import com.example.administrator.sjassistant.view.MyDialog;
import com.example.administrator.sjassistant.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/4/7.
 */
public class AddChatContact extends Activity implements View.OnClickListener {

    private ImageView search,delete;
    private EditText ed_name;

    private TextView bt_left,bt_right,tv_center;
    private ImageView bt_left2;

    private String name;

    private ListView sortListView;
    private SideBar sideBar;

    private AddContactAdapter adapter;

    private List<SortModel> datalist = new ArrayList<SortModel>();

    private PinyinComparator pinyinComparator;


    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addchat);
        initWindow();
        initView();

        count = getIntent().getIntExtra("count",0);
        if (count != 0) {
            bt_right.setText("确定（" + count + ")");
        }
    }

    private void initView() {
        search = (ImageView)findViewById(R.id.search);
        ed_name = (EditText)findViewById(R.id.search_content);
        delete = (ImageView)findViewById(R.id.delete_word);
        search.setOnClickListener(this);
        ed_name.setOnClickListener(this);
        delete.setOnClickListener(this);

        tv_center = (TextView)findViewById(R.id.tv_center);

        tv_center.setText("添加联系人");
        bt_left = (TextView)findViewById(R.id.bt_left2);
        bt_right = (TextView)findViewById(R.id.bt_right);
        bt_left.setVisibility(View.VISIBLE);

        bt_left2 = (ImageView)findViewById(R.id.bt_left);
        bt_left2.setVisibility(View.INVISIBLE);

        bt_right.setText("确定");
        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);


        pinyinComparator = new PinyinComparator();

        sortListView = (ListView)findViewById(R.id.contacts_list);
        sideBar = (SideBar)findViewById(R.id.sidebar);

        sideBar.setmOnTouching(new SideBar.OnTouchingLetterChangeListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                Log.d("tag", "touch");
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });


        //获取数据
        datalist = dealData(getResources().getStringArray(R.array.test));


        Collections.sort(datalist, pinyinComparator);
        adapter = new AddContactAdapter(this,datalist);
        sortListView.setAdapter(adapter);

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView v = (ImageView)view.findViewById(R.id.add);
                SortModel sm = (SortModel) sortListView.getItemAtPosition(position);
                Log.d("position",position+" ");
                if (sm.getChecked() == 0) {
                    v.setImageResource(R.drawable.radio_checked);
                    sm.setChecked(1);
                    count++;
                } else {
                    v.setImageResource(R.drawable.radio_unchecked);
                    sm.setChecked(0);
                    count--;
                }
                if (count != 0) {
                    bt_right.setText("确定（" + count + ")");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                if (!TextUtils.isEmpty(ed_name.getText().toString())) {

//                    intent = new Intent(this, SearchResultActivity.class);
//                    intent.putExtra("name",ed_name.getText().toString());
//                    startActivity(intent);
                }
                break;
            case R.id.delete_word:
                ed_name.setText("");
                break;

            case R.id.bt_left2:
                onBackPressed();
                break;

            case R.id.bt_right:
                if (count > 8) {
                    MyDialog dialog = new MyDialog(AddChatContact.this,R.style.dialog_style);
                    dialog.show();
                    dialog.setMain_text("多方通话最多允许添加8个人");
                    dialog.setVisibility(View.GONE);
                    dialog.setCenterVisibility(View.VISIBLE);
                } else {
                    this.finish();
                }
                break;
        }
    }


    /*
     * 处理数据，获取首字母
     */
    private List<SortModel> dealData(String [] data){
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i<data.length; i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(data[i]);
            sortModel.setChecked(0);
            //汉字转换成拼音
            String pinyin = OperatorUtil.getFirstChar(data[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetter(sortString.toUpperCase());
            }else{
                sortModel.setSortLetter("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;
    }

    private void filterData(String filter) {
        List<SortModel> filterDataList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filter)) {
            filterDataList = datalist;
        } else {
            filterDataList.clear();
            for (SortModel sortModel:datalist) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(
                        filter.toString().toUpperCase()
                )!= -1 || OperatorUtil.getFirstChar(name).toUpperCase().startsWith(filter.toString().toUpperCase())) {
                    filterDataList.add(sortModel);
                }
            }
        }

        Collections.sort(filterDataList,pinyinComparator);
        adapter.updateListView(filterDataList);
    }

    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}

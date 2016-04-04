package com.example.administrator.sjassistant.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.adapter.SortAdapter;
import com.example.administrator.sjassistant.bean.SortModel;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.PinyinComparator;
import com.example.administrator.sjassistant.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/4/4.
 */
public class ContactsDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView search,delete;
    private EditText ed_name;

    private String name;

    private ListView sortListView;
    private SideBar sideBar;

    private SortAdapter adapter;
    private TextView dialog;

    private List<SortModel> datalist = new ArrayList<SortModel>();

    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_contacts_detail);
        name = getIntent().getStringExtra("name");
        if (name != null) {
            setTopText(name);
        }

        search = (ImageView)findViewById(R.id.search);
        ed_name = (EditText)findViewById(R.id.search_content);
        delete = (ImageView)findViewById(R.id.delete_word);
        search.setOnClickListener(this);
        ed_name.setOnClickListener(this);
        delete.setOnClickListener(this);


        pinyinComparator = new PinyinComparator();

        sortListView = (ListView)findViewById(R.id.contacts_list);
        sideBar = (SideBar)findViewById(R.id.sidebar);

        sideBar.setmOnTouching(new SideBar.OnTouchingLetterChangeListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                Log.d("tag","touch");
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        //获取数据
        datalist = dealData(getResources().getStringArray(R.array.test));


        Collections.sort(datalist,pinyinComparator);
        adapter = new SortAdapter(this,datalist);
        sortListView.setAdapter(adapter);

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("position",position+" ");
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
}

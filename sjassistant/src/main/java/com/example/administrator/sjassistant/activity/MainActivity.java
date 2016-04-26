package com.example.administrator.sjassistant.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.fragment.ContactsFragment;
import com.example.administrator.sjassistant.fragment.MessageFragment;
import com.example.administrator.sjassistant.fragment.MyApplicationFragment;
import com.example.administrator.sjassistant.fragment.MySettingFragment;
import com.example.administrator.sjassistant.util.AppManager;
import com.example.administrator.sjassistant.util.ExampleUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener,MySettingFragment.BackHandlerInterface {

    private RelativeLayout message_layout,myapplication_layout,
                            mysetting_layout,contacts_layout;
    private ImageView message_iv,myapplication_iv,mysetting_iv,contacts_iv;
    private TextView message_tv,myapplication_tv,mysetting_tv,contacts_tv;

    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private ViewPager viewPager;

    public static MainActivity instance;
    //当前位置
    private int mCurrentIndex = 0;

    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppManager.getInstance().addActivity(this);

        Log.d("activity", "activity" + android.os.Process.myPid());
        Log.d("tag"," main id  "+Thread.currentThread().getId()+"main   thread"+ Thread.currentThread().getName());
        instance = MainActivity.this;

//        Intent intent = new Intent(MainActivity.this,MessageService.class);
//
//        startService(intent);

        initWindow();
        initView();

        registerMessageReceiver();


    }


    private void initView() {
        //super.initView();
        //setCenterView(R.layout.activity_main);
        //setTopText();
        viewPager = (ViewPager)findViewById(R.id.id_pager);
        message_layout = (RelativeLayout)findViewById(R.id.message_layout);
        myapplication_layout = (RelativeLayout)findViewById(R.id.myappication_layout);
        contacts_layout = (RelativeLayout)findViewById(R.id.contacts_layout);
        mysetting_layout = (RelativeLayout)findViewById(R.id.mysetting_layout);
        message_iv = (ImageView)findViewById(R.id.message_image);
        myapplication_iv = (ImageView)findViewById(R.id.myappication_image);
        mysetting_iv = (ImageView)findViewById(R.id.mysetting_image);
        contacts_iv = (ImageView)findViewById(R.id.contacts_image);
        message_tv = (TextView)findViewById(R.id.message_text);
        mysetting_tv = (TextView)findViewById(R.id.mysetting_text);
        myapplication_tv = (TextView)findViewById(R.id.myappication_text);
        contacts_tv = (TextView)findViewById(R.id.contacts_text);

        message_layout.setOnClickListener(this);
        myapplication_layout.setOnClickListener(this);
        contacts_layout.setOnClickListener(this);
        mysetting_layout.setOnClickListener(this);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        message_iv.setImageResource(R.drawable.message_checked);
        message_tv.setTextColor(Color.rgb(103, 197, 170));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetOtherItems();
                switch (position) {
                    case 0:
                        message_tv.setTextColor(Color.rgb(103, 197, 170));
                        message_iv.setImageResource(R.drawable.message_checked);
                        break;
                    case 1:
                        myapplication_tv.setTextColor(Color.rgb(103, 197, 170));
                        myapplication_iv.setImageResource(R.drawable.myapplication_checked);
                        break;
                    case 2:
                        contacts_tv.setTextColor(Color.rgb(103, 197, 170));
                        contacts_iv.setImageResource(R.drawable.contacts_checked);
                        break;
                    case 3:
                        mysetting_iv.setImageResource(R.drawable.mysetting_checked);
                        mysetting_tv.setTextColor(Color.rgb(103, 197, 170));
                        break;
                }
                mCurrentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        MessageFragment fragment1 = new MessageFragment();

        fragmentList.add(fragment1);
        MyApplicationFragment fragment2 = new MyApplicationFragment();
        fragmentList.add(fragment2);
        ContactsFragment fragment3 = new ContactsFragment();
        fragmentList.add(fragment3);
        Fragment fragment = new MySettingFragment();
        fragmentList.add(fragment);
    }




    private FragmentPagerAdapter adapter = new FragmentPagerAdapter(this.getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = fragmentList.get(position);

            return fragment;
        }


        @Override
        public int getCount() {
            return 4;
        }
    };

    //重置其他按钮
    private void resetOtherItems() {
        message_iv.setImageResource(R.drawable.message_unchecked);
        myapplication_iv.setImageResource(R.drawable.myapplication);
        mysetting_iv.setImageResource(R.drawable.mysetting_unchecked);
        contacts_iv.setImageResource(R.drawable.contacts_unchecked);
        message_tv.setTextColor(Color.parseColor("#5e5e5e"));
        myapplication_tv.setTextColor(Color.parseColor("#5e5e5e"));
        mysetting_tv.setTextColor(Color.parseColor("#5e5e5e"));
        contacts_tv.setTextColor(Color.parseColor("#5e5e5e"));
    }

    @Override
    public void onClick(View v) {
        resetOtherItems();
        switch (v.getId()) {
            case R.id.message_layout:
                viewPager.setCurrentItem(0);
                message_iv.setImageResource(R.drawable.message_checked);
                message_tv.setTextColor(Color.rgb(103,197,170));
                break;
            case R.id.myappication_layout:
                viewPager.setCurrentItem(1);
                myapplication_tv.setTextColor(Color.rgb(103, 197, 170));
                myapplication_iv.setImageResource(R.drawable.myapplication_checked);
                break;
            case R.id.contacts_layout:
                viewPager.setCurrentItem(2);
                contacts_tv.setTextColor(Color.rgb(103, 197, 170));
                contacts_iv.setImageResource(R.drawable.contacts_checked);
                break;
            case R.id.mysetting_layout:
                viewPager.setCurrentItem(3);
                mysetting_iv.setImageResource(R.drawable.mysetting_checked);
                mysetting_tv.setTextColor(Color.rgb(103,197,170));
                break;
        }
    }

    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onBackPressed() {
        if (settingFragment == null || !settingFragment.onBackPressed()) {
            toastPlayForExit();
        }
        //super.onBackPressed();
        //MessageService.messageService.stopSelf();

    }


    /*
     * 点击两次退出程序
     */
    private long exitTime = 0;

    public void toastPlayForExit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            AppManager.getInstance().AppExit(this);
        }
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        isForeground = true;
        Log.d("activity","main onresume");
        super.onResume();
    }


    /*
     * 接收自定义消息的广播
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION ="com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
            }
        }
    }

    /*
     * fragment中使用onActivityResult的处理
     */
    MySettingFragment settingFragment;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        settingFragment = (MySettingFragment) fragmentList.get(3);
        Log.d("tag",data+" ");
        settingFragment.onActivityResult(requestCode,resultCode,data);

    }


    //设置fragment的点击返回
    @Override
    public void setSelectedFragment(MySettingFragment fragment) {
        this.settingFragment = fragment;
    }

    @Override
    protected void onDestroy() {
        Log.d("activity"," main destroy");
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("activity", "onsave");
        super.onSaveInstanceState(outState);
    }

    /*
     * fragment间的切换
     */
//    public void replaceFragment(Fragment from,Fragment to) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
//        if (!to.isAdded()) {
//            Log.d("tag","asd");
//            transaction.hide(from).add(R.id.content,to).commit();
//        } else {
//            transaction.hide(from).show(to).commit();
//        }
//    }

}

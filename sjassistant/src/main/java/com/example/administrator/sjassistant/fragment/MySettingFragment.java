package com.example.administrator.sjassistant.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.activity.HelpActivity;
import com.example.administrator.sjassistant.activity.SettingActivity;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.FileUtil;
import com.example.administrator.sjassistant.view.ChoosePhotoWindow;
import com.example.administrator.sjassistant.view.CircleImageView;
import com.example.administrator.sjassistant.view.MyDialog;
import com.example.administrator.sjassistant.view.SexDialog;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MySettingFragment extends Fragment implements View.OnClickListener {
    private ImageView btn_left,btn_right;


    //显示在下面的内容
    private TextView title,nickname_text,sex_text,apartment_text,work_text,address_text;
    private LinearLayout nickname_layout,sex_layout,apartment_layout,work_layout,address_layout,
                        help_layout,root;

    //上面的小字内容
    private TextView username,apartment_top,work_top;
    private ImageView iv_admin;

    private CircleImageView user_photo;

    private ChoosePhotoWindow choosePhotoWindow;

    private String imgPath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //回调函数赋值
        if (!(getActivity() instanceof BackHandlerInterface)) {
            throw new ClassCastException("activity case exception");
        } else {
            backHandlerInterface = (BackHandlerInterface)getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mysetting,container,false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        btn_left = (ImageView)rootView.findViewById(R.id.bt_left);
        btn_right = (ImageView)rootView.findViewById(R.id.bt_right);
        title = (TextView)rootView.findViewById(R.id.tv_center);

        user_photo = (CircleImageView)rootView.findViewById(R.id.user_photo);

        nickname_layout = (LinearLayout)rootView.findViewById(R.id.nickname_layout);
        sex_layout = (LinearLayout)rootView.findViewById(R.id.sex_layout);
        apartment_layout = (LinearLayout)rootView.findViewById(R.id.apartment_layout);
        work_layout = (LinearLayout)rootView.findViewById(R.id.work_layout);
        address_layout = (LinearLayout)rootView.findViewById(R.id.address_layout);
        help_layout = (LinearLayout)rootView.findViewById(R.id.help_layout);
        root = (LinearLayout)rootView.findViewById(R.id.root);

        nickname_text = (TextView)rootView.findViewById(R.id.nickname_text);
        sex_text = (TextView)rootView.findViewById(R.id.sex_text);
        apartment_text = (TextView)rootView.findViewById(R.id.apratment_text);
        work_text = (TextView)rootView.findViewById(R.id.word_text);
        address_text = (TextView)rootView.findViewById(R.id.address_text);

        username = (TextView)rootView.findViewById(R.id.username);
        iv_admin = (ImageView)rootView.findViewById(R.id.iv_admin);
        apartment_top = (TextView)rootView.findViewById(R.id.apartment_top);
        work_top = (TextView)rootView.findViewById(R.id.work_top);

        choosePhotoWindow = new ChoosePhotoWindow(getActivity());

        btn_left.setVisibility(View.INVISIBLE);
        title.setText("我");
        btn_right.setImageResource(R.drawable.setting);
        btn_right.setVisibility(View.VISIBLE);


        nickname_layout.setOnClickListener(this);
        sex_layout.setOnClickListener(this);
        apartment_layout.setOnClickListener(this);
        work_layout.setOnClickListener(this);
        address_layout.setOnClickListener(this);
        help_layout.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        user_photo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        MyDialog textDialog = new MyDialog(getActivity());

        switch (v.getId()) {
            case R.id.nickname_layout:
                textDialog.show();
                textDialog.setMainTextVisibility(View.GONE);
                textDialog.setVisibility(View.GONE);
                textDialog.setCenterVisibility(View.VISIBLE);
                textDialog.setContentVisibility(View.VISIBLE);
                textDialog.setHandler(handler);
                textDialog.setFlag(1);
                //textDialog.show();
                break;
            case R.id.sex_layout:
                SexDialog dialog = new SexDialog(getActivity());
                dialog.setHandler(handler);
                dialog.show();
                break;
            case R.id.apartment_layout:
                textDialog.show();
                textDialog.setMainTextVisibility(View.GONE);
                textDialog.setVisibility(View.GONE);
                textDialog.setCenterVisibility(View.VISIBLE);
                textDialog.setContentVisibility(View.VISIBLE);
                textDialog.setHandler(handler);
                textDialog.setFlag(2);
                textDialog.show();
                break;
            case R.id.work_layout:
                textDialog.show();
                textDialog.setMainTextVisibility(View.GONE);
                textDialog.setVisibility(View.GONE);
                textDialog.setCenterVisibility(View.VISIBLE);
                textDialog.setContentVisibility(View.VISIBLE);
                textDialog.setHandler(handler);
                textDialog.setFlag(3);
                textDialog.show();
                break;
            case R.id.address_layout:
                textDialog.show();
                textDialog.setMainTextVisibility(View.GONE);
                textDialog.setVisibility(View.GONE);
                textDialog.setCenterVisibility(View.VISIBLE);
                textDialog.setContentVisibility(View.VISIBLE);
                textDialog.setHandler(handler);
                textDialog.setFlag(4);
                textDialog.show();
                break;
            case R.id.help_layout:
                intent = new Intent(getActivity(),HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.user_photo:
                choosePhotoWindow.showChoosePhotoWindow2(root);

                break;
            case R.id.bt_right:
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0X11:
                    Log.d("tag",Constant.gender+" haahah");
                    if (Constant.gender == 1)
                        sex_text.setText("男");
                    else sex_text.setText("女");
                    break;
                case 1:
                    nickname_text.setText(Constant.nickname);
                    username.setText(Constant.nickname);
                    break;
                case 2:
                    apartment_text.setText(Constant.apartment);
                    apartment_top.setText(Constant.apartment);
                    break;
                case 3:
                    work_text.setText(Constant.work);
                    work_top.setText(Constant.work);
                    break;
                case 4:
                    address_text.setText(Constant.address);
                    break;
            }
        }
    };



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        choosePhotoWindow.onActivityResult(requestCode, resultCode, data,
                new ChoosePhotoWindow.Upload() {

                    @Override
                    public void upload(String path) {
                        // TODO Auto-generated method stub
                        Log.d("tag", "path=" + path);
                        //imgPath = path;
                        if (path != null) {
//                            BitmapFactory.Options options = new BitmapFactory.Options();
//                            Bitmap bitmap = BitmapFactory.decodeFile(path,
//                                    options);
                            SharedPreferences sp = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();

                            editor.putString("imgPath", path);
                            editor.commit();
                            Bitmap bitmap1 = FileUtil.getSmallBitmap(path, 500, 500);
                            if (bitmap1 == null) {
                                Toast.makeText(getActivity(), "头像文件不存在", Toast.LENGTH_SHORT).show();
                                user_photo.setImageResource(R.drawable.customer_de);
                            } else
                                user_photo.setImageBitmap(bitmap1);

                        }

                        //uploadImg(path);
                    }

                });

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();

        initUiData();

    }



    //初始化界面数据
    private void initUiData() {
        SharedPreferences sp = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        imgPath = sp.getString("imgPath",null);

        if (imgPath != null) {
            Bitmap bitmap = FileUtil.getSmallBitmap(imgPath, 500, 500);
            if (bitmap == null) {
                //Toast.makeText(getActivity(), "头像文件不存在", Toast.LENGTH_SHORT).show();
                user_photo.setImageResource(R.drawable.customer_de);
            } else
                user_photo.setImageBitmap(bitmap);
        }
    }

    //fragment  回退控制
    private boolean mHandledPressed = false;
    public boolean onBackPressed() {
        if (choosePhotoWindow.isShowing()) {
            choosePhotoWindow.closeChoosePhotoWindow();
            return true;
        } else
            return false;
    }

    protected BackHandlerInterface backHandlerInterface;

    public interface BackHandlerInterface {
        public void setSelectedFragment(MySettingFragment fragment);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            backHandlerInterface = (BackHandlerInterface)getActivity();
        } catch (Exception e) {
            throw new ClassCastException("activity has done on back");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        backHandlerInterface.setSelectedFragment(this);
    }
}

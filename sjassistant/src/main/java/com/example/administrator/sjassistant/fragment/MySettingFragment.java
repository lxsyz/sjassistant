package com.example.administrator.sjassistant.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.administrator.sjassistant.activity.HelpActivity;
import com.example.administrator.sjassistant.activity.SettingActivity;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.view.ChoosePhotoWindow;
import com.example.administrator.sjassistant.view.MyDialog;
import com.example.administrator.sjassistant.view.SexDialog;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MySettingFragment extends Fragment implements View.OnClickListener {
    private ImageView btn_left,btn_right;
    private TextView title,nickname_text,sex_text,apartment_text,work_text,address_text;
    private LinearLayout nickname_layout,sex_layout,apartment_layout,work_layout,address_layout,
                        help_layout,root;

    private ImageView user_photo;

    private ChoosePhotoWindow choosePhotoWindow;
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

        user_photo = (ImageView)rootView.findViewById(R.id.user_photo);

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
                Log.d("tag", "path");
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
                    break;
                case 2:
                    apartment_text.setText(Constant.apartment);
                    break;
                case 3:
                    work_text.setText(Constant.work);
                    break;
                case 4:
                    address_text.setText(Constant.address);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("tag", "pathasdasdasd");
        choosePhotoWindow.onActivityResult(requestCode, resultCode, data,
                new ChoosePhotoWindow.Upload() {

                    @Override
                    public void upload(String path) {
                        // TODO Auto-generated method stub
                        Log.d("tag","path="+path);
                        //imgPath = path;
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(path,
                                options);
                        user_photo.setImageBitmap(bitmap);


                        //uploadImg(path);
                    }

                });

        super.onActivityResult(requestCode, resultCode, data);
    }
}

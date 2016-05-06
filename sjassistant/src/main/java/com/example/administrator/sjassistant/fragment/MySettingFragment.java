package com.example.administrator.sjassistant.fragment;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.activity.HelpActivity;
import com.example.administrator.sjassistant.activity.SettingActivity;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.FileUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.ChoosePhotoWindow;
import com.example.administrator.sjassistant.view.CircleImageView;
import com.example.administrator.sjassistant.view.MyDialog;
import com.example.administrator.sjassistant.view.SexDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MySettingFragment extends Fragment implements View.OnClickListener {
    private ImageView btn_left,btn_right;


    //显示在下面的内容
    private TextView title,nickname_text,sex_text,apartment_text,work_text,address_text;
    private LinearLayout nickname_layout,sex_layout,apartment_layout,work_layout,address_layout,
                        help_layout,root,photo_layout;

    //上面的小字内容
    private TextView username,apartment_top,work_top;
    private ImageView iv_admin;

    private CircleImageView user_photo;

    private ChoosePhotoWindow choosePhotoWindow;

    private String imgPath;

    private String portraitPath = Environment.getExternalStorageDirectory() + "/审计助理/Portrait/portrait.jpg";
//    private SharedPreferences sp;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("response","oncreate");

        File file = new File(Environment.getExternalStorageDirectory() + "/审计助理/Portrait");
        if (file.exists()) {}
        else file.mkdirs();

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
        Log.d("response","oncreatevbi");
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
        photo_layout = (LinearLayout)rootView.findViewById(R.id.photo_layout);


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
        //user_photo.setOnClickListener(this);
        photo_layout.setOnClickListener(this);
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
//                textDialog.show();
//                textDialog.setMainTextVisibility(View.GONE);
//                textDialog.setVisibility(View.GONE);
//                textDialog.setCenterVisibility(View.VISIBLE);
//                textDialog.setContentVisibility(View.VISIBLE);
//                textDialog.setHandler(handler);
//                textDialog.setFlag(2);
//                textDialog.show();
                break;
            case R.id.work_layout:
//                textDialog.show();
//                textDialog.setMainTextVisibility(View.GONE);
//                textDialog.setVisibility(View.GONE);
//                textDialog.setCenterVisibility(View.VISIBLE);
//                textDialog.setContentVisibility(View.VISIBLE);
//                textDialog.setHandler(handler);
//                textDialog.setFlag(3);
//                textDialog.show();
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
            case R.id.photo_layout:
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

                            Bitmap bitmap1 = FileUtil.getSmallBitmap(path, 500, 500);
                            if (bitmap1 == null) {
                                Toast.makeText(getActivity(), "头像文件不存在", Toast.LENGTH_SHORT).show();
                                user_photo.setImageResource(R.drawable.customer_de);
                            } else {
                                user_photo.setImageBitmap(bitmap1);
                                //压缩后的临时图片文件
                                File f = new File(portraitPath);
                                if (!f.exists()) {
                                    try {
                                        f.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (FileUtil.compressBitmap(bitmap1, portraitPath, 90)) {
                                    if (f.exists()) {

                                        uploadImg(f);

                                    } else {
                                        ToastUtil.showShort(getActivity(),"上传失败");
                                    }
                                }
                                bitmap1 = null;
                            }
                           // editor.commit();

                        }


                    }

                });

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();

        initUiData();

    }

    /*
     * 上传头像
     */
    private void uploadImg(final File file) {
        String url = Constant.SERVER_URL + "user/settings/changePortrait";

        OkHttpUtils.post()
                .url(url)
                .addParams("userCode", Constant.username)
                .addFile("image", System.currentTimeMillis()+".jpg",file)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage());
                        ErrorUtil.NetWorkToast(getActivity());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response + " ");
                        //file.delete();
                    }
                });
    }

    //初始化界面数据
    private void initUiData() {
        //sp = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        //imgPath = sp.getString("imgPath", null);
        getUserImg();
//        File file = new File(portraitPath);
//        if (!file.exists()) {
//            getUserImg();
//        } else {
//            Bitmap bitmap = FileUtil.getSmallBitmap(portraitPath,500,500);
//            if (bitmap == null) {
//                getUserImg();
//                //user_photo.setImageResource(R.drawable.customer_de);
//            } else
//                user_photo.setImageBitmap(bitmap);
//        }



        //获取用户信息
        getUserData();


    }

    /*
     *
     */
    private void getUserImg() {
        String url = Constant.SERVER_URL + "user/getPortrait";

        OkHttpUtils.get()
                .url(url)
                .addParams("userCode",Constant.username)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response) throws Exception {
                        InputStream stream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(stream);
                        stream.close();
                        return bitmap;
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                    }

                    @Override
                    public void onResponse(Object response) {
                        Log.d("response", response + " ");
                        //writeToFile((InputStream) response, portraitPath);
                        Bitmap bitmap = (Bitmap)response;
                        user_photo.setImageBitmap(bitmap);
//                        FileUtil.saveBitmap2file(bitmap,portraitPath);
//                        if (bitmap != null && !bitmap.isRecycled()) {
//                            bitmap.recycle();
//                            bitmap = null;
//                        }

                    }
                });
    }
    /*
     * 获取用户信息
     */
    private void getUserData() {
        String url = Constant.SERVER_URL + "user/info";

        OkHttpUtils.post()
                .url(url)
                .addParams("userCode", Constant.username)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error", e.getMessage() + " ");
                        ErrorUtil.NetWorkToast(getActivity());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);

                        try {
                            JSONObject object = new JSONObject(response);
                            int statusCode = object.optInt("statusCode");
                            JSONObject data = object.optJSONObject("data");
                            if (statusCode == 0) {
                                String user = data.optString("userName");
                                String sex = data.optString("sex");
                                String department = data.optString("dept_name");
                                String role = data.optString("role_name");
                                String address = data.optString("address");

//                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE).edit();
//                                editor.putString("nickname",user);
//                                editor.putString("sex",sex);
//                                editor.putString("dept_name",department);
//                                editor.putString("role_name",role);
//                                editor.putString("address",address);
//                                editor.commit();
                                if (!TextUtils.isEmpty(user)) {
                                    nickname_text.setText(user);
                                    username.setText(user);
                                }
                                if (!TextUtils.isEmpty(sex)) {
                                    if (sex.equals("1"))
                                        sex_text.setText("男");
                                    else
                                        sex_text.setText("女");
                                }

                                if (!TextUtils.isEmpty(department) && !department.equals("null")) {
                                    apartment_text.setText(department);
                                    apartment_top.setText(department);
                                }
                                if (!TextUtils.isEmpty(address) && !address.equals("null"))
                                    address_text.setText(address);
                                if (!TextUtils.isEmpty(role) && !role.equals("null")) {
                                    work_text.setText(role);
                                    work_top.setText(role);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }

    /*
     * 将头像保存至本地
     */
    private void writeToFile(InputStream inputStream,String path) {
        try {

            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
            byte[] b = new byte[1024];
            int len = inputStream.read(b);
            while (len != -1) {
                outputStream.write(b,0,len);
                len = inputStream.read(b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("response", "destroyvvvv");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("response", "destroy");
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

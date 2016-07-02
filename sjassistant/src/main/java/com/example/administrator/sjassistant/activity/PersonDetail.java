package com.example.administrator.sjassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.bean.Person;
import com.example.administrator.sjassistant.util.Constant;
import com.example.administrator.sjassistant.util.ErrorUtil;
import com.example.administrator.sjassistant.util.OperatorUtil;
import com.example.administrator.sjassistant.util.ToastUtil;
import com.example.administrator.sjassistant.view.ChangeNumberDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/4.
 */
public class PersonDetail extends BaseActivity implements View.OnClickListener {

    private TextView name,phone,customer_type,person_work,apartment,company_name;
    private ImageView iv_phone;
    Person p;
    //PackageManager pm = getPackageManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState == null) {
            p = (Person) getIntent().getSerializableExtra("person");
            if (p != null) {
                setTopText(p.getLinkName());
                name.setText(p.getLinkName());
                phone.setText(p.getLinkPhone());
                customer_type.setText(p.getCustomerTypeName());
                person_work.setText(p.getCustomerPostName());
                apartment.setText(p.getCustomerDeptName());
                company_name.setText(p.getCustomerName());
            }
        } else {
            p = (Person) savedInstanceState.getSerializable("person");
        }

        iv_phone.setOnClickListener(this);

    }

    @Override
    protected void initView() {
        super.initView();
        setCenterView(R.layout.activity_persondetail);
        RelativeLayout phone_layout = (RelativeLayout)findViewById(R.id.phone_layout);
        name = (TextView)findViewById(R.id.name);
        phone= (TextView)findViewById(R.id.phone);
        customer_type= (TextView)findViewById(R.id.customer_type);
        person_work= (TextView)findViewById(R.id.person_work);
        apartment= (TextView)findViewById(R.id.apartment);
        company_name= (TextView)findViewById(R.id.company_name);

        iv_phone = (ImageView)findViewById(R.id.right_arrow2);

        phone_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_arrow2:
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+p.getPhoneNumber()));
//                if ( Build.VERSION.SDK_INT >= 23 &&
//                        ContextCompat.checkSelfPermission(PersonDetail.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    startActivity(intent);
//                }
//                else {
//                    startActivity(intent);
//                    Toast.makeText(PersonDetail.this,"Something",Toast.LENGTH_SHORT).show();
//                }

                Intent intent = new Intent(PersonDetail.this,MoreContact.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("person",p);
                intent.putExtras(bundle);
                startActivity(intent);

                break;
            case R.id.phone_layout:
                ChangeNumberDialog dialog2 = new ChangeNumberDialog(this);
                dialog2.show();
                dialog2.setFlag(0);
                dialog2.setOnItemClickListener(new ChangeNumberDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(String str) {
                        if (OperatorUtil.isPhoneNumber(str)) {
                            changeNumber(str);
                        } else {
                            ToastUtil.showShort(PersonDetail.this, "手机号码格式不正确");
                            phone.setText("");

                        }
                    }
                });
                break;
        }
    }

    private void changeNumber(final String newPhone) {
        String url = Constant.SERVER_URL + "phoneBook/editCustomerUser";

        OkHttpUtils.post()
                .url(url)
                .addParams("id",p.getId())
                .addParams("linkPhone",newPhone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ErrorUtil.NetWorkToast(PersonDetail.this);
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("response",response);
                        phone.setText(newPhone);
                    }
                });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("person",p);
    }
}

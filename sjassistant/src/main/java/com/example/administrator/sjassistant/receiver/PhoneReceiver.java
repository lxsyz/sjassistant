package com.example.administrator.sjassistant.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.administrator.sjassistant.activity.StartChattingActivity;
import com.example.administrator.sjassistant.util.Constant;

/**
 * Created by Administrator on 2016/5/10.
 */
public class PhoneReceiver extends BroadcastReceiver {
    private final String TAG = "message";
    private static boolean mIncomingFlag = false;
    private static String mIncomingNumber = null;

    @Override
    public void onReceive(Context context, Intent intent) {

        //拨打电话
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            mIncomingFlag = false;
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        } else {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    mIncomingNumber = intent.getStringExtra("incoming_number");
                    Log.d(TAG, "Ringing  " + mIncomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    Log.d(TAG,"incoming accept: " + mIncomingNumber);

                    if (Constant.isMaster) {
                        if (mIncomingNumber.equals("18060248088")
                                || mIncomingNumber.equals("13375909998")
                                || mIncomingNumber.equals("18046240558")
                                || mIncomingNumber.equals("18060248288")) {
                            Intent i = new Intent(context, StartChattingActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    //if (mIncomingFlag) {
                    Log.d(TAG,"idle+" + mIncomingNumber);
                    // }
                    break;
            }
        }
    }
}

package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.itheima.mobilesafe.utils.ConstantValue;
import com.itheima.mobilesafe.utils.SpUtil;
import com.itheima.mobilesafe.utils.ToastUtil;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootCompleteReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG,"重启手机");

        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNumber = telephonyManager.getSimSerialNumber();
        String simSumber = SpUtil.getString(context, ConstantValue.SIM_NUMBER,"");

        if (!simSerialNumber.equals(simSumber)){

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("5556",null,"sim change!!",null,null);

        }

    }
}

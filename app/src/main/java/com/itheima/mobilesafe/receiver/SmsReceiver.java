package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.LocationService;
import com.itheima.mobilesafe.utils.ConstantValue;
import com.itheima.mobilesafe.utils.SpUtil;
import com.itheima.mobilesafe.utils.ToastUtil;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG,"收到短信");
        boolean open_security = SpUtil.getBoolean(context, ConstantValue.OPEN_SECURITY,false);

        if (open_security){
            Object[] objects = (Object[])intent.getExtras().get("pdus");
            for (Object object : objects){

                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])object);
                String phone = smsMessage.getOriginatingAddress();
                String messageBody = smsMessage.getMessageBody();

                if (messageBody.contains("#*alarm*#")){
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }else if (messageBody.contains("#*location*#")){
                    Intent intent1 = new Intent(context, LocationService.class);
                    context.startService(intent1);
                }else if (messageBody.contains("#*wipedata*#")){

                }else if (messageBody.contains("#*lockscreen*#")){

                }
            }
        }
    }
}

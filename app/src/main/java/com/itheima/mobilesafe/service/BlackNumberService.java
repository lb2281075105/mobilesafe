package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.itheima.mobilesafe.db.dao.BlackNumberDao;

import java.lang.reflect.Method;


public class BlackNumberService<ITelephony> extends Service {

    private InnerSmsReceiver innerSmsReceiver;
    private BlackNumberDao dao;
    private TelephonyManager mTm;
    private MYPhoneStateListener myPhoneStateListener;
    private static final String TAG = "BlackNumberService";
    private BroadcastReceiver mInnerSmsReceiver;
    private MyContentObserver mContentObserver;

    @Override
    public void onCreate() {

        // 监听短信
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(1000);

        innerSmsReceiver = new InnerSmsReceiver();
        registerReceiver(innerSmsReceiver,intentFilter);


        // 监听电话
        mTm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MYPhoneStateListener();
        mTm.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        super.onCreate();
    }
    class MYPhoneStateListener extends PhoneStateListener{

        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state){

                case TelephonyManager.CALL_STATE_IDLE:
                    // 空闲
                    Log.i(TAG,"挂断电话..................");

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // 摘机
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    // 响铃
                    Log.i(TAG,"响铃..................");
                    endCall(phoneNumber);
                    break;
            }

            super.onCallStateChanged(state, phoneNumber);
        }
    }

    private void endCall(String phoneNumber) {

        int mode = dao.getMode(phoneNumber);

        if(mode == 2 || mode == 3){
//			ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
            //ServiceManager此类android对开发者隐藏,所以不能去直接调用其方法,需要反射调用
            try {
                //1,获取ServiceManager字节码文件
                Class<?> clazz = Class.forName("android.os.ServiceManager");
                //2,获取方法
                Method method = clazz.getMethod("getService", String.class);
                //3,反射调用此方法
                IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
                //4,调用获取aidl文件对象方法
//                ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
//                //5,调用在aidl中隐藏的endCall方法
//                iTelephony.endCall();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //6,在内容解析器上,去注册内容观察者,通过内容观察者,观察数据库(Uri决定那张表那个库)的变化
            mContentObserver = new MyContentObserver(new Handler(),phoneNumber);
            getContentResolver().registerContentObserver(
                    Uri.parse("content://call_log/calls"), true, mContentObserver);
        }
    }

    private class InnerSmsReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objects = (Object[])intent.getExtras().get("pdus");
            for (Object object : objects) {

                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
                String phone = smsMessage.getOriginatingAddress();
                String messageBody = smsMessage.getMessageBody();
                dao = BlackNumberDao.getInstance(getApplicationContext());
                int mode = dao.getMode(phone);
                // 1 短信  2 电话  3 所有
                if (mode == 1 || mode == 3){
                    abortBroadcast();
                }
            }
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyContentObserver extends ContentObserver {
        private String phone;
        public MyContentObserver(Handler handler,String phone) {
            super(handler);
            this.phone = phone;
        }
        //数据库中指定calls表发生改变的时候会去调用方法
        @Override
        public void onChange(boolean selfChange) {
            //插入一条数据后,再进行删除
            getContentResolver().delete(
                    Uri.parse("content://call_log/calls"), "number = ?", new String[]{phone});
            super.onChange(selfChange);
        }
    }
    @Override
    public void onDestroy() {
        //注销广播
        if(mInnerSmsReceiver!=null){
            unregisterReceiver(mInnerSmsReceiver);
        }

        //注销内容观察者
        if(mContentObserver!=null){
            getContentResolver().unregisterContentObserver(mContentObserver);
        }

        //取消对电话状态的监听
        if(myPhoneStateListener!=null){
            mTm.listen(myPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        }
        super.onDestroy();
    }

}

package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.engine.AddressDao;
import com.itheima.mobilesafe.utils.ConstantValue;
import com.itheima.mobilesafe.utils.SpUtil;

public class AddressService extends Service {


    public static final String tag = "AddressService";
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private View mViewToast;
    private WindowManager mWM;
    private String mAddress;
    private TextView tv_toast;

    private String TAG = "AddressService";
    private TelephonyManager mTm;
    private MYPhoneStateListener myPhoneStateListener;

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            tv_toast.setText(mAddress);
        };
    };
    private int[] mDrawableIds;
    @Override
    public void onCreate() {
        // 第一次开启服务,就需要管理吐司的显示
        mTm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MYPhoneStateListener();
        mTm.listen(myPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);

        Log.i(TAG,"服务开启");

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
                    showToast(phoneNumber);
                    break;
            }

            super.onCallStateChanged(state, phoneNumber);
        }
    }
    public void showToast(String incomingNumber){
        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE	默认能够被触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        //在响铃的时候显示吐司,和电话类型一致
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");

        //指定吐司的所在位置(将吐司指定在左上角)
        params.gravity = Gravity.LEFT+Gravity.TOP;

        //吐司显示效果(吐司布局文件),xml-->view(吐司),将吐司挂在到windowManager窗体上
        mViewToast = View.inflate(this, R.layout.toast_view, null);
        tv_toast = (TextView) mViewToast.findViewById(R.id.tv_toast);

        //从sp中获取色值文字的索引,匹配图片,用作展示
        mDrawableIds = new int[]{
                R.drawable.call_locate_white,
                R.drawable.call_locate_orange,
                R.drawable.call_locate_blue,
                R.drawable.call_locate_gray,
                R.drawable.call_locate_green};
        int toastStyleIndex = SpUtil.getInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);
        tv_toast.setBackgroundResource(mDrawableIds[toastStyleIndex]);

        //在窗体上挂在一个view(权限)
        mWM.addView(mViewToast, params);

        //获取到了来电号码以后,需要做来电号码查询
        query(incomingNumber);
    }
    private void query(final String incomingNumber) {
        new Thread(){
            public void run() {
                mAddress = AddressDao.getAddress(incomingNumber);
                mHandler.sendEmptyMessage(0);
            };
        }.start();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mTm != null && myPhoneStateListener != null){
            mTm.listen(myPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        }
        super.onDestroy();

    }
}

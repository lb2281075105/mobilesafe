package com.itheima.mobilesafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class LocationService extends Service {
    private String TAG = "LocationService";
    @Override
    public void onCreate() {
        super.onCreate();
        //1,获取位置管理者对象
        LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
        //2,以最优的方式获取经纬度坐标()
        Criteria criteria = new Criteria();
        //允许花费
        criteria.setCostAllowed(true);
        //指定获取经纬度的精确度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        Log.i(TAG,"前面");

        String bestProvider = lm.getBestProvider(criteria,true);

        //3,在一定时间间隔,移动一定距离后获取经纬度坐标
        MyLocationListener myLocationListener = new MyLocationListener();
        lm.requestLocationUpdates(bestProvider,0,0,myLocationListener);
    }
    class MyLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {

            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            Log.i(TAG,longitude+"");
//            //4,发送短信(添加权限)
//            SmsManager sms = SmsManager.getDefault();
//            sms.sendTextMessage("5556", null, "longitude = "+longitude+",latitude = "+latitude, null, null);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}

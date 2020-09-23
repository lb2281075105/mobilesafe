package com.itheima.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.itheima.mobilesafe.engine.ProcessInfoProvider;

public class LockScreenService extends Service {

    private ScreenOffReceiver screenOffReceiver;

    @Override
    public void onCreate() {

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);

        screenOffReceiver = new ScreenOffReceiver();
        registerReceiver(screenOffReceiver,intentFilter);

        super.onCreate();
    }

    class ScreenOffReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

            ProcessInfoProvider.killAll(getApplicationContext());
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (screenOffReceiver != null){
            unregisterReceiver(screenOffReceiver);
        }
        super.onDestroy();
    }
}

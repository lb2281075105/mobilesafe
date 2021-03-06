package com.itheima.mobilesafe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.AddressService;
import com.itheima.mobilesafe.service.BlackNumberService;
import com.itheima.mobilesafe.service.WatchDogService;
import com.itheima.mobilesafe.utils.ConstantValue;
import com.itheima.mobilesafe.utils.ServiceUtil;
import com.itheima.mobilesafe.utils.SpUtil;
import com.itheima.mobilesafe.view.SettingClickView;
import com.itheima.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {
    private int toast_style;
    private String[] mToastStyle;
    private SettingClickView settingClickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        // 自动更新设置
        initUpdate();

        // 电话归属地设置
        initAddress();

        // 吐司样式
        initToast();
        // 程序枷锁
        initAppLock();
    }

    private void initAppLock() {

        // 4.
        final SettingItemView chengView = findViewById(R.id.cheng_address);
        boolean isRunning = ServiceUtil.isRunning(getApplicationContext(), "com.itheima.mobilesafe.service.WatchDogService");

        chengView.setCheck(isRunning);
        chengView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ischeck = chengView.isCheck();
                chengView.setCheck(!ischeck);
                if (!ischeck) {
                    startService(new Intent(getApplicationContext(), WatchDogService.class));
                } else {
                    stopService(new Intent(getApplicationContext(), WatchDogService.class));
                }
            }
        });
    }

    public void initToast() {


        // 1.
        settingClickView = findViewById(R.id.scv_toast_style);
        settingClickView.setTitle("设置归属地显示风格");

        mToastStyle = new String[]{"透明", "橙色", "蓝色", "灰色", "绿色"};
        toast_style = SpUtil.getInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);
        settingClickView.setDes(mToastStyle[toast_style]);
        settingClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToastStyle();
            }
        });


        // 2.
        SettingClickView addressClickView = findViewById(R.id.address_toast_style);
        addressClickView.setTitle("归属地提示框位置");
        addressClickView.setDes("设置归属地提示框位置");
        addressClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ToastLocalActivity.class));
            }
        });
        // 3.
        final SettingItemView heiView = findViewById(R.id.hei_address);
        boolean isRunning = ServiceUtil.isRunning(getApplicationContext(), "com.itheima.mobilesafe.service.BlackNumberService");

        heiView.setCheck(isRunning);
        heiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ischeck = heiView.isCheck();
                heiView.setCheck(!ischeck);
                Log.i("SettingActivity", "ischeck" + !ischeck);
                if (!ischeck) {
                    startService(new Intent(getApplicationContext(), BlackNumberService.class));
                } else {
                    stopService(new Intent(getApplicationContext(), BlackNumberService.class));
                }
            }
        });


    }

    public void showToastStyle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.lock);
        builder.setTitle("请选择归属地样式");
        builder.setSingleChoiceItems(mToastStyle, toast_style, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                toast_style = i;
                Log.e("HHH", i + "");
                SpUtil.putInt(getApplicationContext(), ConstantValue.TOAST_STYLE, i);
                dialogInterface.dismiss();
                settingClickView.setDes(mToastStyle[i]);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    // 电话归属地设置
    public void initAddress() {

        final SettingItemView itemView = findViewById(R.id.siv_address);
        boolean isRunning = ServiceUtil.isRunning(getApplicationContext(), "com.itheima.mobilesafe.service.AddressService");
        itemView.setCheck(isRunning);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ischeck = itemView.isCheck();
                itemView.setCheck(!ischeck);
                Log.i("SettingActivity", "ischeck" + !ischeck);
                if (!ischeck) {
                    startService(new Intent(getApplicationContext(), AddressService.class));
                } else {
                    stopService(new Intent(getApplicationContext(), AddressService.class));
                }
            }
        });
    }

    // 自动更新设置
    public void initUpdate() {
        final SettingItemView itemView = findViewById(R.id.setting_item_view);
        boolean open_update = SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        itemView.setCheck(open_update);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ischeck = itemView.isCheck();
                itemView.setCheck(!ischeck);
                SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, !ischeck);
            }
        });
    }
}

package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.AddressService;
import com.itheima.mobilesafe.utils.ConstantValue;
import com.itheima.mobilesafe.utils.ServiceUtil;
import com.itheima.mobilesafe.utils.SpUtil;
import com.itheima.mobilesafe.view.SettingClickView;
import com.itheima.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {
    private String[] mToastStyle;
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
    }
    public void initToast(){

        SettingClickView settingClickView = findViewById(R.id.scv_toast_style);
        settingClickView.setTitle("设置归属地显示风格");

        mToastStyle = new String[]{"透明","橙色","蓝色","灰色","绿色"};
        int toast_style = SpUtil.getInt(getApplicationContext(),ConstantValue.TOAST_STYLE,0);
        settingClickView.setDes(mToastStyle[toast_style]);



        SettingClickView addressClickView = findViewById(R.id.address_toast_style);
        addressClickView.setTitle("归属地提示框位置");
        addressClickView.setDes("设置归属地提示框位置");

        final SettingItemView heiView = findViewById(R.id.hei_address);
        heiView.setCheck(true);


        final SettingItemView chengView = findViewById(R.id.cheng_address);
        chengView.setCheck(true);
    }
    // 电话归属地设置
    public void initAddress(){

        final SettingItemView itemView = findViewById(R.id.siv_address);
        boolean isRunning = ServiceUtil.isRunning(getApplicationContext(),"com.itheima.mobilesafe.service.AddressService");
        itemView.setCheck(isRunning);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ischeck = itemView.isCheck();
                itemView.setCheck(!ischeck);
                Log.i("SettingActivity","ischeck"+!ischeck);
                if (!ischeck){
                    startService(new Intent(getApplicationContext(), AddressService.class));
                }else {
                    stopService(new Intent(getApplicationContext(), AddressService.class));
                }
            }
        });
    }

    // 自动更新设置
    public void initUpdate(){
        final SettingItemView itemView = findViewById(R.id.setting_item_view);
        boolean open_update = SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE,false);
        itemView.setCheck(open_update);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ischeck = itemView.isCheck();
                itemView.setCheck(!ischeck);
                SpUtil.putBoolean(getApplicationContext(),ConstantValue.OPEN_UPDATE,!ischeck);
            }
        });
    }
}

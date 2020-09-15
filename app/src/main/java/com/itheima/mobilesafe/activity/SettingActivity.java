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
import com.itheima.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        // 自动更新设置
        initUpdate();

        // 电话归属地设置
        initAddress();
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

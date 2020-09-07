package com.itheima.mobilesafe.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.ConstantValue;
import com.itheima.mobilesafe.utils.SpUtil;
import com.itheima.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


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

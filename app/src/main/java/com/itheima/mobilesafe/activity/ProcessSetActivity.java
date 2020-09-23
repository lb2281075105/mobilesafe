package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.LockScreenService;
import com.itheima.mobilesafe.utils.ConstantValue;
import com.itheima.mobilesafe.utils.ServiceUtil;
import com.itheima.mobilesafe.utils.SpUtil;

public class ProcessSetActivity extends AppCompatActivity {

    private CheckBox cb_box1;
    private CheckBox cb_box2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_set);

        initShowSystem();
        initClear();
    }

    private void initClear() {
        cb_box2 = findViewById(R.id.cb_box2);
        boolean isRunning = ServiceUtil.isRunning(getApplicationContext(),"com.itheima.mobilesafe.service.LockScreenService");
        if (isRunning){
            cb_box2.setText("锁屏清理已开启");
        }else {
            cb_box2.setText("锁屏清理已关闭");
        }
        cb_box2.setChecked(isRunning);
        cb_box2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    startService(new Intent(getApplicationContext(), LockScreenService.class));
                    cb_box2.setText("锁屏清理已开启");
                }else {
                    stopService(new Intent(getApplicationContext(), LockScreenService.class));
                    cb_box2.setText("锁屏清理已关闭");
                }
            }
        });
    }

    private void initShowSystem() {
        cb_box1 = findViewById(R.id.cb_box1);
        boolean show_system = SpUtil.getBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM, false);
        if (show_system){
            cb_box1.setText("显示系统进程");
        }else {
            cb_box1.setText("隐藏系统进程");
        }
        cb_box1.setChecked(show_system);
        cb_box1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SpUtil.putBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM, b);

                if (b){
                    cb_box1.setText("显示系统进程");
                }else {
                    cb_box1.setText("隐藏系统进程");
                }
            }
        });
    }
}

package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.ConstantValue;
import com.itheima.mobilesafe.utils.SpUtil;
import com.itheima.mobilesafe.utils.ToastUtil;

public class Setup4Activity extends AppCompatActivity {
    private CheckBox checkBox;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        initUI();
    }

    public void initUI() {
        checkBox = findViewById(R.id.cb_box);

        final boolean open_security = SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, false);
        checkBox.setChecked(open_security);
        if (open_security) {
            checkBox.setText("安全设置已开启");
        } else {
            checkBox.setText("安全设置已关闭");
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkBox.setChecked(b);
                if (b) {
                    checkBox.setText("安全设置已开启");
                } else {
                    checkBox.setText("安全设置已关闭");
                }
                SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, b);
            }
        });
    }


    public void nextPage(View view){
        if (checkBox.isChecked()){
            Intent intent = new Intent(this,SetupOverActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
            SpUtil.putBoolean(this, ConstantValue.SETUP_OVER,true);
        }else {
            ToastUtil.show(getApplicationContext(),"请开启防盗保护");
        }

    }
    public void prePage(View view){
        Intent intent = new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);

    }
}

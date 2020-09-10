package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.ConstantValue;
import com.itheima.mobilesafe.utils.SpUtil;

public class SetupOverActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean setup_over = SpUtil.getBoolean(this, ConstantValue.SETUP_OVER,false);

        if (setup_over){
            setContentView(R.layout.activity_setup_over);
        }else {

            Intent intent = new Intent(this,Setup1Activity.class);
            startActivity(intent);
            finish();
        }

        initUI();
    }
    public void initUI(){
        // TextView ImageView 默认是没有点击事件的

        TextView tv_reset_bg = findViewById(R.id.tv_reset_bg);
        // 仅仅添加点击事件
        tv_reset_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Setup1Activity.class);
                startActivity(intent);
            }
        });
        TextView tv_phone = findViewById(R.id.tv_phone);
        ImageView iv_lock = findViewById(R.id.iv_lock);

        // 设置联系人电话
        String contact_phone = SpUtil.getString(getApplicationContext(),ConstantValue.CONTACT_PHONE,"");
        tv_phone.setText(contact_phone);
        boolean open_security = SpUtil.getBoolean(getApplicationContext(),ConstantValue.OPEN_SECURITY,false);

        if (open_security){
            iv_lock.setImageResource(R.drawable.lock);
        }else {
            iv_lock.setImageResource(R.drawable.unlock);
        }

    }
}

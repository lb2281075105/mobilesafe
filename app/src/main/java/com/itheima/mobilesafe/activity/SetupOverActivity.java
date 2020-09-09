package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;

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

//        if (setup_over){
//            setContentView(R.layout.activity_setup_over);
//        }else {

            Intent intent = new Intent(this,Setup1Activity.class);
            startActivity(intent);
            finish();
//        }

    }
}

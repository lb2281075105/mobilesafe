package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.ConstantValue;
import com.itheima.mobilesafe.utils.SpUtil;

public class Setup4Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);


    }
    public void nextPage(View view){
        Intent intent = new Intent(this,SetupOverActivity.class);
        startActivity(intent);
        finish();
        SpUtil.putBoolean(this, ConstantValue.SETUP_OVER,true);
    }
    public void prePage(View view){
        Intent intent = new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
    }
}

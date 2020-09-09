package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;

public class Setup3Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);


    }
    public void nextPage(View view){
        Intent intent = new Intent(this,Setup4Activity.class);
        startActivity(intent);
        finish();
    }
    public void prePage(View view){
        Intent intent = new Intent(this,Setup2Activity.class);
        startActivity(intent);
        finish();
    }
}

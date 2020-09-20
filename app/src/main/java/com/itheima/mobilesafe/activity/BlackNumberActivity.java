package com.itheima.mobilesafe.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.dao.BlackNumberDao;

public class BlackNumberActivity extends AppCompatActivity {
    private Button btn_add;
    private ListView lv_blacknumber;
    private BlackNumberDao blackNumberDao;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_number);

        initUI();
        initData();
    }

    private void initData() {
        new Thread(){
            @Override
            public void run() {

                blackNumberDao = BlackNumberDao.getInstance(getApplicationContext());

            }
        }.start();
    }

    private void initUI() {
        btn_add = findViewById(R.id.btn_add);
        lv_blacknumber = findViewById(R.id.lv_blacknumber);
    }
}

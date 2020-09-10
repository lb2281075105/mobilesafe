package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;


import androidx.annotation.Nullable;

import com.itheima.mobilesafe.R;

public class Setup1Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

    }

    @Override
    protected void showNextPage() {
        Intent intent = new Intent(this,Setup2Activity.class);
        startActivity(intent);
        finish();

        // 设置平移动画
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    @Override
    protected void showPrePage() {

    }

    public void nextPage(View view) {
        super.nextPage(view);
    }
}

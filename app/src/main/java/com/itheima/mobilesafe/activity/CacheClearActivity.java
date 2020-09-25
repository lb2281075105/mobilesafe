package com.itheima.mobilesafe.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;

public class CacheClearActivity extends AppCompatActivity {

    private MyAdapter myAdapter;
    private Button btn_liji;
    private ProgressBar pb_bar;
    private TextView tv_saomiao;
    private ListView lv_cache_clear;
    private Button btn_qingli;
    private Button btn_sd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_clear);

        initUI();
    }

    private void initUI() {
        // 立即清理
        btn_liji = findViewById(R.id.btn_liji);
        //进度条
        pb_bar = findViewById(R.id.pb_bar);
        // 扫描显示的文字
        tv_saomiao = findViewById(R.id.tv_saomiao);
        // listview
        lv_cache_clear = findViewById(R.id.lv_cache_clear);
//        缓存清理
        btn_qingli = findViewById(R.id.btn_qingli);
        // sd卡清理
        btn_sd = findViewById(R.id.btn_sd);





    }

    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }
}

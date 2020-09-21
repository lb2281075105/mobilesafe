package com.itheima.mobilesafe.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;

public class AppManagerActivity extends AppCompatActivity {

    private TextView tv_left;
    private TextView tv_right;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appmanager);
        initUI();
    }

    private void initUI() {
        tv_left = findViewById(R.id.tv_left);
        tv_right = findViewById(R.id.tv_right);

        listView = findViewById(R.id.lv_appmanager);
        //1,获取磁盘(内存,区分于手机运行内存)可用大小,磁盘路径
        String path = Environment.getDataDirectory().getAbsolutePath();
        //2,获取sd卡可用大小,sd卡路径
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        //3,获取以上两个路径下文件夹的可用大小
        String memoryAvailSpace = Formatter.formatFileSize(this, getAvailSpace(path));
        String sdMemoryAvailSpace = Formatter.formatFileSize(this,getAvailSpace(sdPath));


        tv_left.setText("磁盘可用:"+memoryAvailSpace);
        tv_right.setText("sd卡可用:"+sdMemoryAvailSpace);
    }

    //int代表多少个G
    /**
     * 返回值结果单位为byte = 8bit,最大结果为2147483647 bytes
     * @param path
     * @return	返回指定路径可用区域的byte类型值
     */
    private long getAvailSpace(String path) {
        //获取可用磁盘大小类
        StatFs statFs = new StatFs(path);
        //获取可用区块的个数
        long count = statFs.getAvailableBlocks();
        //获取区块的大小
        long size = statFs.getBlockSize();
        //区块大小*可用区块个数 == 可用空间大小
        return count*size;
//		Integer.MAX_VALUE	代表int类型数据的最大大小
//		0x7FFFFFFF
//
//		2147483647bytes/1024 =  2096128 KB
//		2096128KB/1024 = 2047	MB
//		2047MB = 2G
    }
}

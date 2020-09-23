package com.itheima.mobilesafe.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.engine.SmsBackUp;

import java.io.File;

public class AToolActivity extends AppCompatActivity {
    private TextView phone_address;
    private TextView tv_sms_backup;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool);

        initUI();

        initCommonNumberQuery();

        initChengXuSuo();
    }

    /**
     * 程序锁
     */
    private void initChengXuSuo() {
        TextView tv_chengxusuo = findViewById(R.id.tv_chengxusuo);
        tv_chengxusuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AppLockActivity.class));
            }
        });
    }

    private void initCommonNumberQuery() {
        TextView tv_some_phone = findViewById(R.id.tv_some_phone);
        tv_some_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CommonNumberQueryActivity.class));
            }
        });
    }

    public void initUI(){

        phone_address = findViewById(R.id.tv_query_phone_address);
        phone_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),QueryAddressActivity.class));
            }
        });

        tv_sms_backup = findViewById(R.id.tv_sms_backup);
        tv_sms_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    int hasReadSmsPermission = checkSelfPermission(Manifest.permission.READ_SMS);
                    if (hasReadSmsPermission != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_SMS}, REQUEST_CODE_ASK_PERMISSIONS);
                        return;
                    }
                }
                showSmsBackupDialog();
            }
        });
    }

    private void showSmsBackupDialog() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.drawable.home_apps);
        progressDialog.setTitle("短信备份");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();

        new Thread(){
            @Override
            public void run() {


//                String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "sms.xml";
                String path = "/data/data/com.itheima.mobilesafe/files/" + "sms.xml";

                SmsBackUp.backup(getApplicationContext(), path, new SmsBackUp.CallBack() {
                    @Override
                    public void setMax(int max) {
                        Log.e("HHH",max+"");
                        progressDialog.setMax(max);
                    }

                    @Override
                    public void setProgress(int index) {
                        progressDialog.setProgress(index);

                    }
                });
            }
        }.start();
    }
}

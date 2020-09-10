package com.itheima.mobilesafe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.ConstantValue;
import com.itheima.mobilesafe.utils.SpUtil;
import com.itheima.mobilesafe.utils.ToastUtil;
import com.itheima.mobilesafe.view.SettingItemView;

public class Setup2Activity extends AppCompatActivity {
    private SettingItemView siv_sim;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        initUI();
    }
    public void initUI(){

        siv_sim = findViewById(R.id.siv_sim_bound);
        String sim_number = SpUtil.getString(this, ConstantValue.SIM_NUMBER,"");

        if (TextUtils.isEmpty(sim_number)){
            siv_sim.setCheck(false);
        }else {
            siv_sim.setCheck(true);
        }
        siv_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ischeck = siv_sim.isCheck();
                siv_sim.setCheck(!ischeck);
                if (!ischeck){

                    TelephonyManager manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    String simNumber = "1010101010101010";
                    Log.i("simNumber","simNumber" + simNumber);
                    SpUtil.putString(getApplicationContext(),ConstantValue.SIM_NUMBER,simNumber);

                }else {
                    SpUtil.remove(getApplicationContext(),ConstantValue.SIM_NUMBER);
                }
            }
        });
    }
    public void nextPage(View view){

        if (!siv_sim.isCheck()){
            ToastUtil.show(getApplicationContext(),"请绑定SIM卡");
            return;
        }
        Intent intent = new Intent(this,Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }
    public void prePage(View view){
        Intent intent = new Intent(this,Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);

    }
}

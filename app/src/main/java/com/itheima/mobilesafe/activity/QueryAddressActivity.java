package com.itheima.mobilesafe.activity;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.engine.AddressDao;

public class QueryAddressActivity extends AppCompatActivity {
    private String TAG = "QueryAddressActivity";
    private EditText et_address;
    private TextView tv_address;
    private Button btn_address;
    private String mAddress;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.i(TAG,mAddress);
//            tv_address.setText((String)msg.obj);
            tv_address.setText(mAddress);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);

        et_address = findViewById(R.id.et_address);
        tv_address = findViewById(R.id.tv_address);
        btn_address = findViewById(R.id.btn_address);
        btn_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = et_address.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
                        // 插补器
//                    shake.setInterpolator(new Interpolator() {
//                        @Override
//                        public float getInterpolation(float v) {
//                            return 0;
//                        }
//                    });
                    findViewById(R.id.et_address).startAnimation(shake);

                    Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
//                    vibrator.vibrate(1000);
                    // 声音和抖动循环操作
                    vibrator.vibrate(new long[]{2000,3000,2000,3000},-1);
                }else {
                    query(phone);
                }
            }
        });
        et_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phone = et_address.getText().toString();
                query(phone);
            }
        });
    }
    public void query(final String phone){
        new Thread(){

            public void run() {
                mAddress = AddressDao.getAddress(phone);
                mHandler.sendEmptyMessage(0);
//                Message message = new Message();
//                message.obj = AddressDao.getAddress(phone);
//                mHandler.sendMessage(message);
            }
        }.start();
    }
}

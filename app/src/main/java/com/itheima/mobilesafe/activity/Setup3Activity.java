package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.ConstantValue;
import com.itheima.mobilesafe.utils.SpUtil;
import com.itheima.mobilesafe.utils.ToastUtil;

public class Setup3Activity extends BaseSetupActivity {
    private Button bt_select_number;
    private EditText et_phone_number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);



        initUI();
    }
    public void initUI(){


        bt_select_number = findViewById(R.id.bt_select_number);
        et_phone_number = findViewById(R.id.et_phone_number);

        String contact_phone = SpUtil.getString(this,ConstantValue.CONTACT_PHONE,"");
        et_phone_number.setText(contact_phone);

        bt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ContactListActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null ){
            et_phone_number.setText(data.getStringExtra("phone").replace(" ","").trim());
            SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE,et_phone_number.getText().toString());
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void showNextPage() {
        if (TextUtils.isEmpty(et_phone_number.getText().toString())){
            ToastUtil.show(getApplicationContext(),"请选择联系人电话");
            return;
        }
        SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE,et_phone_number.getText().toString());


        Intent intent = new Intent(this,Setup4Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    @Override
    protected void showPrePage() {
        Intent intent = new Intent(this,Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }
    @Override
    public void nextPage(View view) {
        super.nextPage(view);
    }

    @Override
    public void prePage(View view) {
        super.prePage(view);
    }
}

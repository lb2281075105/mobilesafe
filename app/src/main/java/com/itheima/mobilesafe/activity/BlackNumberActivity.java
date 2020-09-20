package com.itheima.mobilesafe.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.db.domain.BlackNumberInfo;
import com.itheima.mobilesafe.utils.ToastUtil;

import java.util.List;

public class BlackNumberActivity extends AppCompatActivity {
    private Button btn_add;
    private ListView lv_blacknumber;
    private BlackNumberDao blackNumberDao;
    private List<BlackNumberInfo> blackNumberInfos;
    private int mode = 1;
    private MyAdapter myAdapter;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            myAdapter = new MyAdapter();
//            blackNumberInfos = (List<BlackNumberInfo>) msg.obj;
            lv_blacknumber.setAdapter(myAdapter);
        }
    };
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
                blackNumberInfos = blackNumberDao.findAll();
                Message message = new Message();
                message.obj = blackNumberInfos;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    private void initUI() {
        btn_add = findViewById(R.id.btn_add);
        lv_blacknumber = findViewById(R.id.lv_blacknumber);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }
    public void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(getApplicationContext(), R.layout.dialog_add_blacknumber,null);
        alertDialog.setView(view,0,0,0,0);

        final EditText et_phone = view.findViewById(R.id.et_phone);
        RadioGroup rg_group = view.findViewById(R.id.rg_group);

        Button btn_confirm = view.findViewById(R.id.btn_confirm);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_sms:
                        mode = 1;
                        break;
                    case R.id.rb_phone:
                        mode = 2;
                        break;
                    case R.id.rb_all:
                        mode = 3;
                        break;

                }
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_phone.getText().toString())){
                    ToastUtil.show(getApplicationContext(),"请输入拦截电话");
                    return;
                }
                blackNumberDao.insert(et_phone.getText().toString(),mode + "");
                // initData();
                BlackNumberInfo numberInfo = new BlackNumberInfo();
                numberInfo.phone = et_phone.getText().toString();
                numberInfo.mode = mode + "";

                blackNumberInfos.add(0,numberInfo);

                if (myAdapter != null){
                    myAdapter.notifyDataSetChanged();
                }
                alertDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return blackNumberInfos.size();
        }

        @Override
        public BlackNumberInfo getItem(int i) {
            return blackNumberInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View item = View.inflate(getApplicationContext(), R.layout.blacknumber_item,null);

            BlackNumberInfo info = blackNumberInfos.get(i);
            TextView tv_phone = item.findViewById(R.id.tv_phone);
            TextView tv_mode = item.findViewById(R.id.tv_mode);
            ImageView btn_delete = item.findViewById(R.id.iv_delete);

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    blackNumberDao.delete(blackNumberInfos.get(i).getPhone());
                    blackNumberInfos.remove(i);
                    if (myAdapter != null){
                        myAdapter.notifyDataSetChanged();
                    }
                }
            });
            tv_phone.setText(info.getPhone());

            int mode = Integer.parseInt(info.getMode());
            switch (mode){
                case 1:
                    tv_mode.setText("拦截短信");
                    break;
                case 2:
                    tv_mode.setText("拦截电话");
                    break;
                case 3:
                    tv_mode.setText("拦截所有");
                    break;
            }

            return item;
        }
    }
}

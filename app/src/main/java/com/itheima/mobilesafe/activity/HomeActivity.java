package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.utils.ConstantValue;
import com.itheima.mobilesafe.utils.Md5Util;
import com.itheima.mobilesafe.utils.SpUtil;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private String[] mTitleStrs;
    private int[] mDrawableIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        GridView gridView = findViewById(R.id.gv_home);

        mTitleStrs = new String[]{
                "手机防盗","通信卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"
        };

        mDrawableIds = new int[]{
                R.drawable.home_safe,R.drawable.home_callmsgsafe,
                R.drawable.home_apps,R.drawable.home_taskmanager,
                R.drawable.home_netmanager,R.drawable.home_trojan,
                R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings
        };
        gridView.setAdapter(new MyAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i){
                    case 0:
                        showDialog();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        });
    }
    public void showDialog(){

       String mobile_safe_psd = SpUtil.getString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD,"");

       if (TextUtils.isEmpty(mobile_safe_psd)){
           // 1.设置初始密码对话框
           showSetPsdDialog();
       }else {
           // 2.确认密码对话框
           showConfirmPsdDialog();
       }

    }
    public void showSetPsdDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        final View topView = View.inflate(getApplicationContext(),R.layout.dialog_set_psd,null);

        alertDialog.setView(topView);
        alertDialog.show();

        Button bt_commit = topView.findViewById(R.id.bt_commit);
        Button bt_cancel = topView.findViewById(R.id.bt_cancel);

        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText et_set_psd = topView.findViewById(R.id.et_set_psd);
                EditText et_confirm_psd = topView.findViewById(R.id.et_confirm_psd);

                String psd = et_set_psd.getText().toString();
                String confirmPsd = et_confirm_psd.getText().toString();

                if (!TextUtils.isEmpty(psd) && !TextUtils.isEmpty(confirmPsd)) {

                    if (psd.equals(confirmPsd)){

                        Intent intent = new Intent(getApplicationContext(),TestActivity.class);
                        startActivity(intent);
                        alertDialog.dismiss();

                        SpUtil.putString(getApplicationContext(),ConstantValue.MOBILE_SAFE_PSD, Md5Util.encoder(confirmPsd));
                    }else {
                        Toast.makeText(getApplicationContext(),"请确认密码",0).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"请输入密码",0).show();
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    /**
     * 确认密码对话框
     */
    public void showConfirmPsdDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        final View topView = View.inflate(getApplicationContext(),R.layout.dialog_confirm_psd,null);

        alertDialog.setView(topView);
        alertDialog.show();

        Button bt_commit = topView.findViewById(R.id.bt_commit);
        Button bt_cancel = topView.findViewById(R.id.bt_cancel);

        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile_safe_psd = SpUtil.getString(getApplicationContext(),ConstantValue.MOBILE_SAFE_PSD,"");
                EditText et_confirm_psd = topView.findViewById(R.id.et_confirm_psd);
                String confirmPsd = et_confirm_psd.getText().toString();

                if (!TextUtils.isEmpty(confirmPsd)) {

                    if (mobile_safe_psd.equals(Md5Util.encoder(confirmPsd))){
                        Intent intent = new Intent(getApplicationContext(),TestActivity.class);
                        startActivity(intent);
                        alertDialog.dismiss();
                    }else {
                        Toast.makeText(getApplicationContext(),"请确认密码",0).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(),"请输入密码",0).show();
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mTitleStrs.length;
        }

        @Override
        public Object getItem(int i) {
            return mTitleStrs[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = View.inflate(getApplicationContext(),R.layout.gridview_item,null);
            TextView textView = view1.findViewById(R.id.item_tv);
            textView.setText(mTitleStrs[i]);
            ImageView imageView = view1.findViewById(R.id.item_imageView);
            imageView.setBackgroundResource(mDrawableIds[i]);

            return view1;
        }
    }
}

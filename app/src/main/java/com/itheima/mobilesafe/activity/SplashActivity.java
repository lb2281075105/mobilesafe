package com.itheima.mobilesafe.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;

import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    private String tag = "SplashActivity";
    private String description;

    private PackageInfo packageInfo;
    protected static final int UPDATE_VERSION = 100;
    protected static final int ENTER_HOME = 101;
    protected static final int USER_ERROR = 102;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case UPDATE_VERSION:
                    break;
                case ENTER_HOME:
//                    enterHome();
                    showUpdateDialog();

                    break;
                case USER_ERROR:
                    Toast.makeText(SplashActivity.this,"错误",0).show();
                    break;

            }

            Log.i(tag,"您好");
            Log.i(tag,msg.what + "");
        }
    };
    protected void showUpdateDialog(){

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.home_settings);
        builder.setTitle("版本更新");
        builder.setMessage(description);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enterHome();

            }
        });
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enterHome();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                enterHome();
                // 点击取消返回按钮也需要进入主界面
                dialogInterface.dismiss();;
            }
        });
        builder.show();
    }
    protected void enterHome(){

        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        // 在开启一个新的界面后,将导航界面关闭(导航界面只可见一次)
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        initUI();
        initAnimation();
    }

    /**
     * 添加淡入淡出动画
     */
    public void initAnimation(){
        Animation animation = new AlphaAnimation(0,1);
        animation.setDuration(3000);
        RelativeLayout rl_root = findViewById(R.id.rl_root);
        rl_root.startAnimation(animation);
    }
//    protected void installApk(File file) {
//        //系统应用界面,源码,安装apk入口
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//		/**
//         * 文件作为数据源
//		intent.setData(Uri.fromFile(file));
//		//设置安装的类型
//		intent.setType("application/vnd.android.package-archive");*/
//        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
////		startActivity(intent);
//        startActivityForResult(intent, 0);
//    }
//
//    //开启一个activity后,返回结果调用的方法
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        enterHome();
//        super.onActivityResult(requestCode, resultCode, data);
//    }
    /**
     * 初始化UI
     */
    public void initUI() {
        TextView tv_version_name = findViewById(R.id.tv_version_name);

        PackageManager pm = getPackageManager();
        try {
            packageInfo  = pm.getPackageInfo(this.getPackageName(),0);
            tv_version_name.setText("版本名称:" + packageInfo.versionName);

        }catch (Exception e){
            e.printStackTrace();
        }


        // 开辟子线程
        new Thread(){
            //                    Message message = new Message();

            Message message = Message.obtain();
            public void run() {
                long time = System.currentTimeMillis();
                String json = "{\n" +
                        "    \"version_name\": \"2.0\",\n" +
                        "    \"version_code\": 2,\n" +
                        "    \"description\": \"最新版手机卫士,快来下载体验吧!\",\n" +
                        "    \"download_url\": \"http://10.0.2.2:8080/mobilesafe2.0.apk\"\n" +
                        "\t}";
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String version_name = jsonObject.getString("version_name");
                    String version_code = jsonObject.getString("version_code");
                    description = jsonObject.getString("description");
                    String download_url = jsonObject.getString("download_url");

                    Log.i("version_name",version_name);
                    Log.i("version_code",version_code);
                    Log.i("description",description);
                    Log.i("download_url",download_url);


//                    if (Integer.parseInt(packageInfo.versionName) < Integer.parseInt(version_code)){
//                        message.what = UPDATE_VERSION;
//                    }else {
                        message.what = ENTER_HOME;
//                    }
                }catch (Exception e){
                    message.what = USER_ERROR;
                    e.printStackTrace();
                }finally {
                    long end_time = System.currentTimeMillis();
                    if (end_time - time < 4){
                         try {
                             Thread.sleep(4000 -(end_time - time));
                         }catch (Exception e){
                             e.printStackTrace();
                         }
                    }
                    handler.sendMessage(message);
                }

            }
        }.start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
    }


}
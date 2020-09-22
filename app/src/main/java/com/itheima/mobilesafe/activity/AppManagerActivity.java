package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.domain.AppInfo;
import com.itheima.mobilesafe.engine.AppInfoProvider;
import com.itheima.mobilesafe.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_left;
    private TextView tv_right;
    private ListView listView;
    private List<AppInfo> appInfoList;
    private MyAdapter myAdapter;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            myAdapter = new MyAdapter();
            listView.setAdapter(myAdapter);
            if (tv_des != null && mCustomList != null){
                tv_des.setText("用户应用("+ mCustomList.size() +")");
            }
        }
    };
    private List<AppInfo> mCustomList;
    private List<AppInfo> mSystomList;
    private TextView tv_des;
    private AppInfo appInfo;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appmanager);
        initUI();

        initData();
    }

    private void initData() {
        tv_des = findViewById(R.id.tv_des);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                if (mCustomList != null && mSystomList != null){
                    if (i >= mCustomList.size() + 1){
                        tv_des.setText("系统应用("+ mSystomList.size() +")");
                    }else {
                        tv_des.setText("用户应用("+ mCustomList.size() +")");
                    }
                }

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0 || i == mCustomList.size() + 1){
                    return;
                }else {
                    if (i < mCustomList.size() + 1){
                        appInfo = mCustomList.get(i - 1);
                    }else {
                        appInfo = mSystomList.get(i - mCustomList.size() - 2);
                    }
                    showPopupWindow(view);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        new Thread(){
            @Override
            public void run() {
                appInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());

                mSystomList = new ArrayList<AppInfo>();
                mCustomList = new ArrayList<AppInfo>();

                for (AppInfo appInfo : appInfoList){
                    if (appInfo.isSystem){
                        mSystomList.add(appInfo);
                    }else {
                        mCustomList.add(appInfo);
                    }
                }

                mHandler.sendEmptyMessage(0);

            }
        }.start();
        super.onResume();
    }

    private void showPopupWindow(View view) {
        View inflate = View.inflate(this, R.layout.popupwindow_layout, null);
        TextView btn_unstall = inflate.findViewById(R.id.btn_uninstall);
        TextView btn_qidong = inflate.findViewById(R.id.btn_qidong);
        TextView btn_share = inflate.findViewById(R.id.btn_share);

        btn_unstall.setOnClickListener( this);
        btn_qidong.setOnClickListener( this);
        btn_share.setOnClickListener( this);

        //透明动画(透明--->不透明)
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);

        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0, 1,
                0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        //动画集合Set
        AnimationSet animationSet = new AnimationSet(true);
        //添加两个动画
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);

        //1,创建窗体对象,指定宽高

        popupWindow = new PopupWindow(inflate, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,true);
//        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.function_greenbutton_normal));
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAsDropDown(view,120,-view.getHeight());
        inflate.startAnimation(animationSet);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_uninstall:

                if (appInfo.isSystem){
                    ToastUtil.show(getApplicationContext(),"此应用无法卸载");
                }else {
                    Intent intent = new Intent("android.intent.action.DELETE");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("package:"+appInfo.getPackageName()));
                    startActivity(intent);
                }
                break;
            case R.id.btn_qidong:

                PackageManager packageManager = getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(appInfo.getPackageName());
                if (intent != null){
                    startActivity(intent);
                }else {
                    ToastUtil.show(getApplicationContext(),"此应用无法开启");
                }
                break;
            case R.id.btn_share:
                Intent intentsend = new Intent(Intent.ACTION_SEND);
                intentsend.putExtra(Intent.EXTRA_TEXT,"分享一个应用,应用名称为"+appInfo.getName());
                intentsend.setType("text/plain");
                startActivity(intentsend);
                break;
        }
        //点击了窗体后消失窗体
        if(popupWindow!=null){
            popupWindow.dismiss();
        }
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

    private class MyAdapter extends BaseAdapter{
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == mCustomList.size() + 1){
                return 0;
            }else {
                return 1;
            }
        }

        @Override
        public int getCount() {
            return mCustomList.size() + mSystomList.size() + 2;
        }

        @Override
        public AppInfo getItem(int i) {
            if (i == 0 || i == mCustomList.size() + 1){
                return null;
            }else {
                if (i < mCustomList.size() + 1){
                    return mCustomList.get(i - 1);
                }else {
                    return mSystomList.get(i - mCustomList.size() - 2);
                }
            }

        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            int type = getItemViewType(i);
            if (type == 0){
                ViewTitleHolder viewHolder = null;
                if (view == null){
                    view = View.inflate(getApplicationContext(), R.layout.activity_appmanager_item_title,null);
                    viewHolder = new ViewTitleHolder();
                    viewHolder.tv_title = view.findViewById(R.id.tv_title);
                    view.setTag(viewHolder);
                }else {
                    viewHolder = (ViewTitleHolder) view.getTag();
                }
                if (i == 0){
                    viewHolder.tv_title.setText("用户应用("+ mCustomList.size()  +")");
                }else {
                    viewHolder.tv_title.setText("系统应用("+ mSystomList.size() +")");
                }

                return view;
            }else {
                ViewHolder viewHolder = null;
                if (view == null){
                    view = View.inflate(getApplicationContext(), R.layout.activity_appmanager_item,null);
                    viewHolder = new ViewHolder();
                    viewHolder.tv_top = view.findViewById(R.id.tv_top);
                    viewHolder.tv_bottom = view.findViewById(R.id.tv_bottom);
                    viewHolder.iv_icon = view.findViewById(R.id.iv_icon);
                    view.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                AppInfo appInfo = getItem(i);
                viewHolder.tv_top.setText(appInfo.name);
                viewHolder.tv_bottom.setText(appInfo.packageName);
                viewHolder.iv_icon.setImageDrawable(appInfo.icon);

                if (appInfo.isSdCard){
                    viewHolder.tv_bottom.setText("sd卡应用");
                }else {
                    viewHolder.tv_bottom.setText("手机应用");
                }
                return view;
            }



        }
    }
    // 静态的 不会创建多个对象
    static class ViewHolder {
        TextView tv_top;
        TextView tv_bottom;
        ImageView iv_icon;
        TextView tv_title;
    }
    // 静态的 不会创建多个对象
    static class ViewTitleHolder {
        TextView tv_title;
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

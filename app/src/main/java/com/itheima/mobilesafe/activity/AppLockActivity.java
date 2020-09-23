package com.itheima.mobilesafe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.dao.AppLockDao;
import com.itheima.mobilesafe.db.domain.AppInfo;
import com.itheima.mobilesafe.engine.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;

public class AppLockActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_unlock;
    private Button btn_lock;
    private ListView lv_unlock;
    private ListView lv_lock;

    private LinearLayout ll_unlock;
    private LinearLayout ll_lock;
    private List<AppInfo> appInfoList;
    private ArrayList<AppInfo> mUnlockList;
    private ArrayList<AppInfo> mlockList;
    private AppLockDao mDao;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {

            myUnlockAdapter = new MyAdapter(false);
            myLockAdapter = new MyAdapter(true);
            lv_unlock.setAdapter(myUnlockAdapter);
            lv_lock.setAdapter(myLockAdapter);


        }
    };
    private MyAdapter myUnlockAdapter;
    private MyAdapter myLockAdapter;

    private TextView tv_unlock;
    private TextView tv_lock;
    private TranslateAnimation mTranslateAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);

        initUI();
        initData();
        initAnimation();
    }

    /**
     * 初始化平移动画的方法(平移自身的一个宽度大小)
     */
    private void initAnimation() {
        mTranslateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        mTranslateAnimation.setDuration(500);
    }

    private void initData() {
        new Thread(){
            @Override
            public void run() {
                appInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
                mUnlockList = new ArrayList<>();
                mlockList = new ArrayList<>();


                //3.获取数据库中已加锁应用包名的的结合
                mDao = AppLockDao.getInstance(getApplicationContext());
                List<String> lockPackageList = mDao.findAll();
                for (AppInfo appInfo : appInfoList) {
                    //4,如果循环到的应用的包名,在数据库中,则说明是已加锁应用
                    if(lockPackageList.contains(appInfo.packageName)){
                        mlockList.add(appInfo);
                    }else{
                        mUnlockList.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        btn_unlock = findViewById(R.id.btn_unlock);
        btn_lock = findViewById(R.id.btn_lock);
        btn_lock.setOnClickListener(this);
        btn_unlock.setOnClickListener(this);

        // 未枷锁
        lv_unlock = findViewById(R.id.lv_unlock);
        // 已枷锁
        lv_lock = findViewById(R.id.lv_lock);

        // 未枷锁
        ll_unlock = findViewById(R.id.ll_unlock);
        // 已枷锁
        ll_lock = findViewById(R.id.ll_lock);

        tv_unlock = findViewById(R.id.tv_unlock);
        tv_lock = findViewById(R.id.tv_lock);
        btn_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
        btn_lock.setBackgroundResource(R.drawable.tab_left_default);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_unlock:
                ll_unlock.setVisibility(View.VISIBLE);
                ll_lock.setVisibility(View.GONE);
                btn_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
                btn_lock.setBackgroundResource(R.drawable.tab_right_default);
                myUnlockAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_lock:
                ll_lock.setVisibility(View.VISIBLE);
                ll_unlock.setVisibility(View.GONE);
                btn_unlock.setBackgroundResource(R.drawable.tab_left_default);
                btn_lock.setBackgroundResource(R.drawable.tab_right_pressed);
                myLockAdapter.notifyDataSetChanged();
                break;
        }
    }
    class MyAdapter extends BaseAdapter{

        private boolean isLock;
        public MyAdapter(boolean isLock) {
            this.isLock = isLock;
        }

        @Override
        public int getCount() {
            if (isLock){
                tv_lock.setText("已加锁应用:"+mlockList.size());

                return mlockList.size();
            }
            tv_unlock.setText("未加锁应用:"+mUnlockList.size());

            return mUnlockList.size();
        }

        @Override
        public AppInfo getItem(int i) {
            if (isLock){

                return mlockList.get(i);
            }

            return mUnlockList.get(i);

        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (viewHolder == null){
                view = View.inflate(getApplicationContext(), R.layout.app_lock_item,null);
                viewHolder = new ViewHolder();
                viewHolder.iv_image = view.findViewById(R.id.iv_image);
                viewHolder.tv_title = view.findViewById(R.id.tv_title);
                viewHolder.iv_lock = view.findViewById(R.id.iv_lock);
            }else {
                viewHolder = (ViewHolder) view.getTag();
            }
            final AppInfo appInfo = getItem(i);
            viewHolder.iv_image.setImageDrawable(getItem(i).icon);
            viewHolder.tv_title.setText(getItem(i).packageName);
            if (isLock){
                viewHolder.iv_lock.setBackgroundResource(R.drawable.lock);
            }else {
                viewHolder.iv_lock.setBackgroundResource(R.drawable.unlock);
            }
            final View animationView = view;

            viewHolder.iv_lock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //添加动画效果,动画默认是非阻塞的,所以执行动画的同时,动画以下的代码也会执行
                    animationView.startAnimation(mTranslateAnimation);//500毫秒
                    //对动画执行过程做事件监听,监听到动画执行完成后,再去移除集合中的数据,操作数据库,刷新界面
                    mTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            //动画开始的是调用方法
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            //动画重复时候调用方法
                        }
                        //动画执行结束后调用方法
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if(isLock){
                                //已加锁------>未加锁过程
                                //1.已加锁集合删除一个,未加锁集合添加一个,对象就是getItem方法获取的对象
                                mlockList.remove(appInfo);
                                mUnlockList.add(appInfo);
                                //2.从已加锁的数据库中删除一条数据
                                mDao.delete(appInfo.packageName);
                                //3.刷新数据适配器
                                myLockAdapter.notifyDataSetChanged();
                            }else{
                                //未加锁------>已加锁过程
                                //1.已加锁集合添加一个,未加锁集合移除一个,对象就是getItem方法获取的对象
                                mlockList.add(appInfo);
                                mUnlockList.remove(appInfo);
                                //2.从已加锁的数据库中插入一条数据
                                mDao.insert(appInfo.packageName);
                                //3.刷新数据适配器
                                myUnlockAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
            return view;
        }
    }
    static class ViewHolder{
        TextView tv_title;
        ImageView iv_image;
        ImageView iv_lock;
    }
}

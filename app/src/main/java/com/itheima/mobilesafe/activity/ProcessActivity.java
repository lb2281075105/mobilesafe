package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.domain.AppInfo;
import com.itheima.mobilesafe.db.domain.ProcessInfo;
import com.itheima.mobilesafe.engine.ProcessInfoProvider;
import com.itheima.mobilesafe.utils.ConstantValue;
import com.itheima.mobilesafe.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

public class ProcessActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv_user_process;
    private TextView tv_total;
    private TextView tv_shengyu;
    private TextView tv_user_process;
    private MyAdapter myAdapter;
    private List<ProcessInfo> processInfos;

    private ArrayList<ProcessInfo> mSystemList;
    private ArrayList<ProcessInfo> mCustomerList;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {

            myAdapter = new MyAdapter();
            lv_user_process.setAdapter(myAdapter);

            if (mCustomerList != null && mSystemList != null) {
                tv_user_process.setText("用户进程(" + mCustomerList.size() + ")");
            }
        }
    };
    private ProcessInfo processInfo;
    private int processCount;
    private long totalSpace;
    private long availSpace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        initUI();
        initData();
    }

    private void initData() {
        // 获取数据是耗时操作
        new Thread() {
            @Override
            public void run() {

                processInfos = ProcessInfoProvider.getProcessInfo(getApplicationContext());
                mSystemList = new ArrayList<ProcessInfo>();
                mCustomerList = new ArrayList<ProcessInfo>();

                for (ProcessInfo processInfo : processInfos) {
                    if (processInfo.isSystem) {
                        mSystemList.add(processInfo);
                    } else {
                        mCustomerList.add(processInfo);
                    }
                }

                mHandler.sendEmptyMessage(0);

            }
        }.start();

        lv_user_process.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0 || i == mCustomerList.size() + 1){
                    return;
                }else {
                    if (i < mCustomerList.size() + 1){
                        processInfo = mCustomerList.get(i - 1);
                    }else {
                        processInfo = mSystemList.get(i - mCustomerList.size() - 2);
                    }

                    if (processInfo != null){
                        if (!processInfo.getPackageName().equals(getPackageName())){
                            processInfo.isCheck = !processInfo.isCheck;
                            CheckBox cb_box = view.findViewById(R.id.cb_box);
                            cb_box.setChecked(processInfo.isCheck);
                        }
                    }
                }
            }
        });
        lv_user_process.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (mCustomerList != null && mSystemList != null) {
                    if (i >= mCustomerList.size() + 1) {
                        //滚动到了系统条目
                        tv_user_process.setText("系统进程(" + mSystemList.size() + ")");
                    } else {
                        //滚动到了用户应用条目
                        tv_user_process.setText("用户进程(" + mCustomerList.size() + ")");
                    }
                }
            }
        });
    }

    private void initUI() {
        lv_user_process = findViewById(R.id.lv_user_process);
        tv_total = findViewById(R.id.tv_total);
        tv_shengyu = findViewById(R.id.tv_shengyu);

        processCount = ProcessInfoProvider.getProcessCount(this);
        tv_total.setText("进程总数(" + processCount + ")");

        totalSpace = ProcessInfoProvider.getTotalSpace(this);
        availSpace = ProcessInfoProvider.getAvailSpace(this);
        tv_shengyu.setText("剩余/总共:" + Formatter.formatFileSize(this, totalSpace) + "/" + Formatter.formatFileSize(this, availSpace) + "");

        tv_user_process = findViewById(R.id.tv_user_process);


        Button btn_all = findViewById(R.id.btn_all);
        Button btn_reserve = findViewById(R.id.btn_reverse);
        Button btn_clear = findViewById(R.id.btn_clear);
        Button btn_set = findViewById(R.id.btn_set);

        btn_all.setOnClickListener(this);
        btn_reserve.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_set.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_all:
                selectAll();
                break;
            case R.id.btn_reverse:
                reverse();
                break;
            case R.id.btn_clear:
                clear();
                break;
            case R.id.btn_set:
                set();
                break;
        }
    }

    private void set() {
        startActivityForResult(new Intent(getApplicationContext(),ProcessSetActivity.class),0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (myAdapter != null){
            myAdapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void clear() {

        List<ProcessInfo> killProcessInfos = new ArrayList<>();
        for (ProcessInfo processInfo : mCustomerList) {
            if (processInfo.getPackageName().equals(getPackageName())) {
                continue;
            }
            if (processInfo.isCheck){
                killProcessInfos.add(processInfo);
            }
        }

        for (ProcessInfo processInfo : mSystemList) {
            if (processInfo.isCheck){
                killProcessInfos.remove(processInfo);
            }
        }

        long total_size = 0;
        for (ProcessInfo processInfo : killProcessInfos) {
            if (processInfo.isSystem){
                mSystemList.remove(processInfo);
            }else {
                mCustomerList.remove(processInfo);
            }
            total_size += processInfo.memSize;
            ProcessInfoProvider.killProcess(getApplicationContext(),processInfo);
        }

        tv_total.setText("进程总数(" + (processCount - killProcessInfos.size()) + ")");
        tv_shengyu.setText("剩余/总共:" + Formatter.formatFileSize(this, totalSpace) + "/" + Formatter.formatFileSize(this, availSpace + total_size) + "");


        if (myAdapter != null){
            myAdapter.notifyDataSetChanged();
        }
    }

    private void reverse() {
        for (ProcessInfo processInfo : mCustomerList) {
            if (processInfo.getPackageName().equals(getPackageName())) {
                continue;
            } else {
                processInfo.isCheck = !processInfo.isCheck ;
            }
        }
        for (ProcessInfo processInfo : mSystemList) {
            processInfo.isCheck = !processInfo.isCheck ;
        }
        if (myAdapter != null){
            myAdapter.notifyDataSetChanged();
        }
    }

    private void selectAll() {
        for (ProcessInfo processInfo : mCustomerList) {
            if (processInfo.getPackageName().equals(getPackageName())) {
                continue;
            } else {
                processInfo.isCheck = true;
            }
        }
        for (ProcessInfo processInfo : mSystemList) {
             processInfo.isCheck = true;
        }
        if (myAdapter != null){
            myAdapter.notifyDataSetChanged();
        }

    }

    public class MyAdapter extends BaseAdapter {
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        @Override
        public int getItemViewType(int position) {

            if (position == 0 || position == mCustomerList.size() + 1) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int getCount() {
            boolean show_system = SpUtil.getBoolean(getApplicationContext(), ConstantValue.SHOW_SYSTEM, false);

            if (show_system){
                return mCustomerList.size() + mSystemList.size() + 2;
            }else {
                return mCustomerList.size() + 1;
            }
        }

        @Override
        public ProcessInfo getItem(int i) {
            if (i == 0 || i == mCustomerList.size() + 1) {
                return null;
            } else {
                if (i < mCustomerList.size() + 1) {
                    return mCustomerList.get(i - 1);
                } else {
                    return mSystemList.get(i - mCustomerList.size() - 2);
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

            if (type == 0) {
                ViewTitleHolder viewHolder = null;
                if (view == null) {
                    view = View.inflate(getApplicationContext(), R.layout.activity_process_item_title, null);
                    viewHolder = new ViewTitleHolder();
                    viewHolder.tv_title = view.findViewById(R.id.tv_title);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewTitleHolder) view.getTag();
                }

                if (i == 0) {
                    viewHolder.tv_title.setText("用户进程(" + mCustomerList.size() + ")");
                } else {
                    viewHolder.tv_title.setText("系统进程(" + mSystemList.size() + ")");
                }
                return view;

            } else {
                //展示图片+文字条目
                ViewHolder holder = null;
                if (view == null) {
                    view = View.inflate(getApplicationContext(), R.layout.activity_process_item, null);
                    holder = new ViewHolder();
                    holder.iv_icon = view.findViewById(R.id.iv_icon);
                    holder.tv_top = view.findViewById(R.id.tv_top);
                    holder.tv_bottom = view.findViewById(R.id.tv_bottom);
                    holder.cb_box = view.findViewById(R.id.cb_box);
                    view.setTag(holder);
                } else {
                    holder = (ViewHolder) view.getTag();
                }
                holder.iv_icon.setBackgroundDrawable(getItem(i).icon);
                holder.tv_top.setText(getItem(i).name);
                String strSize = Formatter.formatFileSize(getApplicationContext(), getItem(i).memSize);
                holder.tv_bottom.setText(strSize);

                if (getItem(i).getPackageName().equals(getPackageName())) {
                    holder.cb_box.setVisibility(View.GONE);
                } else {
                    holder.cb_box.setVisibility(View.VISIBLE);
                }
                holder.cb_box.setChecked(getItem(i).isCheck);
                return view;

            }


        }

    }

    static class ViewTitleHolder {
        TextView tv_title;
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_top;
        TextView tv_bottom;
        CheckBox cb_box;
    }
}

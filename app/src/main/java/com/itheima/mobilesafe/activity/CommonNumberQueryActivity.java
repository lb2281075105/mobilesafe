package com.itheima.mobilesafe.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.engine.CommonnumDao;

import java.util.List;

public class CommonNumberQueryActivity extends AppCompatActivity {

    private ExpandableListView lv_common_number_query;

    private MyAdapter myAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            lv_common_number_query.setAdapter(new MyAdapter());
        }
    };
    private CommonnumDao commonnumDao;
    private List<CommonnumDao.Group> daoGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_number_query);

        initUI();
        initData();
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {

                commonnumDao = new CommonnumDao();
                daoGroup = commonnumDao.getGroup();
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        lv_common_number_query = findViewById(R.id.lv_common_number_query);
        lv_common_number_query.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                startCall(daoGroup.get(i).childList.get(i1).number);
                return false;
            }
        });
    }

    private void startCall(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
////             here to request the missing permissions, and then overriding
////               public void onRequestPermissionsResult(int requestCode, String[] permissions,
////                                                      int[] grantResults)
////             to handle the case where the user grants the permission. See the documentation
////             for ActivityCompat#requestPermissions for more details.
//            ActivityCompat.requestPermissions(getApplicationContext(),new String[]{Manifest.permission.CALL_PHONE},cal);
//            Log.e("HHH",phone);
//            return;
//        }
        startActivity(intent);

    }

    class MyAdapter extends BaseExpandableListAdapter {
        @Override
        public int getGroupCount() {
            return daoGroup.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return i;
        }

        @Override
        public CommonnumDao.Group getGroup(int i) {
            return daoGroup.get(i);
        }

        @Override
        public CommonnumDao.Child getChild(int i, int i1) {
            return daoGroup.get(i).childList.get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

            ViewGroupHolder holder = null;
            if (view == null){
                view = View.inflate(getApplicationContext(),R.layout.common_number_title,null);
                holder = new ViewGroupHolder();
                holder.iv_jiantou = view.findViewById(R.id.iv_jiantou);
                holder.tv_title = view.findViewById(R.id.tv_title);
                view.setTag(holder);
            }else {
                holder = (ViewGroupHolder) view.getTag();
            }
            holder.tv_title.setText(getGroup(i).name);

            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            ViewChildHolder holder = null;
            if (view == null){
                view = View.inflate(getApplicationContext(),R.layout.common_number_item,null);
                holder = new ViewChildHolder();
                holder.tv_top = view.findViewById(R.id.tv_top);
                holder.tv_bottom = view.findViewById(R.id.tv_bottom);
                view.setTag(holder);
            }else {
                holder = (ViewChildHolder) view.getTag();
            }
            holder.tv_top.setText(getChild(i,i1).name);
            holder.tv_bottom.setText(getChild(i,i1).number);

            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
    static class ViewGroupHolder{
        public ImageView iv_jiantou;
        public TextView tv_title;
    }
    static class ViewChildHolder{
        public TextView tv_top;
        public TextView tv_bottom;
    }
}

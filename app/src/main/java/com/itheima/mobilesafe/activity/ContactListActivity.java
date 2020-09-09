package com.itheima.mobilesafe.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {
    private String tag = "ContactListActivity";
    private ListView listView;
    private MyAdapter myAdapter;
    final private List<HashMap<String,String>> contactList = new ArrayList<>();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            myAdapter = new MyAdapter();
            //8,填充数据适配器
            listView.setAdapter(myAdapter);

            Log.i(tag,contactList.toString());
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        listView = findViewById(R.id.lv_contact);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,String> contactMap = myAdapter.getItem(i);
                String phone = contactMap.get("phone");
                Intent intent = new Intent();
                intent.putExtra("phone",phone);
                setResult(0,intent);
                finish();
            }
        });
        initData();
    }

    public class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public HashMap<String, String> getItem(int i) {
            return contactList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View item = View.inflate(getApplicationContext(), R.layout.activity_contact_item,null);
            TextView tv_title = item.findViewById(R.id.tv_title);
            TextView tv_phone = item.findViewById(R.id.tv_phone);
            tv_title.setText(getItem(i).get("name"));
            tv_phone.setText(getItem(i).get("phone"));

            return item;
        }
    }
    public void initData(){
        /**
         * 联系人获取耗时操作
         */
        new Thread(){

            public void run() {
                //1,获取内容解析器对象
                ContentResolver contentResolver = getContentResolver();
                //2,做查询系统联系人数据库表过程(读取联系人权限)
                Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"},
                        null, null, null);
                contactList.clear();

                // 循环游标,直到没有数据为止
                while (cursor.moveToNext()){

                    String id = cursor.getString(0);
                    if (id == null){
                        continue;
                    }
                    //4,根据用户唯一性id值,查询data表和mimetype表生成的视图,获取data以及mimetype字段
                    Cursor indexCursor = contentResolver.query(
                            Uri.parse("content://com.android.contacts/data"),
                            new String[]{"data1","mimetype"},
                            "raw_contact_id = ?", new String[]{id}, null);
                    //5,循环获取每一个联系人的电话号码以及姓名,数据类型
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    while(indexCursor.moveToNext()){
                        String data = indexCursor.getString(0);
                        String type = indexCursor.getString(1);

                        //6,区分类型去给hashMap填充数据
                        if(type.equals("vnd.android.cursor.item/phone_v2")){
                            //数据非空判断
                            if(!TextUtils.isEmpty(data)){
                                hashMap.put("phone", data);
                            }
                        }else if(type.equals("vnd.android.cursor.item/name")){
                            if(!TextUtils.isEmpty(data)){
                                hashMap.put("name", data);
                            }
                        }
                    }
                    indexCursor.close();
                    contactList.add(hashMap);
                }
                cursor.close();
                //7,消息机制,发送一个空的消息,告知主线程可以去使用子线程已经填充好的数据集合
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }
}

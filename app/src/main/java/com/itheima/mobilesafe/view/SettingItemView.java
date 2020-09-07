package com.itheima.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobilesafe.R;

public class SettingItemView extends RelativeLayout {
    public TextView tv_title;
    public TextView tv_des;
    public CheckBox cb_box;
    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.setting_item_view, this);
        tv_title = findViewById(R.id.tv_title);
        tv_des = findViewById(R.id.tv_des);
        cb_box = findViewById(R.id.cb_box);

    }

    public boolean isCheck(){
        return cb_box.isChecked();
    }

    public void setCheck(boolean check){
        cb_box.setChecked(check);
        if (check){
            tv_des.setText("自动更新已开启");
        }else {
            tv_des.setText("自动更新已关闭");
        }
    }
}

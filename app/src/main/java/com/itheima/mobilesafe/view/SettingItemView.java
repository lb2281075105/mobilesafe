package com.itheima.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobilesafe.R;

public class SettingItemView extends RelativeLayout {

    private String NAMESPACE = "http://schemas.android.com/apk/res/com.itheima.mobilesafe";

    public TextView tv_title;
    public TextView tv_des;
    public CheckBox cb_box;
    public String destitle;
    public String desoff;
    public String deson;


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

        initAttr(attrs);
    }
    public void initAttr(AttributeSet attrs){

        destitle = attrs.getAttributeValue(NAMESPACE,"destitle");
        desoff = attrs.getAttributeValue(NAMESPACE,"desoff");
        deson = attrs.getAttributeValue(NAMESPACE,"deson");

        tv_title.setText(destitle);
    }

    public boolean isCheck(){
        return cb_box.isChecked();
    }

    public void setCheck(boolean check){
        cb_box.setChecked(check);
        if (check){
            tv_des.setText(deson);
        }else {
            tv_des.setText(desoff);
        }
    }
}

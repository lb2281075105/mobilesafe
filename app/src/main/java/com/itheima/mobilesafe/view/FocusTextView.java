package com.itheima.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * 能够获取焦点的自定义的TextView
 */
public class FocusTextView extends TextView {
    // 通过Java代码创建控件 new时候
    public FocusTextView(Context context) {
        super(context);
    }
    // 由系统调用(带属性+上下文环境构造方法) xml转化成Java对象的时候调用
    public FocusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    // 由系统调用(带属性+上下文环境构造方法+布局文件中定义样式文件构造方法) xml转化成Java对象的时候调用
    public FocusTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写获取焦点的方法.由系统调用,调用的时候默认就能获取焦点
     * @return
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}

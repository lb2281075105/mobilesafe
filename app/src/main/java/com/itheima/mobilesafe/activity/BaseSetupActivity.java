package com.itheima.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itheima.mobilesafe.R;


public abstract class BaseSetupActivity extends AppCompatActivity {
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initGesTureDetector();
    }
    public void initGesTureDetector(){
        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
            /**
             *
             * @param e1
             * @param e2
             * @param velocityX x轴的移动速度
             * @param velocityY y轴的移动速度
             * @return
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


                // 右到左
                if (e1.getX() - e2.getX() > 0){

                    showNextPage();
                }
                // 左到右
                if (e1.getX() - e2.getX() < 0){
                    showPrePage();
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }
    protected abstract void showNextPage();
    protected abstract void showPrePage();



    // 监听屏幕上响应的事件类型(按下(1次),移动(多次),抬起(1次))
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //通过手势类,通过接受多种类型的事件,用做处理
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    public void nextPage(View view){
        showNextPage();
    }
    public void prePage(View view){
        showPrePage();
    }

}

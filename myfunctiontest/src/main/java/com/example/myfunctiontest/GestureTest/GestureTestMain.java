package com.example.myfunctiontest.GestureTest;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.myfunctiontest.R;

/**
 * author: Administrator
 * created on: 2016/12/8 9:46
 * description:
 */

public class GestureTestMain extends Activity implements GestureDetector.OnGestureListener{


    // 定义手势检测器实例
    GestureDetector detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesturetestmain);
        //创建手势检测器
        detector = new GestureDetector(this, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Toast.makeText(this,"onDown"
                , Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

        Toast.makeText(this ,"onShowPress"
                , Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Toast.makeText(this ,"onSingleTapUp"
                , Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Toast.makeText(this ,"onScroll" ,
                Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

        Toast.makeText(this ,"onLongPress"
                , Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Toast.makeText(this , "onFling"
                , Toast.LENGTH_SHORT).show();
        return false;
    }
}

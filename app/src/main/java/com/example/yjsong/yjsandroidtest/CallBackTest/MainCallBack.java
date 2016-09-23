package com.example.yjsong.yjsandroidtest.CallBackTest;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.yjsong.yjsandroidtest.R;

/**
 * Created by YJSONG on 2016/9/19.
 */
public class MainCallBack extends AppCompatActivity implements ChangeTitle{

    private TextView textView;
    private static final String TAG="MainCallBack..";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callbackxml);
        textView = (TextView) findViewById(R.id.textview);
        new MyTask(this).execute("我是标题");
    }


    @Override
    public void onChangeTitle(String title) {
        textView.setText(title);
    }
}

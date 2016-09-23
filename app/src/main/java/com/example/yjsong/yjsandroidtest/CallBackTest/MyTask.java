package com.example.yjsong.yjsandroidtest.CallBackTest;

import android.os.AsyncTask;

/**
 * Created by YJSONG on 2016/9/19.
 */
public class MyTask extends AsyncTask<String,Void,String>{
    private ChangeTitle changeTitle;
    public MyTask(ChangeTitle changeTitle) {
        this.changeTitle = changeTitle;
    }
    @Override
    protected String doInBackground(String... strings) {
        if (strings[0]!=null){
            changeTitle.onChangeTitle(strings[0]);
        }
        return null;
    }
}

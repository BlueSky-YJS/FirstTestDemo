package com.example.myfunctiontest.LoginCallbackDemo;

import android.content.Context;

/**
 * author: Administrator
 * created on: 2016/12/14 16:37
 * description:
 */

public class userrequest {

    private Context context;
    public userrequest(Context context){
        this.context = context;
    }

    public void login(String name,String pwd,loginListener listener){

        if (NetWorkJudge.isNetworkConnected(context)){//网络连接可用
            if (name.equals("123456")&&pwd.equals("123456")){
                listener.loginsuccess();
            }else{
                listener.loginerr();
            }
        }else{
            listener.disconnect();
        }
    }

    public interface loginListener{
        void loginsuccess();
        void loginerr();
        void disconnect();
    }
}

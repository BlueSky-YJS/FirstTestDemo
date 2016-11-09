package com.example.myfunctiontest.CommunicationTest;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * author: Administrator
 * created on: 2016/11/9 14:57
 * description:
 */

public class ServiceServer extends Service {
    private static final String TAG = "MYTAG";
    private static final int TIME = 1;
    static final int TEST = 0;
    private int i = 0;
    private Timer mTimer = null;
    private Messenger mMessenger;//It's the messenger of server
    private Messenger cMessenger = null;//It's the messenger of client

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TEST:
                    Log.e(TAG, "Get Message from MainActivity.");
                    cMessenger = msg.replyTo;
                    mTimer.schedule(new MyTimerTask(), 1000,TIME * 1000);
                    break;
                default:
                    break;
            }
        }
    };

    public ServiceServer() {
        mMessenger = new Messenger(mHandler);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "MessengerService.onCreate()...pid: "+ Process.myPid());
        mTimer = new Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "MessengerService.onBind()...");
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "MessengerService.onDestroy()...");
        if(mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            if (i == 100) {
                i = 0;
            }
            try {
                //send the message to the client
                Message message = Message.obtain(null, ServiceServer.TEST,i, 0);
                cMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            i++;
        }
    }

}

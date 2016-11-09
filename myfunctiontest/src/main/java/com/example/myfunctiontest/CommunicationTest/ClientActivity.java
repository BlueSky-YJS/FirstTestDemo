package com.example.myfunctiontest.CommunicationTest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myfunctiontest.R;

/**
 * author: Administrator
 * created on: 2016/11/9 14:55
 * description:
 */

public class ClientActivity extends AppCompatActivity {

    private static final String TAG = "MYTAG";
    private boolean mIsBind;
    private Button startBtn = null;
    private Button stopBtn = null;
    private TextView mTextView = null;
    private Messenger rMessenger = null;
    private Messenger mMessenger = null;
    private Intent intent = null;
    private ProgressBar mProgressBar = null;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ServiceServer.TEST:
                    Log.e(TAG, "Get Message From MessengerService. i= "+msg.arg1);
                    int curLoad = msg.arg1;
                    mTextView.setText(curLoad+"%");
                    mProgressBar.setProgress(curLoad);
                    break;

                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clienttoserverlayout);
        intent = new Intent(ClientActivity.this,ServiceServer.class);
        mProgressBar = (ProgressBar)findViewById(R.id.myProgressBar);
        mProgressBar.setMax(100);
        mTextView = (TextView)findViewById(R.id.loading_Tv);
        startBtn = (Button)findViewById(R.id.start_Btn);
        stopBtn = (Button)findViewById(R.id.stop_Btn);
        startBtn.setOnClickListener(new ButtonClickListener());
        stopBtn.setOnClickListener(new ButtonClickListener());
    }

    class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (startBtn == v) {
                Log.i(TAG, "Start Button Clicked.pid: "+ Process.myPid());
                doBindService();
            } else if (stopBtn == v) {
                Log.i(TAG, "Send Button Clicked.");
                doUnBindService();
            }
        }
    }

    private ServiceConnection serConn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected()...");
            rMessenger = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected()...");
            rMessenger = new Messenger(service);//get the object of remote service
            mMessenger = new Messenger(mHandler);//initial the object of local service
            sendMessage();
        }
    };

    private void doBindService(){
        Log.i(TAG, "doBindService()...");
        mIsBind = bindService(intent, serConn, BIND_AUTO_CREATE);//if bind success return true
        Log.e(TAG, "Is bind: "+mIsBind);
    }

    private void doUnBindService(){
        if(mIsBind){
            Log.i(TAG, "doUnBindService()...");
            unbindService(serConn);
            mIsBind = false;
        }
    }

    private void sendMessage() {
        Message msg = Message.obtain(null, ServiceServer.TEST);//ServiceServer.TEST=0
        msg.replyTo = mMessenger;
        try {
            rMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}

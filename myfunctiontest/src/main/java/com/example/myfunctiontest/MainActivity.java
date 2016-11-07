package com.example.myfunctiontest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myfunctiontest.UpdateProgressTest.ProgressInterface;
import com.example.myfunctiontest.UpdateProgressTest.UpdateService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class MainActivity extends AppCompatActivity implements NetWorkCallbackInterface{
    Button start;
    TextView tv;
    ExecutorService pool;
    private ProgressBar updateBar;
    private Button btnDown,btnPause;
    public static final String APK_DOWNLOAD_URL = "http://101.200.195.22:8080/appupdate/weichai.apk";
    private UpdateService myservice;
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myservice=((UpdateService.myBinder)service).getservice();
            myservice.setProgressinterface(new ProgressInterface() {
                @Override
                public void updateProgress(int progress) {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.arg1 = progress;
                    updateHandler.sendMessage(msg);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateBar = (ProgressBar) findViewById(R.id.progressBar2);
        btnDown = (Button) findViewById(R.id.button);
        btnPause = (Button) findViewById(R.id.button2);
        btnDown.setOnClickListener(new MyClickListener());
        btnPause.setOnClickListener(new MyPauseClick());
        pool= Executors.newSingleThreadExecutor();
        init();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if(msg.what==1){
                tv.setText(msg.obj.toString());
            }
        }
    };

    @Override
    public void showCallback(String result) {
        Message msg=new Message();
        msg.what=1;
        msg.obj=result;
        handler.sendMessage(msg);
    }
    private void init() {
        start=(Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HttpUtil util=new HttpUtil(MainActivity.this);
                pool.execute(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        util.GetNow();
                    }
                }));
            }
        });
        tv=(TextView) findViewById(R.id.display_result);
    }
        Handler updateHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1){
                    if(msg.arg1==UpdateService.pauseValue){
                        tv.setText("暂停");
                    }
                    updateBar.setProgress(msg.arg1);
                    tv.setText(msg.arg1+"--");
                }
            }
        };

        public class MyClickListener implements View.OnClickListener{
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, UpdateService.class);
                //下载地址
                it.putExtra("apkUrl", APK_DOWNLOAD_URL);
                bindService(it,conn, BIND_AUTO_CREATE);
                //startService(it);
            }
        }

    public class MyPauseClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            unbindService(conn);

        }
    }

    @Override
    protected void onStop() {
        unbindService(conn);
        super.onStop();
    }
}

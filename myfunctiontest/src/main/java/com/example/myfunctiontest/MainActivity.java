package com.example.myfunctiontest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
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

import com.example.myfunctiontest.AutoFitTextview.AutoFitTextviewMian;
import com.example.myfunctiontest.CallbackDemo.CallbackDemoMian;
import com.example.myfunctiontest.CallbackDemo.GetImageMain;
import com.example.myfunctiontest.CommunicationTest.ClientActivity;
import com.example.myfunctiontest.Compass.Compasstest;
import com.example.myfunctiontest.FileTest.FileTestMain;
import com.example.myfunctiontest.GestureTest.GestureTestMain;
import com.example.myfunctiontest.LoginCallbackDemo.LoginActivity;
import com.example.myfunctiontest.MultithreadDownload.MultithreadDownMain;
import com.example.myfunctiontest.ParserExcel.ExcelParserMain;
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
    private Button testServer;
    private  Button parseExcel;
    private Button mutiDown;
    private Button filetestbtn;
    private Button gestureBtn;
    private Button autofittextviewBtn;
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
        mutiDown = (Button) findViewById(R.id.mutlidown);
        mutiDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mutliIntent = new Intent(MainActivity.this, MultithreadDownMain.class);
                startActivity(mutliIntent);
            }
        });
        updateBar = (ProgressBar) findViewById(R.id.progressBar2);
        btnDown = (Button) findViewById(R.id.button);
        btnPause = (Button) findViewById(R.id.button2);
        btnDown.setOnClickListener(new MyClickListener());
        btnPause.setOnClickListener(new MyPauseClick());
        parseExcel = (Button) findViewById(R.id.parseExcel);
        parseExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent parseIntent = new Intent(MainActivity.this, ExcelParserMain.class);
                startActivity(parseIntent);
            }
        });
        testServer = (Button) findViewById(R.id.testService);
        testServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serIntent = new Intent(MainActivity.this, ClientActivity.class);
                startActivity(serIntent);
            }
        });
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

        filetestbtn = (Button) findViewById(R.id.button4);
        filetestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileIntent = new Intent(MainActivity.this, FileTestMain.class);
                startActivity(fileIntent);
            }
        });

        gestureBtn = (Button) findViewById(R.id.button5);
        gestureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gesIntent = new Intent(MainActivity.this, GestureTestMain.class);
                startActivity(gesIntent);
            }
        });
        autofittextviewBtn = (Button) findViewById(R.id.button6);
        autofittextviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent autoIntent = new Intent(MainActivity.this, AutoFitTextviewMian.class);
                startActivity(autoIntent);
            }
        });
        Button btn7 = (Button) findViewById(R.id.button7);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent7 = new Intent(MainActivity.this, CallbackDemoMian.class);
                startActivity(intent7);
            }
        });
        Button btn8 = (Button) findViewById(R.id.button8);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent8 = new Intent(MainActivity.this, GetImageMain.class);
                startActivity(intent8);
            }
        });
        Button btn9 = (Button) findViewById(R.id.button9);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent9 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent9);
            }
        });
        Button btn10 = (Button) findViewById(R.id.button10);
        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intetn10 = new Intent(MainActivity.this, Compasstest.class);
                startActivity(intetn10);
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
                    if (myservice!=null){
                        updateBar.setProgress(msg.arg1);
                        tv.setText(msg.arg1+"--");
                    }
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
            myservice = null;
            try{
                unbindService(conn);
            }catch (Exception e){}

        }
    }


    @Override
    protected void onDestroy() {
        try {
            unbindService(conn);
        }catch (Exception e){

        }
        super.onDestroy();
    }
}

package com.example.yjsong.yjsandroidtest;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.yjsong.yjsandroidtest.MyService.MyBuild;

import java.lang.ref.WeakReference;

import com.example.yjsong.yjsandroidtest.CallBackTest.MainCallBack;
import com.example.yjsong.yjsandroidtest.RecyclerViewTest.HomeActivity;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private TextView text;
    private Handler mHandler = new Handler();
    private Button btn_view;
    private static final int REQUEST_WRITE_STORAGE = 111;
    private CustomDialog mDialog = null;
    private Button produceMd5;
    private MyService myService;  //我们自己的service
    private SeekBar pBar;  //模拟service更新进度条
    private ConnectionService connectionService;
    private Button btn4,btn5;

    //apk下载链接
    private static final String APK_DOWNLOAD_URL = "http://app.xiaomi.com/download/421138?ref=search";


    /**
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        btn4 = (Button) findViewById(R.id.button4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, MainCallBack.class);
                startActivity(mIntent);
            }
        });
        btn5 = (Button) findViewById(R.id.button5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                //intent.setClass(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        pBar = (SeekBar) findViewById(R.id.seekbar);
        connectionService=new ConnectionService();
        text =(TextView) findViewById(R.id.text);
        produceMd5 = (Button) findViewById(R.id.button);
        produceMd5.setOnClickListener(new myOnclickListener());
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setTitle("提示");
                dialog.setMessage("我是点击产生的对话框");
                dialog.show();
            }
        });
//// TODO: 2016/9/14
        mHandler.postDelayed(new DownRunable(text),8000);

        btn_view = (Button) findViewById(R.id.btn_view);
        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chechVersion();
            }
        });
    }

    private static  final class DownRunable implements Runnable{
        private  final WeakReference<TextView> mtextview;
        protected DownRunable(TextView textView){
            mtextview = new WeakReference<TextView>(textView);
        }

        @Override
        public void run() {
            final  TextView textView = mtextview.get();
            if (textView!=null){
                textView.setText("down");
            }
        }
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

    private void chechVersion() {
        mDialog = new CustomDialog(MainActivity.this);
        mDialog.setTitle("发现新版本");
        mDialog.setContent("为了给大家提供更好的用户体验，每次应用的更新都包含速度和稳定性的提升，感谢您的使用！");
        mDialog.setOKButton("立即更新", new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //请求存储权限
                boolean hasPermission = (ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermission) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                    ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    //下载
                    startDownload();
                }

            }
        });
        mDialog.show();
    }

        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //获取到存储权限,进行下载
                    startDownload();
                } else {
                    Toast.makeText(MainActivity.this, "不授予存储权限将无法进行下载!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 启动下载
     */
    private void startDownload() {
        Intent it = new Intent(MainActivity.this, UpdateService.class);
        //下载地址
        it.putExtra("apkUrl", APK_DOWNLOAD_URL);
        startService(it);
        mDialog.dismiss();
    }

    private class myOnclickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this,Md5ProduceService.class);
            startService(myIntent);
        }
    }


   public void startService(View view) { //绑定service
        bindService(new Intent(this,MyService.class),connectionService, Context.BIND_AUTO_CREATE);
    }
    /**
     *
     * @author Administrator
     *实现service接口，用于service绑定时候，回调
     */
    class ConnectionService  implements ServiceConnection
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService=((MyBuild)service).getMyService(); //获取Myservice
            /**
             * 直接把当前对象传给service，这样service就可以随心所欲的调用本activity的各种可用方法
             */
            myService.setMainActivity(MainActivity.this); //把当前对象传递给myservice


            /**
             * 使用一个接口来实现回调，这样比上面方法更加灵活，推荐
             */
			myService.setOnProgressBarListener(new UpdateProgressListener() {

				@Override
				public void updateBar(int size) {
					updateBar(size);
				}
			});

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
    /**
     *
     * @param size
     * 更新seekbar
     */
    public void updateBar(int size)
    {
        pBar.setProgress(size);
    }

    /**
     *
     * @param view
     * 开始更新seekbar
     */
    public void startBar(View view) {
        if (myService==null) {
            Toast.makeText(this, "请先开始service", Toast.LENGTH_LONG).show();
        }
        else {
            myService.startUpdateProgress();
        }
    }
    /**
     *
     * @param view
     * 停止更新seekbar
     */
    public void stopBar(View view) {

        if (myService==null) {
            Toast.makeText(this, "更新没有开始", Toast.LENGTH_LONG).show();
        }
        else {
            myService.stopUpdateProgress();
        }
    }
    /**
     *
     * @param view
     * 停止service
     */
    public void stopService(View view) {
        myService=null;
        unbindService(connectionService);




    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unbindService(connectionService);
        } catch (Exception e) {
        }

    }

}

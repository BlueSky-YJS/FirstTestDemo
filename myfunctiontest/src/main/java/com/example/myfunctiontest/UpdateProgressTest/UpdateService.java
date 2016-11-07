package com.example.myfunctiontest.UpdateProgressTest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2016/11/3.
 */

public class UpdateService extends Service{
    private String apkURL;
    private String filePath;
    private static final String TAG = "TAG";
    private ProgressInterface progressinterface;
    public static final int pauseValue = 0x1001;
    private myBinder mybinder =new myBinder();
    public void setProgressinterface(ProgressInterface progressinterface){
        this.progressinterface = progressinterface;
    }
    public  class myBinder extends Binder{
        public UpdateService getservice(){
            return UpdateService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (intent == null) {
            stopSelf();
        }
        apkURL = intent.getStringExtra("apkUrl");
        Log.i(TAG, "下载地址: " + apkURL);
        startDownload();
        return mybinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        filePath = Environment.getExternalStorageDirectory() + "/autoupdate/update.apk";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*if (intent == null) {
            stopSelf();
        }
        apkURL = intent.getStringExtra("apkUrl");
        Log.i(TAG, "下载地址: " + apkURL);
        startDownload();*/
        return super.onStartCommand(intent, flags, startId);
    }

    public void startDownload(){
        UpdateManage.getInstance().startDownloads(apkURL, filePath, new UpdateDownloadListener() {

            @Override
            public void onStarted() {
            }

            @Override
            public void onProgressChanged(int progress, String downloadUrl) {
                if(progressinterface!=null){
                    progressinterface.updateProgress(progress);
                }
            }

            @Override
            public void onFinished(int completeSize, String downloadUrl) {
                stopSelf();
            }

            @Override
            public void onFailure() {
                stopSelf();
            }

            @Override
            public void onPausse() {
                if(progressinterface!=null){
                    progressinterface.updateProgress(pauseValue);
                }
            }
        });
    }

    public void onPause(){
        UpdateManage.getInstance().pauseDownloads();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        onPause();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

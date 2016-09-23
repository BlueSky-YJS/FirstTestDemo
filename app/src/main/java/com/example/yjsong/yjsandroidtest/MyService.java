package com.example.yjsong.yjsandroidtest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

public class MyService extends Service{
	private int size=0;
	private UpdateProgress updateProgress;
	private final long TIME=2000;
	private boolean isUpdate=true;
	private MainActivity activity;
	private UpdateProgressListener listener;
	@Override
	public IBinder onBind(Intent intent) {

		return new MyBuild();
	}
	@Override
	public void onCreate() {
		super.onCreate();
		updateProgress=new UpdateProgress();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	/**
	 *
	 * @author Administrator
	 * 线程用来改变seekbar进度的大小
	 *
	 */
	class UpdateProgress implements Runnable
	{
		@Override
		public void run() {
			if (isUpdate) {
				size+=10;
				if (size>100) {
					size=0;
				}
				handler.sendEmptyMessage(0);
			}
		}
	}
	/**
	 * 调用更新相应方法，更新进度条
	 */
	Handler handler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			/**
			 * 直接调用activity对象里面的方法
			 */
			if (activity!=null) {
				activity.updateBar(size);
			}

			/**
			 * 使用接口回调
			 */
//			if (listener!=null) {
//				listener.updateBar(size);
//			}
			handler.postDelayed(updateProgress, TIME);
		};
	};

	/**
	 * 停止更新
	 */
	public void stopUpdateProgress()
	{
		isUpdate=false;
	}
	/**
	 * 开启更新
	 */
	public void startUpdateProgress()
	{

		if (!isUpdate) {
			handler.post(updateProgress);
		}
		isUpdate=true;
	}
	public void onDestroy() {
		isUpdate=false;
	};

	/**
	 *
	 * @param activity
	 * 初始化MainActivity对象
	 */
	public void setMainActivity(MainActivity activity)
	{
		this.activity=activity;
	}
	/**
	 *
	 * @param listener
	 * 初始化UpdateProgressListener对象
	 */
	public void setOnProgressBarListener(UpdateProgressListener listener)
	{
		this.listener=listener;
	}

	/**
	 *
	 * @author Administrator
	 * 使用类部类，返回当前service的实例，用于activity，调用service的各种方法
	 *
	 */
	class MyBuild extends Binder
	{
		public MyService getMyService()
		{
			return MyService.this;
		}
	}
	

}

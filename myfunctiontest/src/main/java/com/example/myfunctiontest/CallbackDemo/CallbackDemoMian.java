package com.example.myfunctiontest.CallbackDemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myfunctiontest.R;

/**
 * author: Administrator
 * created on: 2016/12/14 14:02
 * description:
 */

public class CallbackDemoMian extends Activity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callbackdemo);

        onLincoln();
		/*
		 * button = (Button) findViewById(R.id.button1); //
		 * button.setOnClickListener(this);
		 *
		 * button.setOnClickListener(new View.OnClickListener() {
		 *
		 * @Override public void onClick(View v) {
		 * Toast.makeText(getApplicationContext(), "您点击了Button！！",
		 * Toast.LENGTH_SHORT).show(); } });
		 */

    }

	/*
	 * @Override public void onClick(View v) { switch (v.getId()) { case
	 * R.id.button1: Toast.makeText(getApplicationContext(), "您点击了Button！！",
	 * Toast.LENGTH_SHORT).show(); break;
	 *
	 * default: break; } }
	 */

    private void onLincoln() {
        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DownloadImageUtil.StartDownLoad(imageInterface,
                        CallbackDemoMian.this);
            }
        });

    }

    ImageStateInterface imageInterface = new ImageStateInterface() {

        @Override
        public void onImageStart() {
            Log.d("lincoln", "onImageStart");
            button.setText("onImageStart");
        }

        @Override
        public void onImageSuccess(final Bitmap bitmap) {
            Log.d("lincoln", "onImageSuccess");
            runOnUiThread(new Runnable() {

                @SuppressLint("NewApi")
                @Override
                public void run() {
                    button.setText("onImageSuccess");

                    button.setBackground(new BitmapDrawable(bitmap));
                }
            });
        }

        @Override
        public void onImageFailed() {

        }

        @Override
        public void OnEnd() {
            Toast.makeText(CallbackDemoMian.this, "哈哈!!", Toast.LENGTH_LONG).show();
        }
    };
}

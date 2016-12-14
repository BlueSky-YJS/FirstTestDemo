package com.example.myfunctiontest.CallbackDemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myfunctiontest.R;

/**
 * author: Administrator
 * created on: 2016/12/14 14:03
 * description:
 */

public class DownloadImageUtil {

    public static void StartDownLoad(final ImageStateInterface imageInterface,final Context context) {
        imageInterface.onImageStart();

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    new Thread().sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon);
                imageInterface.onImageSuccess(bitmap);
            }
        }).start();

        imageInterface.OnEnd();

    }
}

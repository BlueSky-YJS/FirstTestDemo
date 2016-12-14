package com.example.myfunctiontest.CallbackDemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


/**
 * author: Administrator
 * created on: 2016/12/14 14:32
 * description:
 */

public class ImageDownload {

    // 传递进接口参数，这样其他类引用的时候就能调用，这个方法在运行的时候又会回调MainActivity的方法
    public Bitmap getBitmap(final String path, final imageInterface callback) {

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                Bitmap bitmap = (Bitmap) msg.obj;
                callback.getImage(bitmap);
            }
        };
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet get = new HttpGet(path);
                HttpResponse response = null;
                try {
                    response = httpClient.execute(get);

                    if (response.getStatusLine().getStatusCode() == 200) {

                        byte[] datas = EntityUtils.toByteArray(response.getEntity());
                        Bitmap bitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length);
                        Message message = Message.obtain();
                        message.obj = bitmap;
                        handler.sendMessage(message);
                    }

                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

        return null;
    }
}

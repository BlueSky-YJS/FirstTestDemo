package com.example.myfunctiontest.CallbackDemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.myfunctiontest.R;

/**
 * author: Administrator
 * created on: 2016/12/14 14:25
 * description:
 */

public class GetImageMain extends Activity {
    private Button btn;
    private ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callbackdemo);
        btn = (Button) findViewById(R.id.button1);
        imageview = (ImageView) findViewById(R.id.imageView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "http://2t.5068.com/uploads/allimg/150407/1-15040G45F6.jpg";
                new ImageDownload().getBitmap(path, new imageInterface() {
                    @Override
                    public void getImage(Bitmap bitmap) {
                        imageview.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }


}

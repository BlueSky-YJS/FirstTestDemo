package com.example.myfunctiontest.CallbackDemo;

import android.graphics.Bitmap;

/**
 * author: Administrator
 * created on: 2016/12/14 13:59
 * description:
 */

public interface ImageStateInterface {

    void onImageStart();
    void onImageSuccess(Bitmap bitmap);
    void onImageFailed();
    void OnEnd();
}

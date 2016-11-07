package com.example.myfunctiontest.UpdateProgressTest;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2016/11/4.
 */

public class UpdateManage {


    private static UpdateManage manager;
    private ThreadPoolExecutor threadPoolExecutor;
    private UpdateDownloadRequest request;

    private UpdateManage() {
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    static {
        manager = new UpdateManage();
    }

    public static UpdateManage getInstance() {
        return manager;
    }

    public void startDownloads(String downloadUrl, String localPath, UpdateDownloadListener listener) {
        if (request != null) {
            return;
        }

        checkLocalFilePath(localPath);

        //开始文件的下载任务
        request = new UpdateDownloadRequest(downloadUrl, localPath, listener);
        Future<?> future = threadPoolExecutor.submit(request);
    }
    public void pauseDownloads(){
        if (request == null) {
            return;
        }
        threadPoolExecutor.remove(request);
    }


    /**
     * 用来检查文件路径是否已经存在
     *
     * @param localPath
     */
    private void checkLocalFilePath(String localPath) {
        File dir = new File(localPath.substring(0, localPath.lastIndexOf("/") + 1));
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(localPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

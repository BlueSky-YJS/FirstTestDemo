package com.example.myfunctiontest.FileTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

/**
 * 文件帮助类 获取SD卡路径，SD卡是否存在，复制文件和文件夹，删除文件和文件夹
 *
 * @author dengchukun
 *
 */
public class FileHelper {
    private static final String TAG = "FileHelper";

    /**
     * 得到SD卡的目录路径
     */
    public static String getSDCardDirPath() {
        return android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static boolean isSDCardExist() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    public static boolean copyFolder(String srcFolderFullPath, String destFolderFullPath) {
        Log.d(TAG, "copyFolder " + "srcFolderFullPath-" + srcFolderFullPath + " destFolderFullPath-" + destFolderFullPath);
        try {
            (new File(destFolderFullPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File file = new File(srcFolderFullPath);
            String[] files = file.list();
            File temp = null;
            for (int i = 0; i < files.length; i++) {
                if (srcFolderFullPath.endsWith(File.separator)) {
                    temp = new File(srcFolderFullPath + files[i]);
                } else {
                    temp = new File(srcFolderFullPath + File.separator + files[i]);
                }
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    copyFile(input, destFolderFullPath + "/" + (temp.getName()).toString());
                }
                if (temp.isDirectory()) { // 如果是子文件夹
                    copyFolder(srcFolderFullPath + "/" + files[i], destFolderFullPath + "/" + files[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "copyFolder " + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean copyFile(InputStream ins, String destFileFullPath) {
        Log.d(TAG, "copyFile " + "destFileFullPath-" + destFileFullPath);
        FileOutputStream fos = null;
        try {
            File file = new File(destFileFullPath);
            Log.d(TAG, "copyFile " + "开始读入");
            fos = new FileOutputStream(file);
            Log.d(TAG, "copyFile " + "开始写出");
            byte[] buffer = new byte[10240];//8192
            int count = 0;
            Log.d(TAG, "copyFile " + "准备循环了");
            while ((count = ins.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            Log.d(TAG, "copyFile " + "已经创建该文件");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "copyFile " + e.getMessage());
            return false;
        } finally {
            try {
                fos.close();
                ins.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "copyFile " + e.getMessage());
            }
        }
    }

    public static void deleteFolder(String targetFolderFullPath) {
        Log.d(TAG, "deleteFolder " + "targetFolderFullPath-" + targetFolderFullPath);
        File file = new File(targetFolderFullPath);
        if (!file.exists()) {
            return;
        }
        String[] files = file.list();
        File temp = null;
        for (int i = 0; i < files.length; i++) {
            if (targetFolderFullPath.endsWith(File.separator)) {
                temp = new File(targetFolderFullPath + files[i]);
            } else {
                temp = new File(targetFolderFullPath + File.separator + files[i]);
            }
            if (temp.isFile()) {
                deleteFile(targetFolderFullPath + "/" + (temp.getName()).toString());
            }
            if (temp.isDirectory()) { // 如果是子文件夹
                deleteFolder(targetFolderFullPath + "/" + files[i]);
            }
        }
        file.delete();
    }

    public static void deleteFile(String targetFileFullPath) {
        Log.d(TAG, "deleteFolder " + "targetFileFullPath-" + targetFileFullPath);
        File file = new File(targetFileFullPath);
        file.delete();
    }

    /**
     * 从assets目录下拷贝文件
     *
     * @param context
     *            上下文
     * @param assetsFilePath
     *            文件的路径名如：SBClock/0001cuteowl/cuteowl_dot.png
     * @param targetFileFullPath
     *            目标文件路径如：/sdcard/SBClock/0001cuteowl/cuteowl_dot.png
     */
    public static void copyFileFromAssets(Context context, String assetsFilePath, String targetFileFullPath) {
        Log.d(TAG, "copyFileFromAssets ");
        InputStream assestsFileImputStream;
        try {
            assestsFileImputStream = context.getAssets().open(assetsFilePath);
            FileHelper.copyFile(assestsFileImputStream, targetFileFullPath);
        } catch (IOException e) {
            Log.d(TAG, "copyFileFromAssets " + "IOException-" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从assets目录下拷贝整个文件夹，不管是文件夹还是文件都能拷贝
     *
     * @param context
     *            上下文
     * @param rootDirFullPath
     *            文件目录，要拷贝的目录如assets目录下有一个SBClock文件夹：SBClock
     * @param targetDirFullPath
     *            目标文件夹位置如：/sdcrad/SBClock
     */
    public static void copyFolderFromAssets(Context context, String rootDirFullPath, String targetDirFullPath) {
        Log.d(TAG, "copyFolderFromAssets " + "rootDirFullPath-" + rootDirFullPath + " targetDirFullPath-" + targetDirFullPath);
        try {
            String[] listFiles = context.getAssets().list(rootDirFullPath);// 遍历该目录下的文件和文件夹
            for (String string : listFiles) {// 看起子目录是文件还是文件夹，这里只好用.做区分了
                Log.d(TAG, "name-" + rootDirFullPath + "/" + string);
                if (isFileByName(string)) {// 文件
                    copyFileFromAssets(context, rootDirFullPath + "/" + string, targetDirFullPath + "/" + string);
                } else {// 文件夹
                    String childRootDirFullPath = rootDirFullPath + "/" + string;
                    String childTargetDirFullPath = targetDirFullPath + "/" + string;
                    new File(childTargetDirFullPath).mkdirs();
                    copyFolderFromAssets(context, childRootDirFullPath, childTargetDirFullPath);
                }
            }
        } catch (IOException e) {
            Log.d(TAG, "copyFolderFromAssets " + "IOException-" + e.getMessage());
            Log.d(TAG, "copyFolderFromAssets " + "IOException-" + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private static boolean isFileByName(String string) {
        if (string.contains(".")) {
            return true;
        }
        return false;
    }
}

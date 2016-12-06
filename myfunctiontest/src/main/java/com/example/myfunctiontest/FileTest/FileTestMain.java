package com.example.myfunctiontest.FileTest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myfunctiontest.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * author: Administrator
 * created on: 2016/12/6 16:54
 * description:
 */

public class FileTestMain extends Activity {
    private Button btn,btn2,btn3;
    private TextView textview;
    private ProgressBar progressbar;
    private FileInfo info;
    private boolean stop = false;
    private Message msg;
    private Handler myHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    FileInfo minfo = (FileInfo) msg.obj;
                    String text = "";
                    text = "拷贝百分比:"+minfo.getStr()+"拷贝速度:"+minfo.getSpeedStr()+"剩余时间:"+minfo.getRealtimeStr();
                    if(minfo.getStr().equalsIgnoreCase("99.99%")){
                        text = "拷贝百分比:"+"100%"+"拷贝速度:"+minfo.getSpeedStr()+"剩余时间:"+minfo.getRealtimeStr();

                    }
                    textview.setText(text);
                    progressbar.setProgress(minfo.getCount());
                    break;
                case 2:
                    btn3.setText(msg.obj.toString());
                    break;

                default:
                    break;
            }

        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filetest_main);
        btn = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        final NumberFormatUtil utils = new NumberFormatUtil();
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                stop =!stop;
                btn2.setClickable(!stop);
                btn.setClickable(stop);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String basePath = Environment.getExternalStorageDirectory().getPath();
                String path = basePath+"/lingtu/db/maps/gcdata/gcpack.dat";
                String destFileFullPath = basePath+"/lingtu/gcpack.dat";
                String oldstr = "";
                String newstr = "";
                Message msg1 = Message.obtain();
                File copyfile = new File(path);
                File tofile = new File(destFileFullPath);
                if(copyfile.isFile()&&copyfile.exists()){
                    long fromsize = copyfile.length();
                    long m = fromsize;
                    oldstr = "原文件大小:"+utils.getTrafficData(1318024558);
                }else{
                    oldstr = "原文件不存在";
                }
                if(tofile.isFile()&&tofile.exists()){
                    long tofilesize = tofile.length();
                    newstr = "已拷贝大小:"+utils.getTrafficData(tofilesize);
                }else{
                    newstr = "已拷贝文件不存在";
                }
                msg1.what = 2;
                msg1.obj = oldstr+"-:-"+newstr;
                myHandler.sendMessage(msg1);

            }
        });

        textview = (TextView) findViewById(R.id.textView1);
        progressbar = (ProgressBar) findViewById(R.id.progressBar1);
        btn.setOnClickListener(new myclick());
    }

    public class myclick implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            btn.setClickable(!stop);
            FileHelper helper = new FileHelper();
            InputStream ins = null;
            String basePath = Environment.getExternalStorageDirectory().getPath();
            final String path = basePath+"/lingtu/db/maps/gcdata/gcpack.dat";
            final String destFileFullPath = basePath+"/lingtu/gcpack.dat";
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    copyFile(path, destFileFullPath);
                }
            }).start();
            //copyFile(path, destFileFullPath);
			/*try {
				ins = new FileInputStream(new File(path));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			helper.copyFile(ins, destFileFullPath);*/
        }

    }


    //
    private void copyFile(String frompath,String topath){
        stop = false;
        btn2.setClickable(!stop);
        File copyfile = new File(frompath);
        File tofile = new File(topath);
        long size = copyfile.length();
        if (!copyfile.exists()) {return ;}
        if (!copyfile.isFile()) {return ;}
        if (!copyfile.canRead()) {return ;}
        try {
            if(tofile.isFile()&&!tofile.exists()){
                tofile.createNewFile();
            }
            long len = copyfile.length();
            progressbar.setMax((int)len);
            FileInputStream fin = new FileInputStream(copyfile);
            FileOutputStream fout = new FileOutputStream(tofile);
            byte[] buff = new byte[1024];
            int s;
            int count = 0;
            long startTime = System.currentTimeMillis();
            while ((s = fin.read(buff)) > 0 && !stop) {
                count += s;
                fout.write(buff, 0, s);
                String str = "" + 100 * (count / (size + 0.01));
                str = forMatString(str);
                long endTime = System.currentTimeMillis();
                String speedStr = getSpeed(count, startTime, endTime);
                String remailTime = getRemailTime(count, size, startTime, endTime);
                info = new FileInfo();
                msg = Message.obtain();
                msg.what = 1;
                info.setCount(count);
                info.setStr(str+"%");
                info.setSpeedStr(speedStr);
                info.setRealtimeStr(remailTime);
                msg.obj = info;
                myHandler.sendMessage(msg);
                //copyFileProgressBar.setString(" precent:   " + str + " %" + "    speed: " + speedStr + "    " + " remail  time : " + remailTime);
                //copyFileProgressBar.setValue(count);
            }
            fin.close();
            fout.close();
            stop = true;

        } catch (Exception e) {
            // TODO: handle exception
            Log.i("exception---", e.getMessage());
        }

    }
    //
    private String forMatString(String str) {
        String values;
        int index = str.indexOf(".");
        values = str.substring(0, index + 3);
        return values;
    }
    private String getSpeed(long readByte, long startTime, long endTime) {
        long speed;
        if (endTime - startTime != 0) {
            speed = (readByte / (endTime - startTime)) * 1000;
            if (speed > 1024 * 1024) {
                return forMatString(speed / (1024 * 1024 + 0.1) + "") + " m/s";
            } else if (speed > 1024) {
                return forMatString(speed / (1024 + 0.1) + "") + " k/s";
            } else {
                return speed + " b/s";
            }
        } else {
            return "0 b/s";
        }
    }
    //
    private String getRemailTime(long readByte, long totalByte, long startTime, long endTime) {
        long hour;
        long minute;
        long second;
        String h;
        String m;
        String s;
        try {
            long speed = readByte / (endTime - startTime);
            long time = ((totalByte - readByte) / speed) / 1000;
            hour = time / 3600;
            minute = time % 3600 / 60;
            second = time % 3600 % 60;
            h = hour + "";
            m = minute + "";
            s = second + "";
            if (hour < 10) {
                m = "0" + minute;
            }
            if (minute < 10) {
                m = "0" + minute;
            }
            if (second < 10) {
                s = "0" + second;
            }
            return h + ":" + m + ":" + s;
        } catch (Exception ex) {
            return "00:00:00";
        }
    }
    //


    public void mcopyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (!oldfile.exists()) {return ;}
            if (!oldfile.isFile()) {return ;}
            if (!oldfile.canRead()) {return ;}
            if (!oldfile.exists()) { //文件不存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }
    //

    public class FileInfo{
        private int count;
        public int getCount() {
            return count;
        }
        public void setCount(int count) {
            this.count = count;
        }
        private String str;
        private String speedStr;
        private String realtimeStr;
        public String getStr() {
            return str;
        }
        public void setStr(String str) {
            this.str = str;
        }
        public String getSpeedStr() {
            return speedStr;
        }
        public void setSpeedStr(String speedStr) {
            this.speedStr = speedStr;
        }
        public String getRealtimeStr() {
            return realtimeStr;
        }
        public void setRealtimeStr(String realtimeStr) {
            this.realtimeStr = realtimeStr;
        }

    }

    //以追加的形式拷贝文件
    public void writeFileData(String pathName,String fileName,String oldpath){
        InputStream is = null;
        RandomAccessFile raf = null;
        try {

            File path = new File(pathName);
            File file = new File(pathName + fileName);
            if( !path.exists()) {
                Log.d("TestFile", "Create the path:" + pathName);
                path.mkdir();
            }
            if( !file.exists()) {
                Log.d("TestFile", "Create the file:" + fileName);
                file.createNewFile();
            }
            is = new FileInputStream(new File(oldpath));
            raf = new RandomAccessFile(file, "rw");
            raf.seek(file.length());
            byte[] buffer = new byte[4096];
            int length = -1;
            while ((length = is.read(buffer)) != -1) {
                raf.write(buffer, 0, length);

            }
            raf.close();


            try {
                Runtime.getRuntime().exec("sync");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        catch(Exception e){

            e.printStackTrace();

        }

    }

}

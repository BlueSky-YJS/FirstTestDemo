package com.example.yjsong.yjsandroidtest;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YJSONG on 2016/9/18.
 */
public class Md5ProduceService extends Service{
    private String filePath;
    private  static final String TAG="Md5ProduceService";
    //list集合存放MD5值
    private List<String> MD5List;


    @Override
    public void onCreate() {
        super.onCreate();
        filePath = Environment.getExternalStorageDirectory().getPath()+"/lingtu/";
        File mFIle = new File(filePath);
        if (!mFIle.exists()) mFIle.mkdirs();
        MD5List = new ArrayList<String>();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null)
        {
            Log.i(TAG,"生产MD5file失败");
            stopSelf();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (checkmd5Folder(filePath)){
                    produceMD5Xml(filePath,"MD5File.xml",MD5List);
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //写MD5校验文件
    public void produceMD5Xml(String filepath,String filename,List<String> list){
        //获得序列化对象
        XmlSerializer xmlSerializer= Xml.newSerializer();


        try {
            //指定序列化地址和编码
            File path=new File(filepath,filename);
            FileOutputStream out= new FileOutputStream(path.toString());
            //设置字节输出流，指定字符集
            xmlSerializer.setOutput(out, "utf-8");
            //设置doucument,开始写入
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "MD5Xml");
            // startTag (String namespace, String name)这里的namespace用于唯一标识xml标签
            //XML 命名空间属性被放置于某个元素的开始标签之中，并使用以下的语法：
            //  xmlns:namespace-prefix="namespaceURI"
            //   当一个命名空间被定义在某个元素的开始标签中时，所有带有相同前缀的子元素都会与同一个命名空间相关联。
            //   注释：用于标示命名空间的地址不会被解析器用于查找信息。其惟一的作用是赋予命名空间一个惟一的名称。不过，很多公司常常会作为指针来使用命名空间指向某个实存的网页，这个网页包含着有关命名空间的信息。

            for (int i = 0; i < list.size(); i++) {
                xmlSerializer.startTag(null, "MD5Value");
                xmlSerializer.attribute(null, "MD5", list.get(i).toString());
                //结束根节点
                xmlSerializer.endTag(null, "MD5Value");
            }

            xmlSerializer.endTag(null, "MD5Xml");
            xmlSerializer.endDocument();
            out.close();
            try {
                Runtime.getRuntime().exec("sync");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.i("xiexml", "xiexml..shibai");
            e.printStackTrace();
        }
    }
    //写xml结束

    //校验文件
    private boolean isFileExist(String strPath) {
        Log.i("ACTION_MOVE", "isFileExist:" + strPath);
        File ftemp = new File(strPath);
        if(!ftemp.exists()) {
            //showResLost(strPath);
            return false;
        }
        return true;
    }

    private boolean isPathExist(String strPath) {
        Log.i("ACTION_MOVE", "isPathExist:" + strPath);
        File ftemp = new File(strPath);
        if(!ftemp.exists() && !ftemp.isDirectory()) {
            //showResLost(strPath);
            return false;
        }
        return true;
    }


    //校验lingtu文件夹
    private boolean checkmd5Folder(String strAppPath) {
        boolean bRet = true;
        //TypedArray typeArray = getResources().obtainTypedArray(R.array.configfolder);
        String[] strLingtuArr = getResources().getStringArray(R.array.lingtufolder);
        for(int i = 0;i < strLingtuArr.length; i++) {
            bRet = isPathExist(strAppPath + strLingtuArr[i]);
            if(!bRet) {
                return false;
            }
        }

        String[] configArr = getResources().getStringArray(R.array.configPath);
        for(int i = 0;i < configArr.length; i++) {
            bRet = isPathExist(strAppPath + strLingtuArr[0]+"/"+configArr[i]);
            if(!bRet) {
                return false;
            }
        }

        String[] strConfigArr = getResources().getStringArray(R.array.configfolder);
        for(int i = 0;i < strConfigArr.length; i++) {
            String strConfigArrPath = strAppPath + strLingtuArr[0] + "/" + strConfigArr[i];
            bRet = isFileExist(strConfigArrPath);
            if(!bRet) {
                return false;
            }
            String md5ConfigFile=ToolMD5.verifyInstallPackage(strConfigArrPath);
            if(md5ConfigFile!=null){
                MD5List.add(md5ConfigFile);
            }
        }
        //db
        String[] strDBArr = getResources().getStringArray(R.array.dbfolder);
        for(int i = 0;i < strDBArr.length; i++) {
            bRet = isPathExist(strAppPath + strLingtuArr[1] + "/" + strDBArr[i]);
            if(!bRet) {
                return false;
            }
        }

        String[] strMapsArr = getResources().getStringArray(R.array.mapspath);
        for (int i = 0; i < strMapsArr.length; i++) {
            bRet = isPathExist(strAppPath + strLingtuArr[1]+"/"+strDBArr[0] + "/" + strMapsArr[i]);
            if(!bRet) {
                return false;
            }
        }

        String[] strMapFileArr = getResources().getStringArray(R.array.mapsfilefolder);
        for (int i = 0; i < strMapFileArr.length; i++) {
            String mapfilePath=strAppPath + strLingtuArr[1]+"/"+strDBArr[0] + "/" + strMapFileArr[i];
            bRet = isFileExist(mapfilePath);
            if(!bRet) {
                return false;
            }
            String md5Mapfile=ToolMD5.verifyInstallPackage(mapfilePath);
            if(md5Mapfile!=null){
                MD5List.add(md5Mapfile);
            }
        }

        String[] strdoorFileArr = getResources().getStringArray(R.array.doorfolder);
        for (int i = 0; i < strdoorFileArr.length; i++) {
            String doorPath=strAppPath + strLingtuArr[1]+"/"+strDBArr[0] + "/" + strMapsArr[0]+"/"+strdoorFileArr[i];
            bRet = isFileExist(doorPath);
            if(!bRet) {
                return false;
            }
            String md5Door=ToolMD5.verifyInstallPackage(doorPath);
            if(md5Door!=null){
                MD5List.add(md5Door);
            }
        }

        String[] strgcdataFileArr = getResources().getStringArray(R.array.gcdatafolder);
        for (int i = 0; i < strgcdataFileArr.length; i++) {
            String gcdataPath=strAppPath + strLingtuArr[1]+"/"+strDBArr[0] + "/" + strMapsArr[1]+"/"+strgcdataFileArr[i];
            bRet = isFileExist(gcdataPath);
            if(!bRet) {
                return false;
            }
            String md5Gcdata=ToolMD5.verifyInstallPackage(gcdataPath);
            if(md5Gcdata!=null){
                MD5List.add(md5Gcdata);
            }
        }

        String[] strlocalbinFileArr = getResources().getStringArray(R.array.localbinfolder);
        for (int i = 0; i < strlocalbinFileArr.length; i++) {
            String localbinPath=strAppPath + strLingtuArr[1]+"/"+strDBArr[0] + "/" + strMapsArr[2]+"/"+strlocalbinFileArr[i];
            bRet = isFileExist(localbinPath);
            if(!bRet) {
                return false;
            }
            String md5Localbin=ToolMD5.verifyInstallPackage(localbinPath);
            if(md5Localbin!=null){
                MD5List.add(md5Localbin);
            }
        }

        String[] strpakFileArr = getResources().getStringArray(R.array.pakfolder);
        for (int i = 0; i < strpakFileArr.length; i++) {
            String pakPath=strAppPath + strLingtuArr[1]+"/"+strDBArr[0] + "/" + strMapsArr[3]+"/"+strpakFileArr[i];
            bRet = isFileExist(pakPath);
            if(!bRet) {
                return false;
            }
            String md5Pak=ToolMD5.verifyInstallPackage(pakPath);
            if(md5Pak!=null){
                MD5List.add(md5Pak);
            }
        }

        String[] strrgFileArr = getResources().getStringArray(R.array.rgfolder);
        for (int i = 0; i < strrgFileArr.length; i++) {
            String rgPath=strAppPath + strLingtuArr[1]+"/"+strDBArr[0] + "/" + strMapsArr[4]+"/"+strrgFileArr[i];
            bRet = isFileExist(rgPath);
            if(!bRet) {
                return false;
            }
            String md5Rg=ToolMD5.verifyInstallPackage(rgPath);
            if(md5Rg!=null){
                MD5List.add(md5Rg);
            }
        }

        String[] strvpblockFileArr = getResources().getStringArray(R.array.vpblockfolder);
        for (int i = 0; i < strvpblockFileArr.length; i++) {
            String vpblockPath=strAppPath + strLingtuArr[1]+"/"+strDBArr[0] + "/" + strMapsArr[5]+"/"+strvpblockFileArr[i];
            bRet = isFileExist(vpblockPath);
            if(!bRet) {
                return false;
            }
            String md5Vpblock=ToolMD5.verifyInstallPackage(vpblockPath);
            if(md5Vpblock!=null){
                MD5List.add(md5Vpblock);
            }
        }


        String[] strdbpluginArr = getResources().getStringArray(R.array.dbpluginfolder);
        for (int i = 0; i < strdbpluginArr.length; i++) {
            String dbpluginPath=strAppPath + strLingtuArr[1]+"/"+strDBArr[1] + "/" + strdbpluginArr[i];
            bRet = isPathExist(dbpluginPath);
            if(!bRet) {
                return false;
            }
            String md5Dbplugin=ToolMD5.verifyInstallPackage(dbpluginPath);
            if(md5Dbplugin!=null){
                MD5List.add(md5Dbplugin);
            }
        }

        String[] strdbpluTourFileArr = getResources().getStringArray(R.array.tourfolder);
        for (int i = 0; i < strdbpluTourFileArr.length; i++) {
            String dbPluTourPath=strAppPath + strLingtuArr[1]+"/"+strDBArr[1] + "/" + strdbpluginArr[0]+"/"+strdbpluTourFileArr[i];
            bRet = isFileExist(dbPluTourPath);
            if(!bRet) {
                return false;
            }
            String md5DbpluTour=ToolMD5.verifyInstallPackage(dbPluTourPath);
            if(md5DbpluTour!=null){
                MD5List.add(md5DbpluTour);
            }
        }

        //db
					/*String[] strPluginArr = getResources().getStringArray(R.array.pluginfolder);
					for(int i = 0; i < strPluginArr.length; i++) {
						bRet = isPathExist(strAppPath + strLingtuArr[2] + "/" + strPluginArr[i]);
						if(!bRet) {
							return false;
						}
					}*/

        String[] strStartupArr = getResources().getStringArray(R.array.startupfolder);
        for(int i = 0; i < strStartupArr.length; i++) {
            String startupPath=strAppPath + strLingtuArr[3] + "/" + strStartupArr[i];
            bRet = isFileExist(startupPath);
            if(!bRet) {
                return false;
            }
            String md5Startup=ToolMD5.verifyInstallPackage(startupPath);
            if(md5Startup!=null){
                MD5List.add(md5Startup);
            }
        }

        String[] strUserDataArr = getResources().getStringArray(R.array.userdatafolder);
        for(int i = 0; i < strUserDataArr.length; i++) {
            bRet = isPathExist(strAppPath + strLingtuArr[4] + "/" + strUserDataArr[i]);
            if(!bRet) {
                return false;
            }
        }

        String[] strFourShopArr = getResources().getStringArray(R.array.fourshopfolder);
        for(int i = 0; i < strFourShopArr.length; i++) {
            String fourshopPath=strAppPath + strLingtuArr[4] + "/" +strUserDataArr[0]+"/"+ strFourShopArr[i];
            bRet = isFileExist(fourshopPath);
            if(!bRet) {
                return false;
            }
            String md5Fourshop=ToolMD5.verifyInstallPackage(fourshopPath);
            if(md5Fourshop!=null){
                MD5List.add(md5Fourshop);
            }
        }

        String[] strhiskeycfgArr = getResources().getStringArray(R.array.hiskeyconfigfolder);
        for(int i = 0; i < strhiskeycfgArr.length; i++) {
            String hiskeycfgPath=strAppPath + strLingtuArr[4] + "/" +strUserDataArr[2]+"/"+ strhiskeycfgArr[i];
            bRet = isFileExist(hiskeycfgPath);
            if(!bRet) {
                return false;
            }
            String md5hiskey=ToolMD5.verifyInstallPackage(hiskeycfgPath);
            if(md5hiskey!=null){
                MD5List.add(md5hiskey);
            }
        }

        String[] districtArr = getResources().getStringArray(R.array.districtfolder);
        for(int i = 0; i < districtArr.length; i++) {
            String districtPath=strAppPath + strLingtuArr[4] + "/" +strUserDataArr[13]+"/"+ districtArr[i];
            bRet = isFileExist(districtPath);
            if(!bRet) {
                return false;
            }
            String md5hiskey=ToolMD5.verifyInstallPackage(districtPath);
            if(md5hiskey!=null){
                MD5List.add(md5hiskey);
            }
        }

        return bRet;
    }

}

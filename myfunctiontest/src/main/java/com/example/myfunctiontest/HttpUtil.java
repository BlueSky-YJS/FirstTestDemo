package com.example.myfunctiontest;

/**
 * Created by Administrator on 2016/11/3.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpUtil {
    private NetWorkCallbackInterface callback;

    private final int CONNECT_TIME_OUT = 5 * 1000;
    private final int READ_TIME_OUT = 10 * 1000;

    /*
     * start----------------------------------------
     * 其他不用看，今天的主角是这里，回调。
     */
    public HttpUtil(NetWorkCallbackInterface callback) {
        this.callback=callback;
    }
    public void GetNow(){
        callback.showCallback(doGet("http://wwww.baidu.com", null));
    }
	/*
	 * ----------------------------------------end
	 */

    private URL url = null;
    private HttpURLConnection uRLConnection_GET;

    // 向服务器发送get请求
    private String doGet(String urlAddress, String params) {
        if (params == null)
            params = "";
        try {
            url = new URL(urlAddress + params);
            uRLConnection_GET = (HttpURLConnection) url.openConnection();
            // 接连超时
            uRLConnection_GET.setConnectTimeout(CONNECT_TIME_OUT);
            // 响应超时
            uRLConnection_GET.setReadTimeout(READ_TIME_OUT);
            InputStream is = uRLConnection_GET.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String response = "";
            String readLine = null;
            while ((readLine = br.readLine()) != null) {
                response = response + readLine;
            }
            is.close();
            br.close();
            uRLConnection_GET.disconnect();
            return response;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package com.example.yjsong.yjsandroidtest;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;


public class Md5Parse {
	private XmlPullParser myparser = Xml.newPullParser();
	boolean isMd5True = true;

	public boolean checkMD5Value(InputStream m_is,List<String> m_list){
		try {
			Log.i("md5解析器:", "流读取:"+m_is);
			//设置要解析的流对象以及字符编码方式
			myparser.setInput(m_is, "UTF-8");
			//获取pull解析的事件的类型
			int Eventcode = myparser.getEventType();
			//只要不是文档结束事件就一直执行下去
			while(Eventcode!=XmlPullParser.END_DOCUMENT){
				switch (Eventcode) {
					case XmlPullParser.START_DOCUMENT://在文档开始的时候创建集合对象
						System.out.println("初始化集合");
						break;
					case XmlPullParser.START_TAG:
						//获取标签名
						String tagName = myparser.getName();
						if (tagName.equalsIgnoreCase("MD5Xml")) {
							Log.i("校验md5开始:", "md5");

						}else if (tagName.equalsIgnoreCase("MD5Value")) {
							Log.i("md5值集合:", "集合为:"+m_list);
							isMd5True = false;
							String mdvalue = myparser.getAttributeValue(null, "MD5").toString();
							for (int i = 0; i < m_list.size(); i++) {
								Log.i("进行循环位置", "位置:"+i);
								Log.i("md5值为:", "测试MD5:"+mdvalue);
								if(m_list.get(i).toString().equalsIgnoreCase(mdvalue)){
									isMd5True = true;
									break;
								}
							}
						}
						break;
					case XmlPullParser.END_TAG:
						//获取标签名
						tagName = myparser.getName();
						if (tagName.equalsIgnoreCase("MD5Value")) {

						}else if (tagName.equalsIgnoreCase("MD5Xml")) {

						}
						break;
					default:
						break;
				}

				//手动去驱动
				myparser.next();
				Eventcode = myparser.getEventType();

			}
			//Log.i("城市列表", msg);
			return isMd5True;


		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isMd5True;
	}
}




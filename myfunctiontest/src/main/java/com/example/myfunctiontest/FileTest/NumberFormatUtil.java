package com.example.myfunctiontest.FileTest;

import java.text.DecimalFormat;

//转换地图数据包大小
public class NumberFormatUtil {

	public static String  getTrafficData(long total){

		String dataText = "0";
		DecimalFormat df = new DecimalFormat("###.00");
		if(total == -1){
			return dataText;
		}else if(total < 1024) {
			dataText += total + "byte";
		}else if(total < 1024 * 1024){
			dataText = df.format(total / 1024f) + "KB";
		}else if(total < 1024*1024*1024){
			dataText = df.format(total / 1024f /1024f) + "MB";
		}else{//total < 1024*1024*1024*1024
			dataText = df.format(total / 1024f /1024f/1024f) + "GB";
		}
		return dataText;
	}

}

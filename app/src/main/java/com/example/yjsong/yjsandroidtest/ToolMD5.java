package com.example.yjsong.yjsandroidtest;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import android.util.Log;

public class ToolMD5 {
	 
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			 'A', 'B', 'C', 'D', 'E', 'F' };

	 public static String toHexString(byte[] b) {
	        StringBuilder sb = new StringBuilder(b.length * 2);
	        for (int i = 0; i < b.length; i++) {
	            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
	            sb.append(HEX_DIGITS[b[i] & 0x0f]);
	        }
	        return sb.toString();
	    }
		public static String md5sum(String filename) {
			InputStream fis;
			byte[] buffer = new byte[1024];
			int numRead = 0;
			MessageDigest md5;
			try{
			    fis = new FileInputStream(filename);
			    md5 = MessageDigest.getInstance("MD5");
			    while((numRead=fis.read(buffer)) > 0) {
			         md5.update(buffer,0,numRead);
			    }
			    fis.close(); 
			    return toHexString(md5.digest());   
			 } catch (Exception e) {
			     System.out.println("error");
			      return null;
			  }
		}
		
		public static boolean verifyInstallPackage(String packagePath,String crc)
		{
			boolean isequal= false;
			String digestStr = md5sum(packagePath);
			if(digestStr!=null){
				digestStr=digestStr.toLowerCase();
				Log.i("MD5值:", "MD5校验码:"+digestStr);
			 if (digestStr.equals(crc)) {//比较两个文件的MD5值，如果一样则返回true
				 isequal= true;
             }
			}
			 return isequal;
		}
		
		public static String verifyInstallPackage(String packagePath)
		{
			String digestStr = null;
			digestStr = md5sum(packagePath);
			if(digestStr!=null){
				digestStr=digestStr.toLowerCase();
				Log.i("MD5值:", "MD5校验码:"+digestStr);
			}
			 return digestStr;
		}
		
}

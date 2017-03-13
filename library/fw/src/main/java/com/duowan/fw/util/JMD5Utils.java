package com.duowan.fw.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class JMD5Utils {
	private static char[] hexDigits = "0123456789ABCDEF".toCharArray();

	public static String md5(InputStream is) throws IOException {
		String md5 = "";

		try {
		    byte[] bytes = new byte[4096];
		    int read = 0;
		    MessageDigest digest = MessageDigest.getInstance("MD5");

		    while ((read = is.read(bytes)) != -1) {
		        digest.update(bytes, 0, read);
		    }

		    byte[] messageDigest = digest.digest();

		    StringBuilder sb = new StringBuilder(32);

		    for (byte b : messageDigest) {
		        sb.append(hexDigits[(b >> 4) & 0x0f]);
		        sb.append(hexDigits[b & 0x0f]);
		    }

		    md5 = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

	    return md5;
	}
	
	public static String md5(File file){
		String md5Checksum = "";
		try{
			FileInputStream fis   = new FileInputStream(file);
			md5Checksum = md5(fis);
			fis.close();	
		}catch(Exception e){
			e.printStackTrace();
		}
		return md5Checksum;
	}
	
	public static String md5(String path){
		return md5(new File(path));
	}
}

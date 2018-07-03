package com.yuechuankeji.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
	public static String getMd5(String str) throws NoSuchAlgorithmException{
		
		MessageDigest md = MessageDigest.getInstance("MD5");	
		md.update(str.getBytes());
		byte[] b = md.digest();
		
		int i;
		StringBuffer buff = new StringBuffer("");
		for(int index = 0 ; index <b.length ; index ++){
			i = b[index];
			if(i <0){
				i += 256;
			}
			if(i<16){
				buff.append("0");
			}
			buff.append(Integer.toHexString(i));
		}
		return buff.toString();
	}
}

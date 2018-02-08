package cn.management.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5Util {
	
	/**
	 * 获得md5加密后的数据
	 */
	public static String getMD5Value(String value){
		try {
			//1 获得jdk提供消息摘要算法工具类
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			//2 加密的结果10进制
			byte[] md5ValueByteArray = messageDigest.digest(value.getBytes());
			//3将10进制 转换16进制，new BigInteger(1,..);1表示正数
			BigInteger bigInteger = new BigInteger(1 , md5ValueByteArray);
			return bigInteger.toString(16);       //toString(16)返回16进制

		} catch (Exception e) {
			throw new RuntimeException(e);
			//如果出现了异常，将默认值
			//return value;
		}
	}
	
}

package com.whty.zdrj.libset.msg.utils;

import java.security.MessageDigest;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//import android.util.Log;

/**
 * 方法工具类，包含常用的一些数据格式的转换方法
 * 
 * @author OneWay
 * 
 */
public class FunctionUtils {

	/**
	 * ECB模式中的DES/3DES/TDES算法(数据不需要填充),支持8、16、24字节的密钥 说明：3DES/TDES解密算法 等同与
	 * 先用前8个字节密钥DES解密 再用中间8个字节密钥DES加密 最后用后8个字节密钥DES解密 一般前8个字节密钥和后8个字节密钥相同
	 * 
	 * @param key
	 *            加解密密钥(8字节:DES算法 16字节:3DES/TDES算法
	 *            24个字节:3DES/TDES算法,一般前8个字节密钥和后8个字节密钥相同)
	 * @param data
	 *            待加/解密数据(长度必须是8的倍数)
	 * @param mode
	 *            0-加密，1-解密
	 * @return 加解密后的数据 为null表示操作失败
	 */
	public static String desecb(String key, String data, int mode) {
		try {

			// 判断加密还是解密
			int opmode = (mode == 0) ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

			SecretKey keySpec = null;

			Cipher enc = null;

			// 判断密钥长度
			if (key.length() == 16) {
				// 生成安全密钥
				keySpec = new SecretKeySpec(str2bytes(key), "DES");// key

				// 生成算法
				enc = Cipher.getInstance("DES/ECB/NoPadding");
			} else if (key.length() == 32) {
				// 计算加解密密钥，即将16个字节的密钥转换成24个字节的密钥，24个字节的密钥的前8个密钥和后8个密钥相同,并生成安全密钥
				keySpec = new SecretKeySpec(str2bytes(key + key.substring(0, 16)), "DESede");// 将key前8个字节复制到keyecb的最后8个字节

				// 生成算法
				enc = Cipher.getInstance("DESede/ECB/NoPadding");
			} else if (key.length() == 48) {
				// 生成安全密钥
				keySpec = new SecretKeySpec(str2bytes(key), "DESede");// key

				// 生成算法
				enc = Cipher.getInstance("DESede/ECB/NoPadding");
			} else {
				// Log.d("GPMethod", "Key length is error");
				System.out.println("Key length is error");
				return null;
			}

			// 初始化
			enc.init(opmode, keySpec);

			// 返回加解密结果
			return (bytesToHexString(enc.doFinal(str2bytes(data))));// 开始计算
		} catch (Exception e) {
			// Log.d("GPMethod", e.getMessage());
			System.out.println(e.getMessage());
		}
		return null;
	}

	/**
	 * CBC模式中的DES/3DES/TDES算法(数据不需要填充),支持8、16、24字节的密钥 说明：3DES/TDES解密算法 等同与
	 * 先用前8个字节密钥DES解密 再用中间8个字节密钥DES加密 最后用后8个字节密钥DES解密 一般前8个字节密钥和后8个字节密钥相同
	 * 
	 * @param key
	 *            加解密密钥(8字节:DES算法 16字节:3DES/TDES算法
	 *            24个字节:3DES/TDES算法,一般前8个字节密钥和后8个字节密钥相同)
	 * @param data
	 *            待加/解密数据(长度必须是8的倍数)
	 * @param icv
	 *            初始链值(8个字节) 一般为8字节的0x00 icv="0000000000000000"
	 * @param mode
	 *            0-加密，1-解密
	 * @return 加解密后的数据 为null表示操作失败
	 */
	public static String descbc(String key, String data, String icv, int mode) {
		try {
			// 判断加密还是解密
			int opmode = (mode == 0) ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

			SecretKey keySpec = null;

			Cipher enc = null;

			// 判断密钥长度
			if (key.length() == 16) {
				// 生成安全密钥
				keySpec = new SecretKeySpec(str2bytes(key), "DES");// key

				// 生成算法
				enc = Cipher.getInstance("DES/CBC/NoPadding");
			} else if (key.length() == 32) {
				// 计算加解密密钥，即将16个字节的密钥转换成24个字节的密钥，24个字节的密钥的前8个密钥和后8个密钥相同,并生成安全密钥
				keySpec = new SecretKeySpec(str2bytes(key + key.substring(0, 16)), "DESede");// 将key前8个字节复制到keycbc的最后8个字节

				// 生成算法
				enc = Cipher.getInstance("DESede/CBC/NoPadding");
			} else if (key.length() == 48) {
				// 生成安全密钥
				keySpec = new SecretKeySpec(str2bytes(key), "DESede");// key

				// 生成算法
				enc = Cipher.getInstance("DESede/CBC/NoPadding");
			} else {
				// Log.d("GPMethod", "Key length is error");
				System.out.println("Key length is error");
				return null;
			}

			// 生成ICV
			IvParameterSpec ivSpec = new IvParameterSpec(str2bytes(icv));// icv

			// 初始化
			enc.init(opmode, keySpec, ivSpec);

			// 返回加解密结果
			return (bytesToHexString(enc.doFinal(str2bytes(data)))).toUpperCase(Locale.getDefault());// 开始计算
		} catch (Exception e) {
			e.printStackTrace();
			// Log.d("GPMethod", e.getMessage());
			System.out.println(e.getMessage());
		}
		return null;
	}

	/**
	 * 将字符串转化成二进制表示的字符串 "36" ==> "00110110"
	 * 
	 * @param str
	 *            长度必须为偶数
	 * @return
	 */
	public static String stringToBinaryStr(String str) {
		if (str.length() % 2 != 0) {
			System.out.println("传入的字符串长度非偶数");
			return null;
		} else {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < str.length(); i += 2) {
				String string = str.substring(i, i + 2);
				String part = Integer.toBinaryString(Integer.parseInt(string, 16));
				while (part.length() < 8) {
					part = '0' + part;
				}
				sb.append(part);
			}
			// System.out.println("字符串转换成二进制字符串后:" + sb.toString());
			return sb.toString().toUpperCase(Locale.getDefault());
		}
	}

	/**
	 * 将二进制字符串转换成16进制字符串 "00110110" ==> "36"
	 * 
	 * @param str
	 *            传入字符串长度必须是16的倍数
	 * @return
	 */
	public static String binaryStrToHexString(String str) {
		StringBuffer sb = new StringBuffer();
		if (str.length() % 16 != 0) {
			System.out.println("输入参数的长度不是16的整数倍");
			return null;
		} else {
			for (int i = 0; i < str.length(); i += 8) {
				String part = "";
				part = Integer.toHexString(Integer.parseInt(str.substring(i, i + 8), 2));
				while (part.length() < 2) {
					part = '0' + part;
				}
				sb.append(part);
			}
			return sb.toString();
		}
	}

	/**
	 * 将字符串转化成内存中存放的16进制表示 eg. "31323334" ==> "1234"
	 * 
	 * @param src
	 * @return
	 */
	public static String hexByteStringToStr(String src) {
		// String tag = "hexByteStringToStr";
		if (src.length() % 2 != 0) {
			// Log.e(tag, "输入的字符串长度有误！");
			System.out.println("输入的字符串长度有误！");
			return null;
		}
		byte[] tempData = str2bytes(src);
		String finalStr = "";
		for (int i = 0; i < tempData.length; i++) {
			char tempChar = (char) tempData[i];
			finalStr += tempChar;
		}
		return finalStr;
	}

	/**
	 * 将16进制组成的字符串转换成byte数组 例如 hex2Byte("0710BE8716FB"); 将返回一个byte数组
	 * b[0]=0x07;b[1]=0x10;...b[5]=0xFB;
	 * 
	 * @param src
	 *            待转换的16进制字符串
	 * @return
	 */
	public static byte[] str2bytes(String src) {
		if (src == null || src.length() == 0 || src.length() % 2 != 0) {
			return null;
		}
		int nSrcLen = src.length();
		byte byteArrayResult[] = new byte[nSrcLen / 2];
		StringBuffer strBufTemp = new StringBuffer(src);
		String strTemp;
		int i = 0;
		while (i < strBufTemp.length() - 1) {
			strTemp = src.substring(i, i + 2);
			byteArrayResult[i / 2] = (byte) Integer.parseInt(strTemp, 16);
			i += 2;
		}
		return byteArrayResult;
	}

	/**
	 * 该函数具有两个功能： 1.产生一个具有length长度的随机数 2.将该随机数转化为ascii的形式
	 * 
	 * @param length
	 * @return ascii字符串
	 */
	public static String generalStringToAscii(int length) {
		int num = 1;
		String strRandom;
		for (int i = 0; i < length; i++) {
			num *= 10;
		}
		try {
			Thread.sleep(10);
			Random rand = new Random((new Date()).getTime());
			strRandom = Integer.toString(rand.nextInt(num));
			int len = strRandom.length();
			for (int i = 0; i < length - len; i++) {
				strRandom = "0" + strRandom;
			}
		} catch (InterruptedException e) {
			strRandom = "00000000";
		}
		StringBuffer sbu = new StringBuffer();
		char[] chars = strRandom.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			sbu.append((int) chars[i]);
		}
		return sbu.toString();
	}

	/**
	 * 将byte数组转换成16进制组成的字符串 例如 一个byte数组 b[0]=0x07;b[1]=0x10;...b[5]=0xFB;
	 * byte2hex(b); 将返回一个字符串"0710BE8716FB"
	 * 
	 * @param bytes
	 *            待转换的byte数组
	 * @return
	 */
	public static String bytesToHexString(byte[] bytes) {
		if (bytes == null) {
			return "";
		}
		StringBuffer buff = new StringBuffer();
		int len = bytes.length;
		for (int j = 0; j < len; j++) {
			if ((bytes[j] & 0xff) < 16) {
				buff.append('0');
			}
			buff.append(Integer.toHexString(bytes[j] & 0xff));
		}
		return buff.toString();
	}

	public static String reverseStringBit(String src) {
		byte[] bits = str2bytes(src);
		for (int i = 0; i < bits.length; i++) {
			bits[i] = (byte) (0xff - bits[i]);
		}
		return bytesToHexString(bits);
	}

	/**
	 * 生成len个字节的十六进制随机数字符串 例如:len=8 则可能会产生 DF15F0BDFADE5FAF 这样的字符串
	 * 
	 * @param len
	 *            待产生的字节数
	 * @return String
	 */
	public static String yieldHexRand(int len) {
		StringBuffer strBufHexRand = new StringBuffer();
		Random rand = new Random(System.currentTimeMillis());
		int index;
		// 随机数字符
		char charArrayHexNum[] = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F' };
		for (int i = 0; i < len * 2; i++) {
			index = Math.abs(rand.nextInt()) % 16;
			if (i == 0) {
				while (charArrayHexNum[index] == '0') {
					index = Math.abs(rand.nextInt()) % 16;
				}
			}
			strBufHexRand.append(charArrayHexNum[index]);
		}
		return strBufHexRand.toString();
	}

	/**
	 * 将整数转换为规定长度的字符串
	 * 
	 * @param i
	 *            整数
	 * @param len
	 *            装换字符串的长度
	 * @return
	 */
	public static String int2String(int i, int len) {
		String ret = String.valueOf(i);
		while (ret.length() < len) {
			ret = "0" + ret;
		}
		return ret;
	}

	/**
	 * 按位异或byte数组 (两个byte数组的长度必须一样)
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static String doXOR(String b1, String b2) {
		if (b1.length() != b2.length()) {
			return null;
		}

		byte[] byte1 = str2bytes(b1);
		byte[] byte2 = str2bytes(b2);

		byte[] result = new byte[byte1.length];
		for (int i = 0; i < byte1.length; i++) {
			int temp = (byte1[i] ^ byte2[i]) & 0xff;
			result[i] = (byte) temp;
		}
		return bytesToHexString(result).toUpperCase(Locale.getDefault());
	}

	/**
	 * 填充80数据，首先在数据块的右边追加一个
	 * '80',如果结果数据块是8的倍数，不再进行追加,如果不是,追加0x00到数据块的右边，直到数据块的长度是8的倍数。
	 * 
	 * @param data
	 *            待填充80的数据
	 * @return
	 */
	public static String padding80(String data) {
		int padlen = 8 - (data.length() / 2) % 8;
		String padstr = "";
		for (int i = 0; i < padlen - 1; i++)
			padstr += "00";
		data = data + "80" + padstr;
		return data;
	}

	/**
	 * 得到TLV格式长度
	 * 
	 * @return
	 */
	public static String getLengthTLV(int n) {
		n = n / 2;
		String hex = "";
		if (n < 128) {
			hex = Int2HexStr(n, 2);
		} else if (n >= 128 && n < 256) {
			hex = "81" + Int2HexStr(n, 2);
		} else if (n >= 256) {
			hex = "82" + Int2HexStr(n, 4);
		}
		return hex;
	}

	/**
	 * 将整数转为16进行数后并以指定长度返回（当实际长度大于指定长度时只返回从末位开始指定长度的值）
	 * 
	 * @param val
	 *            int 待转换整数
	 * @param len
	 *            int 指定长度
	 * @return String
	 */
	public static String Int2HexStr(int val, int len) {
		String result = Integer.toHexString(val).toUpperCase(Locale.getDefault());
		int r_len = result.length();
		if (r_len > len) {
			return result.substring(r_len - len, r_len);
		}
		if (r_len == len) {
			return result;
		}
		StringBuffer strBuff = new StringBuffer(result);
		for (int i = 0; i < len - r_len; i++) {
			strBuff.insert(0, '0');
		}
		return strBuff.toString();
	}

	/**
	 * 按位异或byte数组 (两个byte数组的长度必须一样)
	 * 
	 * @param b1
	 *            (byte数组1)
	 * @param b2
	 *            (byte数组2)
	 * @param length
	 *            byte数组的长度 (b1.length = b2.length)
	 * @return
	 */
	public static byte[] doXOR(byte[] b1, byte[] b2, int length) {
		if (b1 == null || b2 == null || b1.length != length || b2.length != length) {
			return null;
		}
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			int temp = b1[i] ^ b2[i];
			result[i] = (byte) temp;
		}
		return result;
	}

	/**
	 * 填充0数据
	 * 
	 * @param data
	 * 
	 * @return
	 */
	public static String paddingLeft0(String data, int len) {
		int padlen = len * 2 - data.length();
		String padstr = "";
		for (int i = 0; i < padlen; i++)
			padstr += "0";
		data = padstr + data;
		return data;
	}

	public static String paddingLeft(String c, String data, int len) {
		int padlen = len * 2 - data.length();
		String padstr = "";
		for (int i = 0; i < padlen; i++)
			padstr += c;
		data = padstr + data;
		return data;
	}

	public static String paddingRight0(String data, int len) {
		int padlen = len * 2 - data.length();
		String padstr = "";
		for (int i = 0; i < padlen; i++)
			padstr += "0";
		data = data + padstr;
		return data;
	}

	public static String paddingRight(String c, String data, int len) {
		int padlen = len * 2 - data.length();
		String padstr = "";
		for (int i = 0; i < padlen; i++)
			padstr += c;
		data = data + padstr;
		return data;
	}

	public static byte[] copyOfRange(byte[] original, int start, int end) {
		if (start > end) {
			throw new IllegalArgumentException();
		}
		int originalLength = original.length;
		if (start < 0 || start > originalLength) {
			throw new ArrayIndexOutOfBoundsException();
		}
		int resultLength = end - start;
		int copyLength = Math.min(resultLength, originalLength - start);
		byte[] result = new byte[resultLength];
		System.arraycopy(original, start, result, 0, copyLength);
		return result;
	}

	/***
	 * MD5加码 生成32位md5码
	 */
	public static String string2MD5(String inStr) {
		if (inStr == null || inStr.trim().length() == 0) {
			return null;
		}
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	/**
	 * 加密解密算法 执行一次加密，两次解密
	 */
	public static String convertMD5(String inStr) {

		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;

	}

}
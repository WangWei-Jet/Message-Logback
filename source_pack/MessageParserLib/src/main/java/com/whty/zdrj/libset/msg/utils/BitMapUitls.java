package com.whty.zdrj.libset.msg.utils;

import java.util.ArrayList;
import java.util.List;

import com.whty.zdrj.libset.msg.ParseElement;

/**
 * 位图工具,用于生成8583报文中的位图信息
 * 
 * @author OneWay
 * 
 */
public class BitMapUitls {

	// private static final String tag = BitMapUitls.class.getSimpleName();

	/**
	 * 获取位图八字节的字符串表示(eg
	 * .0101011001010110010101100101011001010110010101100101011001010110 ==>
	 * "5656565656565656")
	 * 
	 * @param maps
	 *            长度64的0,1数组
	 * @return 二进制转成16进制后的字符串表示
	 */
	public static String getBitMap(int[] maps) {
		StringBuffer bits = new StringBuffer("0000000000000000000000000000000000000000000000000000000000000000");
		int bitsLen = 64;
		for (int i = 0; i < maps.length && i < bitsLen; i++) {
			if (maps[i] > 1) {
				bits.replace(maps[i] - 1, maps[i], "1");
			}
		}
		return FunctionUtils.binaryStrToHexString(bits.toString());
	}

	/**
	 * 获取位图八字节的字符串表示(eg ."0|1|0|1|0|1|1|0|0|1|0|1|0|1|1|0|0|1|0|1|0|1|1|0|0|1| 0|1
	 * |0|1|1|0|0|1|0|1|0|1|1|0|0|1|0|1|0|1|1|0|0|1|0|1|0|1|1|0|0|1|0|1|0|1|1|0" ==>
	 * "5656565656565656")
	 * 
	 * @param maps
	 *            长度64的0,1数组
	 * @return 二进制转成16进制后的字符串表示
	 */
	public static String getBitMap(String maps) {
		StringBuffer bits = new StringBuffer("0000000000000000000000000000000000000000000000000000000000000000");
		String[] array = maps.split("\\|");
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				if (array[i] != null && !array[i].equals("")) {
					int v = Integer.valueOf(array[i]);
					if (v > 1) {
						bits.replace(v - 1, v, "1");
					}
				}
			}
		}
		return FunctionUtils.binaryStrToHexString(bits.toString());
	}

	/**
	 * 通过ParseElement的ID来获取位图信息
	 * 
	 * @param data
	 *            ParseElement的list
	 * @return
	 */
	public static String getBitMap(List<ParseElement> data) {
		StringBuffer bits = new StringBuffer("0000000000000000000000000000000000000000000000000000000000000000");
		int bitsLen = 64;
		for (int i = 0; i < data.size() && i < bitsLen; i++) {
			ParseElement pe = data.get(i);
			if (pe != null) {
				int id = Integer.valueOf(pe.getId());
				if (id > 1) {
					bits.replace(id - 1, id, "1");
				}
			}

		}
		return FunctionUtils.binaryStrToHexString(bits.toString());
	}

	/**
	 * 将16进制八字节的byte数组转换成二进制位图信息
	 * 
	 * @param bitMap
	 * @return
	 */
	public static String getMessageUnit(byte[] bitMap) {
		String ret = FunctionUtils.stringToBinaryStr(FunctionUtils.bytesToHexString(bitMap));
		// Log.d(tag, "message unit:" + ret);
		System.out.println("message unit:" + ret);
		return ret;
	}

	/**
	 * 将16进制八字节BCD长度的string转成二进制的int数组（eg."36" ==> 00110110）
	 * 
	 * @param bitMap
	 * @return
	 */
	public static int[] getMessageUnit(String bitMap) {
		String ret = FunctionUtils.stringToBinaryStr(bitMap);
		// Log.d("BitMapUitls", "===========开始解析位元表===========");
		// System.out.println("===========开始解析位元表===========");
		ArrayList<Integer> list = new ArrayList<Integer>();
		StringBuffer unitLogInfo = new StringBuffer();
		if (ret != null) {
			for (int i = 0; i < ret.length(); i++) {
				if (ret.charAt(i) == '1') {
					list.add(i + 1);
					// Log.d(tag, "发现" + (i + 1) + "域");
				}
			}
			if (list.size() == 0)
				return null;

			int[] units = new int[list.size()];
			try {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) != null && !list.get(i).equals(""))
						units[i] = list.get(i);
					if (i == 0) {
						unitLogInfo.append(units[i] + "");
					} else {
						unitLogInfo.append("," + units[i]);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			/*if (unitLogInfo != null && unitLogInfo.length() > 0) {
				// Log.d("BitMapUitls", "发现 " + unitLogInfo.toString() + " 域");
				System.out.println("发现 " + unitLogInfo.toString() + " 域");
			}*/
			// Log.d("BitMapUitls", "===========解析位元表结束===========");
			// System.out.println("===========解析位元表结束===========");
			return units;
		}
		/*if (unitLogInfo != null && unitLogInfo.length() > 0) {
			// Log.d("BitMapUitls", "发现 " + unitLogInfo.toString() + " 域");
			System.out.println("发现 " + unitLogInfo.toString() + " 域");
		}*/
		// Log.d("BitMapUitls", "===========解析位元表结束===========");
		// System.out.println("===========解析位元表结束===========");
		return null;
	}
}

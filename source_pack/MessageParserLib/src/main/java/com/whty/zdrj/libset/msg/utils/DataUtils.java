/**
 * 
 */
package com.whty.zdrj.libset.msg.utils;

import java.util.regex.Pattern;

/**
 * <p>
 * Title:DataUtils
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Date:2018年4月17日 上午11:05:28
 * </p>
 * <p>
 * 
 * @author wangwei01
 *         </p>
 */
public class DataUtils {
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
}

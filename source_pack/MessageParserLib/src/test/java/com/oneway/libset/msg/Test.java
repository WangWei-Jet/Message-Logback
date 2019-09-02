/**
 * 
 */
package com.oneway.libset.msg;

import java.io.IOException;

/**
 * <p>
 * Title:Test
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Date:2018年4月8日 上午11:14:01
 * </p>
 * <p>
 * 
 * @author wangwei01
 *         </p>
 */
public class Test {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// InputStream is =
		// Test.class.getClassLoader().getResourceAsStream("template/8583-standard.json");
		//
		// int len = is.available();
		//
		// byte[] content = new byte[len];
		//
		// is.read(content, 0, len);
		//
		// String fieldModelFileStr = new String(content, "UTF-8");
		//
		// System.out.println(fieldModelFileStr);
		//
		// JSONObject jsonObject = JSONObject.fromObject(fieldModelFileStr);
		//
		// JSONArray jsonArray = jsonObject.getJSONArray("8583AllFieldModel");
		//
		// System.out.println(jsonArray.size());
		//
		// for (int i = 0; i < jsonArray.size(); i++) {
		// DataFormat dataFormat = (DataFormat)
		// JSONObject.toBean(jsonArray.getJSONObject(i), DataFormat.class);
		// System.out.println(dataFormat.toString());
		// }

		String aa = "12345";
		System.out.println(aa.substring(5));

	}

}

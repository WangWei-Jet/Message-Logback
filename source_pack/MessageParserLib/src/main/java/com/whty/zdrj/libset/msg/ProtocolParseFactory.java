package com.whty.zdrj.libset.msg;

import java.io.InputStream;

import com.whty.zdrj.libset.data.parse.Standard8583Parser;

public class ProtocolParseFactory {

	private static IDataHandler dataHandler;

	/**
	 * 获取8583报文解析器
	 * 
	 * @param lookupKey
	 *            报文模板对应的key
	 * @return
	 */
	public static IDataHandler getMSG8583DataParserHandler(String lookupKey) {
		if (dataHandler == null || !(dataHandler instanceof DataHandler)) {
			dataHandler = new DataHandler(lookupKey);
		}
		return dataHandler;
	}

	/**
	 * 
	 * @param parameters
	 * 
	 *            // 1.数据模板inputStream // 2.是否允许log输出 // 3.解析结果返回码模板inputStream
	 *            //无参时加载默认配置
	 * 
	 * @return
	 */
	public static IDataHandler getMSG8583ParserByJsonModel(InputStream dataModelIs, boolean allowLogOut) {
		IDataHandler dataHandler = new Standard8583Parser();
		InputStream targetDataModelIs = null;
		if (dataModelIs == null) {
			// 未指定数据模板inputStream，使用默认的
			targetDataModelIs = ProtocolParseFactory.class.getClassLoader()
					.getResourceAsStream("template/8583-standard-brief.json");
		} else {
			targetDataModelIs = dataModelIs;
		}
		dataHandler = new Standard8583Parser(targetDataModelIs, allowLogOut);
		return dataHandler;
	}
}

package com.whty.zdrj.libset.msg;

import java.util.List;
import java.util.Map;

public interface IDataHandler {

	/**
	 * 解析数据成每一个域
	 * 
	 * @param data
	 *            报文信息(从报文头一直到64域的MAC信息)
	 * @return 域信息list(域被封装成一个类)
	 */
	public List<?> parseData(StringBuffer data);

	/**
	 * 重组解析后的数据
	 * 
	 * @param listdata
	 *            报文解析后得到的list数据
	 * @return 重组后的报文数据
	 */
	public String rebuildData(List<?> listdata);

	/**
	 * 组装报文信息
	 * 
	 * @param data
	 * @return
	 */
	public String buildData(Map<String, Object> data);

	/**
	 * 解析标准的8583报文
	 * 
	 * @param data
	 *            8583报文信息
	 * @return 8583报文信息元列表
	 */
	public MessageParseResult parseStandard8583MSG(StringBuffer data);

	/**
	 * 重组标准的8583报文
	 * 
	 * @param listdata
	 *            数据元列表
	 * @return 拼装好的8583报文
	 */
	public String buildStandard8583MSG(List<?> listdata);

	/**
	 * 拼装标准的8583报文
	 * 
	 * @param mapData
	 *            报文数据
	 * @return 拼装好的8583报文
	 */
	public String buildStandard8583MSG(Map<String, Object> mapData);

	/**
	 * 设置是否输出打印信息
	 * 
	 * @param allowLogOut
	 * @return
	 */
	public void setLogPrint(boolean allowLogOut);

	/**
	 * 返回当前是否允许log输出
	 * 
	 * @return
	 */
	public boolean isLogPrintPermitted();

	/**
	 * 修改域模板格式(暂时不对外开放)
	 * 
	 * @param fieldModel
	 * @return
	 */
	// public boolean changeFieldModel(String fieldTag, Object fieldModel);
}

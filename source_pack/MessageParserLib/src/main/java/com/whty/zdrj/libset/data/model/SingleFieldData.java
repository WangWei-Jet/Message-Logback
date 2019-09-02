/**
 * 
 */
package com.whty.zdrj.libset.data.model;

/**
 * <p>
 * Title:SingleFieldData
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Date:2018年4月8日 下午2:15:35
 * </p>
 * <p>
 * 
 * @author wangwei01
 *         </p>
 */
public class SingleFieldData {

	private DataFormat dataFormat;

	private String orignalLenStr;

	private Integer parsedLen;

	private String originalData;

	private String parsedData;

	public DataFormat getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(DataFormat dataFormat) {
		this.dataFormat = dataFormat;
	}

	public String getOrignalLenStr() {
		return orignalLenStr;
	}

	public void setOrignalLenStr(String orignalLenStr) {
		this.orignalLenStr = orignalLenStr;
	}

	public Integer getParsedLen() {
		return parsedLen;
	}

	public void setParsedLen(Integer parsedLen) {
		this.parsedLen = parsedLen;
	}

	public String getOriginalData() {
		return originalData;
	}

	public void setOriginalData(String originalData) {
		this.originalData = originalData;
	}

	public String getParsedData() {
		return parsedData;
	}

	public void setParsedData(String parsedData) {
		this.parsedData = parsedData;
	}

}

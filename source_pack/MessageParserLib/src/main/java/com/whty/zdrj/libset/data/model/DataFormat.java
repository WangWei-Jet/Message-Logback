/**
 * 
 */
package com.whty.zdrj.libset.data.model;

/**
 * <p>
 * Title:DataFormat
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Date:2018年4月4日 下午4:33:32
 * </p>
 * <p>
 * 
 * @author wangwei01
 *         </p>
 */
public class DataFormat {
	// TAG
	private String tag;
	// length位所占字节(数据变长才存在)
	private Integer lenLen;
	// length位字节编码
	private String lenEncode;
	// 数据最大长度
	private Integer dataMaxLen;
	// 数据长度(数据定长才存在)
	private Integer dataLen;
	// 数据编码
	private String dataEncode;
	// 数据对齐方向(当数据域是BCD码编码并且真实长度是奇数的时候有意义),默认左对齐
	private String dataPaddingDirection;
	// 填充数据
	private String paddingData;
	// 说明/备注
	private String comments;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getLenLen() {
		return lenLen;
	}

	public void setLenLen(Integer lenLen) {
		this.lenLen = lenLen;
	}

	public String getLenEncode() {
		return lenEncode;
	}

	public void setLenEncode(String lenEncode) {
		this.lenEncode = lenEncode;
	}

	public Integer getDataMaxLen() {
		return dataMaxLen;
	}

	public void setDataMaxLen(Integer dataMaxLen) {
		this.dataMaxLen = dataMaxLen;
	}

	public Integer getDataLen() {
		return dataLen;
	}

	public void setDataLen(Integer dataLen) {
		this.dataLen = dataLen;
	}

	public String getDataEncode() {
		return dataEncode;
	}

	public void setDataEncode(String dataEncode) {
		this.dataEncode = dataEncode;
	}

	public String getDataPaddingDirection() {
		return dataPaddingDirection;
	}

	public void setDataPaddingDirection(String dataPaddingDirection) {
		this.dataPaddingDirection = dataPaddingDirection;
	}

	public String getPaddingData() {
		return paddingData;
	}

	public void setPaddingData(String paddingData) {
		this.paddingData = paddingData;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "DataFormat [tag=" + tag + ", lenLen=" + lenLen + ", lenEncode=" + lenEncode + ", dataMaxLen="
				+ dataMaxLen + ", dataLen=" + dataLen + ", dataEncode=" + dataEncode + ", dataPaddingDirection="
				+ dataPaddingDirection + ", comments=" + comments + "]";
	}

}

/**
 * 
 */
package com.whty.zdrj.libset.data.parse;

import com.whty.zdrj.libset.data.model.SingleFieldData;

/**
 * <p>
 * Title:DataParseResult
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Date:2018年4月9日 下午1:25:59
 * </p>
 * <p>
 * 
 * @author wangwei01
 *         </p>
 */
public class SingleFieldParseResult {
	// 单个域对象
	private SingleFieldData singleFieldData;
	// 解析前所有数据块
	private String allDataSection;
	// 解析后剩余数据块
	private String leftDataSection;
	// 解析结果
	private boolean parseSuccess;

	private String remarks;

	public SingleFieldData getSingleFieldData() {
		return singleFieldData;
	}

	public void setSingleFieldData(SingleFieldData singleFieldData) {
		this.singleFieldData = singleFieldData;
	}

	public String getAllDataSection() {
		return allDataSection;
	}

	public void setAllDataSection(String allDataSection) {
		this.allDataSection = allDataSection;
	}

	public String getLeftDataSection() {
		return leftDataSection;
	}

	public void setLeftDataSection(String leftDataSection) {
		this.leftDataSection = leftDataSection;
	}

	public boolean isParseSuccess() {
		return parseSuccess;
	}

	public void setParseSuccess(boolean parseSuccess) {
		this.parseSuccess = parseSuccess;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}

package com.whty.zdrj.libset.data.model;

/**
 * 交易要素
 * 
 * @author Administrator
 * 
 */
public class TransactionElements {
	// 消息类型
	private String messageType;
	// 处理码
	private String processCode;
	// 服务点条件码
	private String serviceConditionCode;
	// 消息类型码;
	private String messageTypeCode;
	// 网络管理信息码
	private String netManagementInfoCode;

	// 报文类型中文说明
	private String comments;
	// 自定义编号
	private String customNum;
	// 必须域
	private String mfields;
	// 条件域
	private String cfields;

	public String getCustomNum() {
		return customNum;
	}

	public void setCustomNum(String customNum) {
		this.customNum = customNum;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	public String getServiceConditionCode() {
		return serviceConditionCode;
	}

	public void setServiceConditionCode(String serviceConditionCode) {
		this.serviceConditionCode = serviceConditionCode;
	}

	public String getMessageTypeCode() {
		return messageTypeCode;
	}

	public void setMessageTypeCode(String messageTypeCode) {
		this.messageTypeCode = messageTypeCode;
	}

	public String getNetManagementInfoCode() {
		return netManagementInfoCode;
	}

	public void setNetManagementInfoCode(String netManagementInfoCode) {
		this.netManagementInfoCode = netManagementInfoCode;
	}

	public String getMfields() {
		return mfields;
	}

	public void setMfields(String mfields) {
		this.mfields = mfields;
	}

	public String getCfields() {
		return cfields;
	}

	public void setCfields(String cfields) {
		this.cfields = cfields;
	}
}

/**
 * 
 */
package com.whty.zdrj.libset.data.pattern.reponsibilitychain;

import com.whty.zdrj.libset.data.parse.SingleFieldParseResult;
import com.whty.zdrj.libset.msg.utils.BitMapUitls;

/**
 * <p>
 * Title:AbstructHandler
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Date:2018年3月14日 上午10:38:43
 * </p>
 * <p>
 * 
 * @author wangwei01
 *         </p>
 */
public abstract class AbstructHandler {

	// private DataFormat dataFormat;
	private String fieldTag;

	private AbstructHandler nextHandler;

	private HanderObserver handlerObserver;

	// public AbstructHandler(DataFormat dataFormat) {
	// this.dataFormat = dataFormat;
	// }

	public AbstructHandler(String fieldTag) {
		this.fieldTag = fieldTag;
	}

	public String getFieldTag() {
		return this.fieldTag;
	}

	// public DataFormat getDataFormat() {
	// return this.dataFormat;
	// }

	public void setNextHandler(AbstructHandler nextHandler) {
		this.nextHandler = nextHandler;
	}

	public void setHanderObserver(HanderObserver handlerObserver) {
		this.handlerObserver = handlerObserver;
	}

	public HanderObserver getHandlerObserver() {
		return handlerObserver;
	}

	public void handleEvent(String message) {
		SingleFieldParseResult singleFieldParseResult = doWork(message);

		handlerObserver.singleFinishNotified(singleFieldParseResult);
		if (!singleFieldParseResult.isParseSuccess()) {
			// 解析失败
			if (handlerObserver != null) {
				handlerObserver.finishNotified(false, singleFieldParseResult.getRemarks());
			}
			return;
		} else {
			// 解析成功
			// 如果是位元表解析成功，还需要添加后续的域解析handler
			if ("BITMAP".equalsIgnoreCase(singleFieldParseResult.getSingleFieldData().getDataFormat().getTag())) {
				String bitmapData = singleFieldParseResult.getSingleFieldData().getOriginalData();
				int[] messageUnits = BitMapUitls.getMessageUnit(bitmapData);
				for (int unit : messageUnits) {
					handlerObserver.addNextHandlerNotified(unit + "");
				}
			}
		}
		// go on,continue the responsibility chain
		String leftData = singleFieldParseResult.getLeftDataSection();
		if (nextHandler != null) {
			nextHandler.handleEvent(leftData);
		} else {
			if (leftData != null && leftData.trim().length() > 0) {
				// 有多余数据未解析
				if (handlerObserver != null) {
					handlerObserver.finishNotified(false, "原始数据存在多余信息");
				}
			} else {
				if (handlerObserver != null) {
					handlerObserver.finishNotified(true, "解析完成");
				}
			}
			System.out.println("hander chain finished");
		}
	}

	// do work according to event type
	abstract SingleFieldParseResult doWork(String message);
}

/**
 * 
 */
package com.whty.zdrj.libset.data.pattern.reponsibilitychain;

import com.whty.zdrj.libset.data.model.DataFormat;
import com.whty.zdrj.libset.data.model.SingleFieldData;
import com.whty.zdrj.libset.data.parse.SingleFieldParseResult;
import com.whty.zdrj.libset.msg.utils.FunctionUtils;

/**
 * <p>
 * Title:HandlerA
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Date:2018年3月14日 上午10:50:23
 * </p>
 * <p>
 * 
 * @author wangwei01
 *         </p>
 */
public class FieldParseHandler extends AbstructHandler {

	/**
	 * @param eventType
	 */
	public FieldParseHandler(String fieldTag) {
		super(fieldTag);
	}

	public DataFormat getDataFormat() {
		return getHandlerObserver().getDataFormat(getFieldTag());
	}

	@Override
	public SingleFieldParseResult doWork(String message) {
		SingleFieldData singleFieldData = new SingleFieldData();
		singleFieldData.setDataFormat(getDataFormat());
		SingleFieldParseResult singleFieldParseResult = new SingleFieldParseResult();
		if (message == null || message.trim().length() == 0) {
			singleFieldParseResult.setSingleFieldData(singleFieldData);
			singleFieldParseResult.setParseSuccess(false);
			singleFieldParseResult.setRemarks("待解析数据为空");
			return singleFieldParseResult;
		}
		singleFieldParseResult.setAllDataSection(message);

		String dataEncodeStr = getDataFormat().getDataEncode();
		if (dataEncodeStr == null || dataEncodeStr.trim().length() == 0) {
			// 获取模板中数据编码失败
			singleFieldParseResult.setSingleFieldData(singleFieldData);
			singleFieldParseResult.setParseSuccess(false);
			singleFieldParseResult.setRemarks("TAG_"
					+ singleFieldParseResult.getSingleFieldData().getDataFormat().getTag() + "_"
					+ singleFieldParseResult.getSingleFieldData().getDataFormat().getComments() + "_获取模板中数据编码失败");
			return singleFieldParseResult;
		}
		// 获取是否是变长
		Integer lenLen = getDataFormat().getLenLen();
		String originalLenStr = null;
		int parsedLen = 0;
		if (lenLen == null || lenLen == 0) {
			// 非变长，定长
			Integer originalLen = getDataFormat().getDataLen();
			if (originalLen == null || originalLen.intValue() == 0) {
				// 模板定长域中未获取到域长度
				singleFieldParseResult.setSingleFieldData(singleFieldData);
				singleFieldParseResult.setParseSuccess(false);
				singleFieldParseResult.setRemarks("TAG_"
						+ singleFieldParseResult.getSingleFieldData().getDataFormat().getTag() + "_"
						+ singleFieldParseResult.getSingleFieldData().getDataFormat().getComments() + "_模板定长域中未获取到域长度");
				return singleFieldParseResult;
			}
			parsedLen = originalLen.intValue();
		} else {
			// 变长
			if (getDataFormat().getDataMaxLen() == null) {
				// 获取模板中的最大长度失败
				singleFieldParseResult.setSingleFieldData(singleFieldData);
				singleFieldParseResult.setParseSuccess(false);
				singleFieldParseResult.setRemarks("TAG_"
						+ singleFieldParseResult.getSingleFieldData().getDataFormat().getTag() + "_"
						+ singleFieldParseResult.getSingleFieldData().getDataFormat().getComments() + "_获取模板中的最大长度失败");
				return singleFieldParseResult;
			}
			// 获取长度位数据
			if ("BCD".equalsIgnoreCase(getDataFormat().getLenEncode())) {
				if (message.length() < (lenLen.intValue() + 1) / 2 * 2) {
					singleFieldParseResult.setSingleFieldData(singleFieldData);
					singleFieldParseResult.setParseSuccess(false);
					singleFieldParseResult.setRemarks("TAG_"
							+ singleFieldParseResult.getSingleFieldData().getDataFormat().getTag() + "_"
							+ singleFieldParseResult.getSingleFieldData().getDataFormat().getComments() + "_长度位数据长度不够");
					return singleFieldParseResult;
				}
				originalLenStr = message.substring(0, (lenLen.intValue() + 1) / 2 * 2);
				parsedLen = Integer.valueOf(originalLenStr, 10);
				message = message.substring((lenLen.intValue() + 1) / 2 * 2);
			} else if ("ASCII".equalsIgnoreCase(getDataFormat().getLenEncode())) {
				if (message.length() < lenLen.intValue() * 2) {
					singleFieldParseResult.setSingleFieldData(singleFieldData);
					singleFieldParseResult.setParseSuccess(false);
					singleFieldParseResult.setRemarks("TAG_"
							+ singleFieldParseResult.getSingleFieldData().getDataFormat().getTag() + "_"
							+ singleFieldParseResult.getSingleFieldData().getDataFormat().getComments() + "_长度位数据长度不够");
					return singleFieldParseResult;
				}
				originalLenStr = message.substring(0, lenLen.intValue() * 2);
				String bcdLenStr = new String(FunctionUtils.str2bytes(originalLenStr));
				parsedLen = Integer.valueOf(bcdLenStr, 10);
				message = message.substring(lenLen.intValue() * 2);
			} else if ("BINARY".equalsIgnoreCase(getDataFormat().getLenEncode())) {
				if (message.length() < lenLen.intValue() * 2) {
					singleFieldParseResult.setSingleFieldData(singleFieldData);
					singleFieldParseResult.setParseSuccess(false);
					singleFieldParseResult.setRemarks("TAG_"
							+ singleFieldParseResult.getSingleFieldData().getDataFormat().getTag() + "_"
							+ singleFieldParseResult.getSingleFieldData().getDataFormat().getComments() + "_长度位数据长度不够");
					return singleFieldParseResult;
				}
				originalLenStr = message.substring(0, lenLen.intValue() * 2);
				parsedLen = Integer.valueOf(originalLenStr, 16);
				message = message.substring(lenLen.intValue() * 2);
			} else {
				// 未知的长度位编码
				singleFieldParseResult.setSingleFieldData(singleFieldData);
				singleFieldParseResult.setParseSuccess(false);
				singleFieldParseResult.setRemarks("TAG_"
						+ singleFieldParseResult.getSingleFieldData().getDataFormat().getTag() + "_"
						+ singleFieldParseResult.getSingleFieldData().getDataFormat().getComments() + "_未知的长度位编码");
				return singleFieldParseResult;
			}
			if (parsedLen == 0) {
				// 报文中的长度位为0
				singleFieldParseResult.setSingleFieldData(singleFieldData);
				singleFieldParseResult.setParseSuccess(false);
				singleFieldParseResult.setRemarks("TAG_"
						+ singleFieldParseResult.getSingleFieldData().getDataFormat().getTag() + "_"
						+ singleFieldParseResult.getSingleFieldData().getDataFormat().getComments() + "_报文中的长度位为0");
				return singleFieldParseResult;
			}
			if (parsedLen > getDataFormat().getDataMaxLen().intValue()) {
				// 报文中的长度位比模板中的最大长度大
				singleFieldParseResult.setSingleFieldData(singleFieldData);
				singleFieldParseResult.setParseSuccess(false);
				singleFieldParseResult
						.setRemarks("TAG_" + singleFieldParseResult.getSingleFieldData().getDataFormat().getTag() + "_"
								+ singleFieldParseResult.getSingleFieldData().getDataFormat().getComments()
								+ "_报文中的长度位比模板中的最大长度大");
				return singleFieldParseResult;
			}
		}
		// 成功获取数据域字节长度
		String originalData = null;
		String parsedData = null;
		if ("BCD".equalsIgnoreCase(dataEncodeStr)) {
			// 数据BCD编码
			if (message.length() < (parsedLen + 1) / 2 * 2) {
				singleFieldParseResult.setSingleFieldData(singleFieldData);
				singleFieldParseResult.setParseSuccess(false);
				singleFieldParseResult.setRemarks("TAG_"
						+ singleFieldParseResult.getSingleFieldData().getDataFormat().getTag() + "_"
						+ singleFieldParseResult.getSingleFieldData().getDataFormat().getComments() + "_数据位数据长度不够");
				return singleFieldParseResult;
			}
			originalData = message.substring(0, (parsedLen + 1) / 2 * 2);
			message = message.substring((parsedLen + 1) / 2 * 2);
			if (parsedLen % 2 != 0) {
				// 数据域真实长度为奇数，准备获取真实数据
				if ("right".equalsIgnoreCase(getDataFormat().getDataPaddingDirection())) {
					parsedData = originalData.substring(1, originalData.length());
				} else {
					parsedData = originalData.substring(0, originalData.length() - 1);
				}
			} else {
				parsedData = originalData;
			}
		} else if ("ASCII".equalsIgnoreCase(dataEncodeStr)) {
			// 数据ASCII码编码
			if (message.length() < parsedLen * 2) {
				singleFieldParseResult.setSingleFieldData(singleFieldData);
				singleFieldParseResult.setParseSuccess(false);
				singleFieldParseResult.setRemarks("TAG_"
						+ singleFieldParseResult.getSingleFieldData().getDataFormat().getTag() + "_"
						+ singleFieldParseResult.getSingleFieldData().getDataFormat().getComments() + "_数据位数据长度不够");
				return singleFieldParseResult;
			}
			originalData = message.substring(0, parsedLen * 2);
			message = message.substring(parsedLen * 2);
			parsedData = new String(FunctionUtils.str2bytes(originalData));
		} else if ("BINARY".equalsIgnoreCase(dataEncodeStr)) {
			// 数据十六进制编码
			if (message.length() < parsedLen * 2) {
				singleFieldParseResult.setSingleFieldData(singleFieldData);
				singleFieldParseResult.setParseSuccess(false);
				singleFieldParseResult.setRemarks("TAG_"
						+ singleFieldParseResult.getSingleFieldData().getDataFormat().getTag() + "_"
						+ singleFieldParseResult.getSingleFieldData().getDataFormat().getComments() + "_数据位数据长度不够");
				return singleFieldParseResult;
			}
			originalData = message.substring(0, parsedLen * 2);
			message = message.substring(parsedLen * 2);
			parsedData = originalData;
		} else {
			// 未知编码
			singleFieldParseResult.setSingleFieldData(singleFieldData);
			singleFieldParseResult.setParseSuccess(false);
			singleFieldParseResult
					.setRemarks("TAG_" + singleFieldParseResult.getSingleFieldData().getDataFormat().getTag() + "_"
							+ singleFieldParseResult.getSingleFieldData().getDataFormat().getComments() + "_未知的数据域编码");
			return singleFieldParseResult;
		}
		if (originalData == null) {
			singleFieldParseResult.setSingleFieldData(singleFieldData);
			singleFieldParseResult.setParseSuccess(false);
			singleFieldParseResult
					.setRemarks("TAG_" + singleFieldParseResult.getSingleFieldData().getDataFormat().getTag() + "_"
							+ singleFieldParseResult.getSingleFieldData().getDataFormat().getComments() + "_解析获取数据域失败");
			return singleFieldParseResult;
		}
		// 数据获取成功
		singleFieldData.setOriginalData(originalData);
		singleFieldData.setParsedData(parsedData);
		singleFieldData.setOrignalLenStr(originalLenStr);
		singleFieldData.setParsedLen(parsedLen);
		singleFieldParseResult.setSingleFieldData(singleFieldData);
		singleFieldParseResult.setLeftDataSection(message);
		singleFieldParseResult.setParseSuccess(true);
		return singleFieldParseResult;
	}

}

/**
 * 
 */
package com.whty.zdrj.libset.data.parse;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.whty.zdrj.libset.data.model.DataFormat;
import com.whty.zdrj.libset.data.pattern.reponsibilitychain.AbstructHandler;
import com.whty.zdrj.libset.data.pattern.reponsibilitychain.FieldChainManager;
import com.whty.zdrj.libset.data.pattern.reponsibilitychain.FieldParseHandler;
import com.whty.zdrj.libset.msg.IDataHandler;
import com.whty.zdrj.libset.msg.MessageParseResult;
import com.whty.zdrj.libset.msg.utils.BitMapUitls;
import com.whty.zdrj.libset.msg.utils.CustomLogger;
import com.whty.zdrj.libset.msg.utils.FunctionUtils;

/**
 * <p>
 * Title:Standard8583Parser
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Date:2018年4月8日 下午3:36:44
 * </p>
 * <p>
 * 
 * @author wangwei01
 *         </p>
 */
public class Standard8583Parser implements IDataHandler {

	// 数据模板inputStream
	private InputStream dataModelIs;
	// 是否允许log输出
	private boolean allowLogOut;

	private FieldChainManager fieldChainManager;

	private CustomLogger logger = CustomLogger.getLogger(this.getClass());

	/**
	 * 
	 */
	public Standard8583Parser() {
		super();
	}

	/**
	 * @param dataModelIs
	 * @param allowLogOut
	 * @param responseCodeModelIs
	 */
	public Standard8583Parser(InputStream dataModelIs, boolean allowLogOut) {
		super();
		this.dataModelIs = dataModelIs;
		this.allowLogOut = allowLogOut;

		fieldChainManager = FieldChainManager.createNewChainManager();
		fieldChainManager.loadTradeTypeModelFile();
		boolean initResult = fieldChainManager.loadDataModelFile(dataModelIs);

		showLog("读取模板文件结果:" + initResult);
	}

	public InputStream getDataModelIs() {
		return dataModelIs;
	}

	public void setDataModelIs(InputStream dataModelIs) {
		this.dataModelIs = dataModelIs;
	}

	public boolean isAllowLogOut() {
		return allowLogOut;
	}

	public void setAllowLogOut(boolean allowLogOut) {
		this.allowLogOut = allowLogOut;
	}

	@Override
	public List<?> parseData(StringBuffer data) {
		MessageParseResult messageParseResult = parseStandard8583MSG(data);
		if (messageParseResult == null) {
			return null;
		}
		return messageParseResult.getResultList();
	}

	@Override
	public String rebuildData(List<?> listdata) {
		return buildStandard8583MSG(listdata);
	}

	@Override
	public String buildData(Map<String, Object> data) {
		return buildStandard8583MSG(data);
	}

	@Override
	public MessageParseResult parseStandard8583MSG(StringBuffer data) {
		if (data == null || data.toString().trim().length() == 0) {
			showLog("请求解析的数据为空");
			return null;
		}
		if (fieldChainManager.getDataFormatList() == null || fieldChainManager.getDataFormatList().isEmpty()) {
			showLog("未加载到域模板数据");
			return null;
		}
		AbstructHandler msgHeaderParseHandler = new FieldParseHandler("MSGTYPE");
		AbstructHandler bitmapParseHandler = new FieldParseHandler("BITMAP");

		fieldChainManager.init();

		fieldChainManager.addHander(msgHeaderParseHandler);
		fieldChainManager.addHander(bitmapParseHandler);

		fieldChainManager.startDataParseChain(data.toString());
		MessageParseResult dataParseResult = fieldChainManager.getDataParseResult();
		if (dataParseResult != null) {
			showLog("请求数据:" + data);
			if (dataParseResult.getTransactionType() != null) {
				showLog("请求数据交易类别:" + dataParseResult.getTransactionType().getComments());
			}
			showLog("数据解析结果:" + dataParseResult.isSuccess());
			if (!dataParseResult.isSuccess()) {
				showLog("原因:" + dataParseResult.getResultRemarks());
			}
			List<?> resultList = dataParseResult.getResultList();
			if (resultList != null && !resultList.isEmpty()) {
				for (Object singleResult : resultList) {
					SingleFieldParseResult temp = (SingleFieldParseResult) singleResult;
					showLog("TAG:" + temp.getSingleFieldData().getDataFormat().getTag() + "=>"
							+ temp.getSingleFieldData().getDataFormat().getComments());
					showLog("\toriginalLen:" + temp.getSingleFieldData().getOrignalLenStr());
					showLog("\tparsedLen:" + temp.getSingleFieldData().getParsedLen());
					showLog("\tdataEncode:" + temp.getSingleFieldData().getDataFormat().getDataEncode());
					showLog("\toriginalData:" + temp.getSingleFieldData().getOriginalData());
					showLog("\tparsedData:" + temp.getSingleFieldData().getParsedData());
				}
			}
		}
		return dataParseResult;
	}

	@Override
	public String buildStandard8583MSG(List<?> listdata) {
		return null;
	}

	@Override
	public String buildStandard8583MSG(Map<String, Object> mapData) {
		if (mapData == null || mapData.isEmpty()) {
			showLog("请求组装报文的map数据为空");
			return null;
		}
		if (mapData.get("MSGTYPE") == null || ((String) mapData.get("MSGTYPE")).trim().length() == 0) {
			showLog("请求组装报文的map数据中未检测到报文头信息");
			return null;
		}
		StringBuilder strBuilder = new StringBuilder();
		Set<String> keySet = mapData.keySet();

		Map<String, String> targetDataMap = new HashMap<>();
		for (String key : keySet) {
			StringBuilder currentFieldBuilder = new StringBuilder();
			// 处理当前域
			DataFormat targetFieldDataFormat = fieldChainManager.getDataFormatList().get(key);
			if (targetFieldDataFormat == null) {
				showLog("配置文件找不到对应的域说明:" + key);
				return null;
			}
			// 找到对应的域模板
			String data = (String) mapData.get(key);
			int dataLen = 0;
			String dataEncode = targetFieldDataFormat.getDataEncode();
			if ("BCD".equalsIgnoreCase(dataEncode) || "ASCII".equalsIgnoreCase(dataEncode)) {
				dataLen = data.length();
			} else if ("BINARY".equalsIgnoreCase(dataEncode)) {
				if (data.length() % 2 != 0) {
					showLog("模板中该域的数据格式为binary，与传入的数据格式不符:TAG_" + key + ":" + targetFieldDataFormat.getComments());
					return null;
				}
				dataLen = data.length() / 2;
			} else {
				showLog("模板中定义的数据编码有误:TAG_" + key + ":" + targetFieldDataFormat.getComments());
				return null;
			}
			Integer lenLen = targetFieldDataFormat.getLenLen();
			if (lenLen != null && lenLen.intValue() > 0) {
				// 包含长度字段,变长
				if ((dataLen + "").length() > lenLen) {
					showLog("模板中定义的长度字段长度无法表示当前数据长度" + ":" + targetFieldDataFormat.getComments());
					return null;
				}
				Integer dataMaxLen = targetFieldDataFormat.getDataMaxLen();
				if (dataMaxLen == null || dataMaxLen.intValue() == 0) {
					showLog("模板中未定义数据最大长度");
				} else {
					// 判断数据长度与模板定义的数据最大长度
					if (dataLen > dataMaxLen.intValue()) {
						showLog("数据长度超过模板定义的最大长度:TAG_" + key + ":" + targetFieldDataFormat.getComments());
						return null;
					}
				}
				String lenEncode = targetFieldDataFormat.getLenEncode();
				String lenLenStr = dataLen + "";
				if ("BCD".equalsIgnoreCase(lenEncode)) {
					// 获取长度位数据
					while (lenLenStr.length() < (lenLen + 1) / 2 * 2) {
						lenLenStr = '0' + lenLenStr;
					}
				} else if ("ASCII".equalsIgnoreCase(lenEncode)) {
					lenLenStr = FunctionUtils.bytesToHexString(lenLenStr.getBytes());
					while (lenLenStr.length() < lenLen * 2) {
						lenLenStr = "30" + lenLenStr;
					}
				} else if ("BINARY".equalsIgnoreCase(lenEncode)) {
					showLog("目前不支持长度字节BINARY编码" + ":" + targetFieldDataFormat.getComments());
					return null;
				} else {
					showLog("模板中定义的长度字节编码有误:TAG_" + key + ":" + targetFieldDataFormat.getComments());
					return null;
				}
				showLog("TAG_" + key + ":" + targetFieldDataFormat.getComments() + ":组装后的长度域部分为:" + lenLenStr);
				currentFieldBuilder.append(lenLenStr);
			} else {
				// 定长
				Integer fixedDataLen = targetFieldDataFormat.getDataLen();
				if (fixedDataLen == null || fixedDataLen.intValue() == 0) {
					showLog("模板中未指定域数据长度:TAG_" + key);
					return null;
				}
				if ("BCD".equalsIgnoreCase(dataEncode) || "ASCII".equalsIgnoreCase(dataEncode)) {
					if (data.length() != fixedDataLen.intValue()) {
						showLog("数据长度与模板指定的长度不符:TAG_" + key + ":" + targetFieldDataFormat.getComments());
						return null;
					}
				} else if ("BINARY".equalsIgnoreCase(dataEncode)) {
					if (data.length() != fixedDataLen.intValue() * 2) {
						showLog("数据长度与模板指定的长度不符:TAG_" + key + ":" + targetFieldDataFormat.getComments());
						return null;
					}
				} else {
					showLog("模板中定义的数据编码有误:TAG_" + key + ":" + targetFieldDataFormat.getComments());
					return null;
				}
			}

			// 长度字段限制检查完毕，准备转换拼接数据位

			String targetData = data;
			if ("BCD".equalsIgnoreCase(dataEncode)) {
				// 获取长度位数据
				if (targetData.length() % 2 != 0) {
					// 数据长度为奇数，需要填充
					String dataPaddingDirection = targetFieldDataFormat.getDataPaddingDirection();
					String paddingData = targetFieldDataFormat.getPaddingData();
					if (paddingData == null) {
						showLog("模板中未定义填充元素数据:TAG_" + key + ":" + targetFieldDataFormat.getComments());
						return null;
					}
					if (paddingData.trim().length() != 1) {
						showLog("模板中填充元素长度有误，目前只允许填充元素长度为1,TAG_" + key + ":" + targetFieldDataFormat.getComments());
						return null;
					}
					if ("RIGHT".equalsIgnoreCase(dataPaddingDirection)) {
						// 数据靠右，则左填充
						targetData = paddingData + targetData;
					} else if ("LEFT".equalsIgnoreCase(dataPaddingDirection) || dataPaddingDirection == null
							|| dataPaddingDirection.trim().length() == 0) {
						// 默认左靠，右填充
						targetData = targetData + paddingData;
					} else {
						showLog("模板中填充方向数据有误:TAG_" + key + ":" + targetFieldDataFormat.getComments());
						return null;
					}
				} else {
					// 数据长度为偶数位，无需填充
					// showLog("数据长度为偶数位，无需填充:TAG_" + key + ":" +
					// targetFieldDataFormat.getComments());
				}
			} else if ("ASCII".equalsIgnoreCase(dataEncode)) {
				targetData = FunctionUtils.bytesToHexString(targetData.getBytes());
			} else if ("BINARY".equalsIgnoreCase(dataEncode)) {
				showLog("数据为BINARY编码，系统默认传入的数据符合该编码");
			} else {
				showLog("模板中定义的数据编码有误:TAG_" + key + ":" + targetFieldDataFormat.getComments());
				return null;
			}
			showLog("TAG_" + key + ":" + targetFieldDataFormat.getComments() + ":组装后的数据域部分为:" + targetData);
			currentFieldBuilder.append(targetData);

			targetDataMap.put(key, currentFieldBuilder.toString());
		}
		// 成功拼装完所有数据
		// 准备排序进行数据的拼接
		StringBuilder bitmapStringBuilder = new StringBuilder();
		for (int i = 1; i <= 64; i++) {
			String fieldValue = targetDataMap.get(i + "");
			if (fieldValue == null) {
				continue;
			}
			bitmapStringBuilder.append(i + "|");
			strBuilder.append(fieldValue);
		}
		// 计算位元表
		showLog(bitmapStringBuilder.toString());
		String calculatedBitmap = BitMapUitls.getBitMap(bitmapStringBuilder.toString());
		if (calculatedBitmap == null) {
			showLog("计算位元表信息失败");
			return null;
		}
		showLog("计算得到的位元表:" + calculatedBitmap);
		String originalBitmap = targetDataMap.get("BITMAP");
		if (originalBitmap != null) {
			if (!calculatedBitmap.equalsIgnoreCase(originalBitmap)) {
				showLog("计算得到的位元表信息与传入的位元表信息不一致");
			}
		}
		strBuilder.insert(0, originalBitmap == null ? calculatedBitmap : originalBitmap);
		strBuilder.insert(0, targetDataMap.get("MSGTYPE"));
		return strBuilder.toString().toUpperCase(Locale.getDefault());
	}

	@Override
	public void setLogPrint(boolean allowLogOut) {
		setAllowLogOut(allowLogOut);
	}

	@Override
	public boolean isLogPrintPermitted() {
		return isAllowLogOut();
	}

	private void showLog(String logInfo) {
		if (isLogPrintPermitted()) {
			// System.out.println(logInfo);
			logger.debug(logInfo);
		}
	}

	public boolean changeFieldModel(String fieldTag, Object fieldModel) {
		return fieldChainManager.changeFieldModel(fieldTag, fieldModel);
	}
}

/**
 * 
 */
package com.whty.zdrj.libset.data.pattern.reponsibilitychain;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.whty.zdrj.libset.data.model.DataFormat;
import com.whty.zdrj.libset.data.model.TransactionElements;
import com.whty.zdrj.libset.data.parse.SingleFieldParseResult;
import com.whty.zdrj.libset.msg.MessageParseResult;
import com.whty.zdrj.libset.msg.utils.DataUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <p>
 * Title:ChainManager
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Date:2018年3月14日 上午11:10:23
 * </p>
 * <p>
 * 
 * @author wangwei01
 *         </p>
 */
public class FieldChainManager extends HanderObserver {

	private static FieldChainManager singletonChianManager;

	private List<AbstructHandler> handerList = new ArrayList<>();

	private List<SingleFieldParseResult> singleFieldParseResultList;

	private MessageParseResult dataParseResult;

	private Map<String, DataFormat> dataFormatList = new HashMap<>();

	private Map<String, DataFormat> fileDataFormatMap = new HashMap<>();

	private Map<String, TransactionElements> allTradeTypeMap = new HashMap<>();

	// chain running state
	private boolean chainRunning = false;

	public static FieldChainManager getSingletonChainManager() {
		if (singletonChianManager == null) {
			singletonChianManager = new FieldChainManager();
		}
		return singletonChianManager;
	}

	public static FieldChainManager createNewChainManager() {
		return new FieldChainManager();
	}

	public MessageParseResult getDataParseResult() {
		return dataParseResult;
	}

	public Map<String, TransactionElements> getAllTradeTypeMap() {
		return allTradeTypeMap;
	}

	public boolean loadDataModelFile(InputStream is) {
		if (is == null) {
			return false;
		}
		try {
			dataFormatList.clear();
			int len = is.available();

			byte[] content = new byte[len];

			is.read(content, 0, len);

			String fieldModelFileStr = new String(content, "UTF-8");

			JSONObject jsonObject = JSONObject.fromObject(fieldModelFileStr);

			JSONArray jsonArray = jsonObject.getJSONArray("8583AllFieldModel");

			for (int i = 0; i < jsonArray.size(); i++) {
				DataFormat dataFormat = (DataFormat) JSONObject.toBean(jsonArray.getJSONObject(i), DataFormat.class);
				dataFormatList.put(dataFormat.getTag(), dataFormat);
				fileDataFormatMap.put(dataFormat.getTag(), dataFormat);
			}
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public void resetFieldModel() {
		Set<String> keySets = fileDataFormatMap.keySet();
		dataFormatList.clear();
		for (String key : keySets) {
			dataFormatList.put(key, fileDataFormatMap.get(key));
		}
	}

	public boolean loadTradeTypeModelFile() {
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("template/iso8583-msgtype.xml");
			allTradeTypeMap.clear();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(is);

			Element rootElement = doc.getDocumentElement();
			NodeList items = rootElement.getChildNodes();

			for (int i = 0; i < items.getLength(); i++) {
				Node message = items.item(i);
				String nodeName = message.getNodeName();
				if (nodeName != null && nodeName.equals("message")) {
					// 属性列表
					NodeList properties = message.getChildNodes();
					TransactionElements transactionEle = new TransactionElements();
					for (int k = 0; k < properties.getLength(); k++) {
						Node messageProperty = properties.item(k);
						String propertyValue = messageProperty.getNodeName();

						if (propertyValue.equals("#text"))
							continue;
						else if (propertyValue.equals("messageType")) {
							if (messageProperty.getFirstChild() != null) {
								transactionEle.setMessageType(messageProperty.getFirstChild().getNodeValue());
							}
						} else if (propertyValue.equals("processCode")) {
							if (messageProperty.getFirstChild() != null) {
								transactionEle.setProcessCode(messageProperty.getFirstChild().getNodeValue());
							}

						} else if (propertyValue.equals("serviceConditionCode")) {
							if (messageProperty.getFirstChild() != null) {
								transactionEle.setServiceConditionCode(messageProperty.getFirstChild().getNodeValue());
							}

						} else if (propertyValue.equals("messageTypeCode")) {
							if (messageProperty.getFirstChild() != null) {
								transactionEle.setMessageTypeCode(messageProperty.getFirstChild().getNodeValue());
							}

						} else if (propertyValue.equals("netManagementInfoCode")) {
							if (messageProperty.getFirstChild() != null) {
								transactionEle.setNetManagementInfoCode(messageProperty.getFirstChild().getNodeValue());
							}

						} else if (propertyValue.equals("comments")) {
							if (messageProperty.getFirstChild() != null) {
								transactionEle.setComments(messageProperty.getFirstChild().getNodeValue());
							}

						} else if (propertyValue.equals("customNum")) {
							if (messageProperty.getFirstChild() != null) {
								transactionEle.setCustomNum(messageProperty.getFirstChild().getNodeValue());
							}

						} else if (propertyValue.equals("mfield")) {
							if (messageProperty.getFirstChild() != null) {
								transactionEle.setMfields(messageProperty.getFirstChild().getNodeValue());
							}

						} else if (propertyValue.equals("cfield")) {
							if (messageProperty.getFirstChild() != null) {
								transactionEle.setCfields(messageProperty.getFirstChild().getNodeValue());
							}

						}
					}
					if (transactionEle != null) {
						allTradeTypeMap.put(transactionEle.getCustomNum(), transactionEle);
					}
				}
			}

			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public Map<String, DataFormat> getDataFormatList() {
		return dataFormatList;
	}

	@Override
	public DataFormat getDataFormat(String fieldTag) {
		if (dataFormatList != null && !dataFormatList.isEmpty()) {
			return dataFormatList.get(fieldTag);
		}
		return null;
	}

	public void init() {
		handerList.clear();
	}

	/**
	 * 责任链添加handler
	 * 
	 * @param hander
	 */
	public void addHander(AbstructHandler hander) {
		if (handerList.isEmpty()) {
			hander.setHanderObserver(this);
			handerList.add(hander);
			return;
		}
		hander.setHanderObserver(this);
		handerList.get(handerList.size() - 1).setNextHandler(hander);
		handerList.add(hander);
		return;
	}

	/**
	 * 启动责任链
	 * 
	 * @param eventType
	 * @param message
	 * @return
	 */
	public boolean startDataParseChain(String message) {
		if (handerList.isEmpty()) {
			System.out.println("chain is empty");
			return false;
		}
		if (chainRunning) {
			System.out.println("chain is already in running state");
			return false;
		}
		chainRunning = true;
		singleFieldParseResultList = new ArrayList<>();
		dataParseResult = new MessageParseResult();
		handerList.get(0).handleEvent(message);
		return true;
	}

	/**
	 * 清空责任链
	 * 
	 * @return
	 */
	public boolean resetChain() {
		if (chainRunning) {
			System.out.println("chain is already in running state");
			return false;
		}
		handerList.clear();
		return true;
	}

	public boolean changeFieldModel(String fieldTag, Object fieldModel) {
		if (!(fieldModel instanceof DataFormat)) {
			return false;
		}
		getDataFormatList().put(fieldTag, (DataFormat) fieldModel);
		return true;
	}

	@Override
	void finishNotified(boolean result, String remarks) {
		// chain run over
		chainRunning = false;
		resetFieldModel();
		System.out.println("chain runs over");
		if (getDataParseResult() != null) {
			getDataParseResult().setSuccess(result);
			getDataParseResult().setResultRemarks(remarks);
			getDataParseResult().setResultList(singleFieldParseResultList);
			// 判断报文类型
			TransactionElements targetTransaction = checkTransaction(getDataParseResult(), getAllTradeTypeMap());
			getDataParseResult().setTransactionType(targetTransaction);

		}
	}

	@Override
	void addNextHandlerNotified(String dataFormatTag) {
		if (dataFormatList != null) {
			if (dataFormatTag != null) {
				addHander(new FieldParseHandler(dataFormatTag));
			}
		}
	}

	@Override
	void singleFinishNotified(SingleFieldParseResult singleFieldParseResult) {
		String fieldTag = singleFieldParseResult.getSingleFieldData().getDataFormat().getTag();
		if (singleFieldParseResultList != null) {
			singleFieldParseResultList.add(singleFieldParseResult);
		}
		if (DataUtils.isNumeric(fieldTag)) {
			int fieldNumber = Integer.valueOf(fieldTag);
			if (fieldNumber == 60) {
				// 解析到60域就能确定交易类型了
				System.out.println("60域解析完成，尝试解析报文所属交易类型");
				MessageParseResult tempMessageParseResult = new MessageParseResult();
				tempMessageParseResult.setResultList(singleFieldParseResultList);
				TransactionElements tempTransactionElements = checkTransaction(tempMessageParseResult,
						getAllTradeTypeMap());
				if (tempTransactionElements != null) {
					System.out.println("交易类型:" + tempTransactionElements.getComments());
					// 根据交易类型改变62域的模板格式
					DataFormat targetDataFormat = new DataFormat();
					targetDataFormat.setTag("62");
					targetDataFormat.setComments("62域自定义域");
					targetDataFormat.setLenEncode("BCD");
					targetDataFormat.setLenLen(3);
					targetDataFormat.setDataMaxLen(512);
					targetDataFormat.setDataEncode("ASCII");
					int customerNumber = Integer.valueOf(tempTransactionElements.getCustomNum());
					switch (customerNumber) {
					case 126:
					case 128:
					case 130:
					case 170:// 积分签到
					case 218:
						// 签到应答，用法一:终端密钥
						// 三倍长秘钥规范中未涉及，故目前不能按照规范给出具体的最大长度限制
						// targetDataFormat.setDataMaxLen(84);
						targetDataFormat.setDataEncode("BINARY");
						targetDataFormat.setComments("工作秘钥信息");
						break;
					case 163:// POS状态上送请求,用法二:终端状态信息
					case 132:// POS参数传递(磁条卡交易)应答,用法三:终端参数信息
						targetDataFormat.setDataMaxLen(160);
						break;
					case 93:
					case 95:
						// 基于PBOC电子钱包/存折标准的IC卡圈存批结处理报文请求,用法五:基于PBOC电子钱包标准的圈存确认明细
						targetDataFormat.setDataMaxLen(53);
						break;
					case 83:
						// IC卡联机交易(完全成功交易)明细上送报文请求,用法6:基于PBOC借/贷记标准的交易明细
						targetDataFormat.setDataMaxLen(21);
						break;
					case 85:
						// (IC卡离线交易失败,IC卡卡片认证ARPC错仍同意交易)明细上送报文请求,用法7:基于PBOC借/贷记标准的通知明细
						targetDataFormat.setDataMaxLen(23);
						break;
					case 136:
						// POS参数传递(TMS参数下载)应答,用法8:TMS参数
						targetDataFormat.setDataMaxLen(396);
						break;
					case 29:
						// 消费（分期付款）请求,用法9:分期付款请求信息
						targetDataFormat.setDataMaxLen(62);
						break;
					case 51:
					case 52:
					case 55:
					case 56:
						// 磁条卡账户充值(转账)请求/应答;电子现金非指定账户转账圈存报文请求/应答,用法10:转入账户标识
						targetDataFormat.setDataMaxLen(28);
						break;
					case 30:
						// 消费(分期付款)应答,用法11:分期付款应答信息
						targetDataFormat.setDataMaxLen(77);
						break;
					case 57:
					case 58:
					case 3:
					case 21:
					case 9:
						// 用法12:持卡人身份认证信息
						// 一次性付款消费,预约消费,订购消费请求/应答
						// 一次性付款消费冲正请求
						// 订购预授权请求
						// 订购预授权完成请求
						// 磁条卡现金充值账户验证请求
						targetDataFormat.setDataMaxLen(68);
						break;
					case 33:
						// 积分消费请求,用法13:积分兑换业务请求信息
						targetDataFormat.setDataMaxLen(60);
						break;
					case 34:
						// 积分消费应答,用法14:积分兑换业务应答
						targetDataFormat.setDataMaxLen(72);
						break;
					case 54:
					case 11:
					case 15:
					case 49:
						// 磁条预付费卡现金充值应答,用法16:磁条卡现金充值应答
						// 余额查询请求,用法17:行业卡专用信息
						// 普通消费请求(当需要携带行业卡信息的时候),用法17:行业卡专用信息
						// 电子现金现金充值请求,用法17:行业卡专用信息
						targetDataFormat.setDataMaxLen(50);
						break;
					case 65:
						// IC卡脱机退货请求,用法18:原始交易终端号
						targetDataFormat.setLenLen(null);
						targetDataFormat.setLenEncode(null);
						targetDataFormat.setDataMaxLen(null);
						targetDataFormat.setDataLen(8);
						targetDataFormat.setDataEncode("ASCII");
						break;
					case 125:
					case 127:
					case 129:
					case 169:// 积分签到
					case 217:
						// 签到请求，用法十九
						targetDataFormat.setLenLen(2);
						targetDataFormat.setDataMaxLen(50);
						break;

					default:
						break;
					}
					changeFieldModel("62", targetDataFormat);
				}
				System.out.println("62域格式:" + getDataFormatList().get("62").toString());
			}
		}
	}

	public TransactionElements checkTransaction(MessageParseResult messageParseResult,
			Map<String, TransactionElements> transactionElementsMap) {
		try {
			if (messageParseResult == null || transactionElementsMap == null) {
				return null;
			}
			String targetMessageType = null;
			String targetProcessCode = null;
			String targetServiceConditionCode = null;
			String targetMessageTypeCode = null;
			String targetNetManagementInfoCode = null;

			List<?> fieldList = messageParseResult.getResultList();
			if (fieldList == null || fieldList.isEmpty()) {
				return null;
			}
			// 遍历数据获取判断交易类型的关键值
			for (Object field : fieldList) {
				String fieldTag = ((SingleFieldParseResult) field).getSingleFieldData().getDataFormat().getTag();
				String fieldData = ((SingleFieldParseResult) field).getSingleFieldData().getParsedData();
				if ("MSGTYPE".equalsIgnoreCase(fieldTag)) {
					targetMessageType = fieldData;
				} else if ("3".equalsIgnoreCase(fieldTag)) {
					targetProcessCode = fieldData.substring(0, 2);
				} else if ("25".equalsIgnoreCase(fieldTag)) {
					targetServiceConditionCode = fieldData;
				} else if ("60".equalsIgnoreCase(fieldTag)) {
					targetMessageTypeCode = fieldData.substring(0, 2);
					if (fieldData.length() >= 11) {
						targetNetManagementInfoCode = fieldData.substring(8, 11);
					}
				} else if ("MSGTYPE".equalsIgnoreCase(fieldTag)) {

				}
			}

			Set<String> keySet = transactionElementsMap.keySet();
			for (String key : keySet) {
				TransactionElements tempEle = transactionElementsMap.get(key);
				String tempMessageType = tempEle.getMessageType();
				String tempProcessCode = tempEle.getProcessCode();
				String tempServiceConditionCode = tempEle.getServiceConditionCode();
				String tempMessageTypeCode = tempEle.getMessageTypeCode();
				String tempNetManagementInfoCode = tempEle.getNetManagementInfoCode();
				if (checkMatch(targetMessageType, tempMessageType) && checkMatch(targetProcessCode, tempProcessCode)
						&& checkMatch(targetServiceConditionCode, tempServiceConditionCode)
						&& checkMatch(targetMessageTypeCode, tempMessageTypeCode)
						&& checkMatch(targetNetManagementInfoCode, tempNetManagementInfoCode)) {
					System.out.println("匹配成功");
					return tempEle;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean checkMatch(String str1, String str2) {
		if (str1 == null || "".equals(str1.trim())) {
			if (str2 == null || "".equals(str2.trim())) {
				return true;
			}
		} else {
			if (str1.equalsIgnoreCase(str2) || str2 == null || "".equals(str2.trim())) {
				return true;
			}
		}
		return false;
	}
}

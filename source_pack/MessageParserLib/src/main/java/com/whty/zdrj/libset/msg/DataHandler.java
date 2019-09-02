package com.whty.zdrj.libset.msg;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.whty.zdrj.libset.msg.utils.BitMapUitls;
import com.whty.zdrj.libset.msg.utils.CustomLogger;
import com.whty.zdrj.libset.msg.utils.FunctionUtils;

/**
 * 数据转换器
 * 
 * @author OneWay
 * 
 */
public class DataHandler implements IDataHandler {

	// 查询规则表的key
	private String lookupKey = null;
	private boolean allowLogOut = false;

	private CustomLogger logger = CustomLogger.getLogger(this.getClass());

	public String getLookupKey() {
		return lookupKey;
	}

	public void setLookupKey(String lookupKey) {
		this.lookupKey = lookupKey;
	}

	public DataHandler(String lookupKey) {
		super();
		this.lookupKey = lookupKey;
	}

	public List<?> parseData(StringBuffer data) {
		List<?> p = PatternTables.getPattern(lookupKey);
		if (p == null)
			return null;

		List<ParseElement> list = processParse(lookupKey, data, p);

		return list;
	}

	public String rebuildData(List<?> listdata) {
		List<?> p = PatternTables.getPattern(lookupKey);
		if (p == null)
			return null;

		String result = reBuild(lookupKey, p, listdata);

		return result;
	}

	public String buildData(Map<String, Object> data) {

		List<?> p = PatternTables.getPattern(lookupKey);
		if (p == null)
			return null;

		String result = processBuild(lookupKey, p, data);

		return result;
	}

	@SuppressWarnings("unchecked")
	private List<ParseElement> processParse(String lookupKey, StringBuffer sb, List<?> p) {

		if (p == null || p.size() == 0)
			return null;

		if (lookupKey == null || lookupKey.equals(""))
			return null;

		if (sb == null || sb.toString().trim().length() == 0)
			return null;

		List<ParseElement> parseList = new ArrayList<ParseElement>();

		ParseElement bitMap = null;
		if (lookupKey.startsWith(KeyFactory.MSG_8583_KEY)) {
			for (int i = 0; i < p.size(); i++) {
				ParseElement e = (ParseElement) p.get(i);

				// 普通node结构
				if (e.getTargetClass() == null) {

					ParseElement mirror = e.clone();
					int len = 0;

					if (e.getLllvar() != null && !e.getLllvar().equals("")) {
						len = Integer.valueOf(e.getLllvar()) / 2 * 2;
					} else {
						String lenStr = e.getLen();
						len = Integer.valueOf(lenStr);
					}

					String targetValue = sb.substring(0, Integer.valueOf(len));
					mirror.setValue(targetValue);
					showLog(mirror.getComments() + ":" + targetValue);
					sb = new StringBuffer(sb.substring(Integer.valueOf(len)));

					if (e.getId() != null && Integer.valueOf(e.getId()) == 1) {
						bitMap = mirror;
						break;
					}

					parseList.add(mirror);
				} else {
					// 复杂类型
					try {
						Class<?> targetClass = Class.forName(e.getTargetClass());
						if (targetClass == null)
							return null;
						String parseMethodName = e.getParseMethod();
						String parseParams = e.getParseMethodParam();

						String[] params = parseParams.split(",");

						Class<?>[] classArray = new Class<?>[params.length];
						for (int c = 0; c < params.length; c++) {
							classArray[c] = Class.forName(params[c]);
						}

						Method parseMethod = targetClass.getDeclaredMethod(parseMethodName, classArray);
						if (parseMethod != null) {
							List<Object> peList = (List<Object>) parseMethod.invoke(targetClass.newInstance(),
									new Object[] { e, sb });
							for (int j = 0; j < peList.size(); j++) {
								if (j == 0) {
									sb = (StringBuffer) peList.get(j);
								} else {
									parseList.add((ParseElement) peList.get(j));
								}
							}
						}

					} catch (Exception e2) {
						e2.printStackTrace();
					}

				}
			}
		}
		// 非擎动才会执行此段代码
		if (bitMap != null) {
			int[] maps = BitMapUitls.getMessageUnit(bitMap.getValue());

			for (int i = 0; i < maps.length; i++) {
				ParseElement e = getMessageUnit(maps[i], p);

				if (e == null) {
					// Log.d("DataHandler", "域" + maps[i] + "未找到");
					showLog("DataHandler：" + "域" + maps[i] + "未找到");
					// Log.d(tag, "当前报文配置无该位元信息,报文解析终止.");
					showLog("DataHandler:" + "当前报文配置无该位元信息,报文解析终止.");
					return null;
					// throw new NullPointerException("未知位元信息");
				}

				ParseElement mirror = e.clone();
				int realLen = Integer.valueOf(e.getLen());
				int subLen = realLen / 2 * 2;

				int subLenOfLength = 0;
				int valueLen = -1;
				// String llenValue = null;
				if (mirror.getLllvar() != null) {
					subLenOfLength = Integer.valueOf(mirror.getLllvar());
					valueLen = Integer.valueOf(sb.substring(0, subLenOfLength).toString());
				}

				String unitValue = null;
				if (valueLen != -1) {
					int rvalueLen = 0;
					if (mirror.getCode().equalsIgnoreCase("binary") || mirror.getCode().equalsIgnoreCase("ascii"))
						rvalueLen = valueLen * 2;
					else if (mirror.getCode().equalsIgnoreCase("bcd"))
						rvalueLen = (valueLen % 2 == 0 ? valueLen : valueLen + 1);
					unitValue = sb.substring(subLenOfLength, subLenOfLength + rvalueLen);
					mirror.setValueLength(sb.substring(0, subLenOfLength));
					sb = new StringBuffer(sb.substring(subLenOfLength + rvalueLen));
				} else {
					unitValue = sb.substring(0, Integer.valueOf(subLen));
					sb = new StringBuffer(sb.substring(subLen));
				}
				String showValue = unitValue;
				// 域
				final int msgID = Integer.valueOf(mirror.getId());
				switch (msgID) {
				// ASCII编码的域
				case 39:
					unitValue = FunctionUtils.hexByteStringToStr(unitValue);
					break;
				case 37:
				case 41:
				case 42:
				case 44:
				case 49:
					showValue = FunctionUtils.hexByteStringToStr(unitValue);
					break;

				default:
					break;
				}
				mirror.setValue(unitValue);

				showLog(mirror.getComments() != null ? mirror.getComments() + ":" + showValue
						: mirror.getId() + "域" + ":" + showValue);
				parseList.add(mirror);
			}
		}
		return parseList;
	}

	/**
	 * 获取位元信息
	 * 
	 * @param i
	 * @param p
	 * @return
	 */
	private ParseElement getMessageUnit(int id, List<?> p) {

		for (int i = 0; i < p.size(); i++) {
			ParseElement el = (ParseElement) p.get(i);
			if (el.getId() != null && el.getId().equals(String.valueOf(id))) {
				return el;
			}
		}

		return null;
	}

	/**
	 * 构建数据格式
	 * 
	 * @param lookupKey2
	 * @param p
	 * @param data
	 * @return
	 */
	private String processBuild(String lookupKey2, List<?> p, Map<String, Object> data) {
		if (p == null || p.size() == 0)
			return null;

		if (lookupKey == null || lookupKey.equals(""))
			return null;

		if (data == null || data.size() == 0)
			return null;

		StringBuffer builder = new StringBuffer();

		if (lookupKey.startsWith(KeyFactory.MSG_8583_KEY)) {
			String MSGType = "";
			String tempInfo = "";
			StringBuffer mapInfo = new StringBuffer();
			for (int i = 0; i < p.size(); i++) {
				ParseElement rule = (ParseElement) p.get(i);
				if (rule != null) {

					// 普通规则的拼装
					if (rule.getTargetClass() == null) {
						String tag = rule.getTag();
						if (tag == null || tag.equals("")) {
							if (rule.getId() != null && !rule.getId().equals("")) {
								tempInfo = rule.getId() + "|";
								// tag = "id_" + rule.getId();
								tag = rule.getId();
							} else
								tag = null;
						}

						if (tag != null) {
							String value = (String) data.get(tag);
							if (value != null) {
								if (tag.equals("MSGType"))
									MSGType = value;
								else {
									mapInfo.append(tempInfo);
									builder.append(value);
								}
							}
						}
					} else {
						// 复杂结构的拼装
						try {
							Class<?> targetClass = Class.forName(rule.getTargetClass());
							if (targetClass == null)
								return null;
							String buildMethodName = rule.getBuildMethod();
							String buildParams = rule.getBuildMethodParam();
							String[] params = buildParams.split(",");
							Class<?>[] classArray = new Class<?>[params.length];
							for (int c = 0; c < params.length; c++) {
								classArray[c] = Class.forName(params[c]);
							}
							Method buildMethod = targetClass.getDeclaredMethod(buildMethodName, classArray);
							if (buildMethod != null) {
								String ret = (String) buildMethod.invoke(targetClass.newInstance(),
										new Object[] { rule, data });
								builder.append(ret);
							}
						} catch (Exception e2) {
							e2.printStackTrace();
						}

					}
				}
			}
			// 当传入的数据是从"消息类型"开始的时候
			if (MSGType != null && MSGType.length() != 0) {
				builder.insert(0, BitMapUitls.getBitMap(mapInfo.toString()));
				builder.insert(0, MSGType);
			}

		}

		return builder.toString();
	}

	/**
	 * 重组报文
	 * 
	 * @param lookupKey
	 *            报文模板对应的key
	 * @param p
	 *            报文格式list
	 * @param data
	 *            解析完毕的报文数据
	 * @return
	 */
	private String reBuild(String lookupKey, List<?> p, List<?> data) {
		if (p == null || p.size() == 0)
			return null;

		if (lookupKey == null || lookupKey.equals(""))
			return null;

		if (data == null || data.size() == 0)
			return null;

		if (data.get(0).getClass() != ParseElement.class) {
			showLog("请求数据列表类型错误");
			return null;
		}
		StringBuffer builder = new StringBuffer();

		if (lookupKey.startsWith(KeyFactory.MSG_8583_KEY)) {
			String MSGType = "";
			String tempInfo = "";
			StringBuffer mapInfo = new StringBuffer();
			for (int i = 0; i < p.size(); i++) {
				ParseElement rule = (ParseElement) p.get(i);
				if (rule != null) {

					// 普通规则的拼装
					if (rule.getTargetClass() == null) {
						String tag = rule.getTag();
						if (tag == null || tag.equals("")) {
							if (rule.getId() != null && !rule.getId().equals("")) {
								tempInfo = rule.getId() + "|";
								// tag = "id_" + rule.getId();
								tag = rule.getId();
							} else
								tag = null;
						}
						// 数据中包含对应的域信息
						if (tag != null) {
							// String value = (String) data.get(tag);
							String value = null;
							for (int j = 0; j < data.size(); j++) {
								ParseElement mirror = (ParseElement) data.get(j);
								if (tag.equals(mirror.getId()) || tag.equals(mirror.getTag())) {
									value = mirror.getValue();
									// int len = 0;

									if (mirror.getLllvar() != null && !mirror.getLllvar().equals("")) {
										// 变长
										builder.append(mirror.getValueLength());
									}
								}
							}
							if (value != null) {
								if (tag.equals("MSGType"))
									MSGType = value;
								else {
									mapInfo.append(tempInfo);
									builder.append(value);
								}
							}
						}
					}
				}
			}
			// 当传入的数据是从"消息类型"开始的时候
			if (MSGType != null && MSGType.length() != 0) {
				builder.insert(0, BitMapUitls.getBitMap(mapInfo.toString()));
				builder.insert(0, MSGType);
			}

		}

		return builder.toString();
	}

	public MessageParseResult parseStandard8583MSG(StringBuffer data) {
		MessageParseResult result = new MessageParseResult();
		List<?> p = PatternTables.getPattern(lookupKey);
		if (p == null) {
			showLog("使用提供的报文模板key未成功找到对应的模板");
			result.setResultCode("09");
			return result;
		}

		result = process8583MSGParse(lookupKey, data, p);

		return result;
	}

	public String buildStandard8583MSG(List<?> listdata) {
		List<?> p = PatternTables.getPattern(lookupKey);
		if (p == null)
			return null;

		String result = process8583MSGBuild(lookupKey, p, listdata);

		return result;
	}

	public String buildStandard8583MSG(Map<String, Object> mapData) {

		List<?> p = PatternTables.getPattern(lookupKey);
		if (p == null)
			return null;

		String result = process8583MSGBuild(lookupKey, p, mapData);

		return result;
	}

	public void setLogPrint(boolean allowLogOut) {
		this.allowLogOut = allowLogOut;
	}

	public boolean isLogPrintPermitted() {
		return this.allowLogOut;
	}

	private MessageParseResult process8583MSGParse(String lookupKey, StringBuffer sb, List<?> p) {
		MessageParseResult result = new MessageParseResult();
		// 判断报文是退货请求的时候，62域格式变化
		if (p == null || p.size() == 0) {
			showLog("数据解析模板为空");
			result.setResultCode("01");
			return result;
		}

		if (lookupKey == null || lookupKey.equals("")) {
			showLog("模板标识符为空");
			result.setResultCode("02");
			return result;
		}

		if (sb == null || sb.toString().equals("")) {
			showLog("数据长度有误,长度:0");
			result.setResultCode("03");
			return result;
		}
		String msgType = null;
		// 3域交易处理码
		String handleCode = null;
		List<ParseElement> parseList = new ArrayList<ParseElement>();
		try {
			ParseElement bitMap = null;
			if (!lookupKey.startsWith(KeyFactory.MSG_8583_KEY)) {
				showLog("不支持的报文模板格式");
				result.setResultCode("10");
				return result;
			}
			// 获取报文头以及位元表数据
			for (int i = 0; i < p.size(); i++) {

				ParseElement curElement = (ParseElement) p.get(i);

				int lengthFlagLen = 0;
				int len = 0;

				// 解析长度(定长&变长)
				String lllVar = curElement.getLllvar();
				if (lllVar == null || lllVar.trim().equals("")) {
					// 定长
					String lenStr = curElement.getLen();
					len = Integer.valueOf(lenStr);
				} else {
					// 变长
					try {
						lengthFlagLen = (Integer.valueOf(lllVar) + 1) / 2 * 2;
					} catch (Exception e) {
						showLog("模板变长域长度定义有误");
						result.setResultCode("11");
						return result;
					}
					if (sb.length() < lengthFlagLen) {
						showLog("请求数据长度有误(短)，长度:" + sb.length());
						result.setResultCode("20");
						return result;
					}
					curElement.setValueLength(sb.substring(0, lengthFlagLen));
					try {
						len = Integer.valueOf(sb.substring(0, lengthFlagLen));
					} catch (Exception e) {
						showLog("数据有误,无法获取正确的长度字段");
						result.setResultCode("21");
						return result;
					}
					sb = new StringBuffer(sb.substring(lengthFlagLen));
				}
				curElement.setLen(len + "");

				// 解析数据
				String valueCode = curElement.getCode();
				String targetValue = null;
				if (valueCode == null || valueCode.length() == 0) {
					showLog("模板未定义value编码模式");
					result.setResultCode("12");
					return result;
				} else if (valueCode.equalsIgnoreCase("binary") || (valueCode.equalsIgnoreCase("ascii"))) {
					if (sb.length() < len * 2) {
						showLog("请求数据长度有误(短)，长度:" + sb.length());
						result.setResultCode("20");
						return result;
					}
					targetValue = sb.substring(0, len * 2);
				} else if (valueCode.equalsIgnoreCase("bcd")) {
					if (sb.length() < (len + 1) / 2 * 2) {
						showLog("请求数据长度有误(短)，长度:" + sb.length());
						result.setResultCode("20");
						return result;
					}
					targetValue = sb.substring(0, (len + 1) / 2 * 2);
				} else {
					showLog("未知的域编码:" + valueCode);
					result.setResultCode("13");
					return result;
				}
				curElement.setValue(targetValue);
				if ("MSGType".equalsIgnoreCase(curElement.getTag())) {
					msgType = targetValue;
				}
				showLog(curElement.getComments() + ":" + targetValue + "  长度:" + curElement.getLen() + "字节" + "  编码:"
						+ curElement.getCode());
				sb = new StringBuffer(sb.substring(Integer.valueOf((targetValue.length() + 1) / 2 * 2)));

				parseList.add(curElement);
				result.setResultList(parseList);
				if (curElement.getId() != null && Integer.valueOf(curElement.getId()) == 1) {
					// 遇到位元表的时候跳出for循环
					bitMap = curElement;
					break;
				}

			}
			/**
			 * 解析位元表以及后续数据
			 */
			if (bitMap == null) {
				showLog("数据中未获取到位元表信息");
				result.setResultCode("22");
				return result;
			}
			int[] maps = BitMapUitls.getMessageUnit(bitMap.getValue());
			if (maps == null || maps.length == 0) {
				showLog("位元表解析失败");
				result.setResultCode("23");
				return result;
			}
			// 打印解析的位元表信息
			StringBuffer messageUnit = new StringBuffer();
			for (int temp : maps) {
				messageUnit.append(temp + ",");
			}
			showLog("位元表解析:" + messageUnit.toString());
			for (int i = 0; i < maps.length; i++) {
				// 检查配置文件时候存在各域配置信息
				ParseElement curElement = getMessageUnit(maps[i], p);
				if (curElement == null) {
					showLog("DataHandler：" + "域" + maps[i] + "未找到");
					showLog("DataHandler:" + "当前报文配置无该位元信息,报文解析终止.");
					result.setResultCode("14");
					return result;
				}
				ParseElement tempEle = curElement.clone();
				if (maps[i] == 62) {
					if ("0220".equals(msgType) && "20".equals(handleCode)) {
						// 退货请求的时候，62域格式改变
						tempEle.setLllvar(null);
						tempEle.setLllcode(null);
						tempEle.setLen("8");
					}
				}
				int lengthFlagLen = 0;
				int len = 0;

				// 请求数据解析
				String lllVar = tempEle.getLllvar();
				if (lllVar == null || lllVar.trim().equals("")) {
					// 定长
					String lenStr = tempEle.getLen();
					len = Integer.valueOf(lenStr);
				} else {
					// 变长
					try {
						lengthFlagLen = (Integer.valueOf(lllVar) + 1) / 2 * 2;
					} catch (Exception e) {
						showLog("模板变长域长度定义有误");
						result.setResultCode("11");
						return result;
					}
					// showLog("长度标志位：" + lengthFlagLen);
					if (sb.length() < lengthFlagLen) {
						showLog("请求数据长度有误(短)，长度:" + sb.length());
						result.setResultCode("20");
						return result;
					}
					tempEle.setValueLength(sb.substring(0, lengthFlagLen));
					try {
						len = Integer.valueOf(sb.substring(0, lengthFlagLen));
					} catch (Exception e) {
						showLog("数据有误,无法获取正确的长度字段");
						result.setResultCode("21");
						return result;
					}
					sb = new StringBuffer(sb.substring(lengthFlagLen));
				}
				tempEle.setLen(len + "");

				String valueCode = tempEle.getCode();
				String targetValue = null;
				if (valueCode == null || valueCode.length() == 0) {
					showLog("模板未定义value编码模式");
					result.setResultCode("12");
					return result;
				} else if (valueCode.equalsIgnoreCase("binary") || valueCode.equalsIgnoreCase("ascii")) {
					if (sb.length() < len * 2) {
						showLog("请求数据长度有误(短)，长度:" + sb.length());
						result.setResultCode("20");
						return result;
					}
					targetValue = sb.substring(0, len * 2);
				} else if (valueCode.equalsIgnoreCase("bcd")) {
					if (sb.length() < (len + 1) / 2 * 2) {
						showLog("请求数据长度有误(短)，长度:" + sb.length());
						result.setResultCode("20");
						return result;
					}
					// targetValue = sb.substring(0, len);
					targetValue = sb.substring(0, (len + 1) / 2 * 2);
					String tagID = tempEle.getId();
					if (tagID != null) {
						int id = Integer.valueOf(tagID);
						// 卡片序列号，数据右靠BCD码
						if (len % 2 > 0) {
							if (sb.length() < len + 1) {
								showLog("请求数据长度有误(短)，长度:" + sb.length());
								result.setResultCode("20");
								return result;
							}
							if (id == 23) {
								targetValue = sb.substring(1, len + 1);
							} else {
								targetValue = sb.substring(0, len);
							}
						}
					}
				} else {
					showLog("未知的域编码:" + valueCode);
					result.setResultCode("13");
					return result;
				}
				tempEle.setValue(targetValue);
				if ("3".equals(tempEle.getId())) {
					// 交易处理码前2位
					handleCode = tempEle.getValue().substring(0, 2);
				}
				parseList.add(tempEle);
				result.setResultList(parseList);
				showLog(tempEle.getComments() + ":" + targetValue + "  长度:" + tempEle.getLen() + "字节" + "  编码:"
						+ tempEle.getCode()
						+ (tempEle.getValueLength() == null ? "" : "  长度域:" + tempEle.getValueLength()));
				sb = new StringBuffer(sb.substring(Integer.valueOf((targetValue.length() + 1) / 2 * 2)));

			}
		} catch (Exception e) {
			e.printStackTrace();
			showLog("解析数据时异常");
			result.setResultCode("99");
			return result;
		}
		result.setSuccess(true);
		result.setResultCode("00");
		return result;
	}

	/**
	 * 重组报文
	 * 
	 * @param lookupKey
	 *            报文模板对应的key
	 * @param p
	 *            报文格式list
	 * @param data
	 *            解析完毕的报文数据
	 * @return
	 */
	private String process8583MSGBuild(String lookupKey, List<?> p, List<?> data) {
		if (p == null || p.size() == 0)
			return null;

		if (lookupKey == null || lookupKey.equals(""))
			return null;

		if (data == null || data.size() == 0)
			return null;

		if (data.get(0).getClass() != ParseElement.class) {
			showLog("请求数据列表类型错误");
			return null;
		}

		StringBuffer builder = new StringBuffer();

		if (lookupKey.startsWith(KeyFactory.MSG_8583_KEY)) {
			String MSGType = "";
			String tempInfo = "";
			StringBuffer mapInfo = new StringBuffer();
			for (int i = 0; i < p.size(); i++) {
				ParseElement curElement = (ParseElement) p.get(i);
				if (curElement == null) {
					return null;
				}
				// 只有消息类型跟位元表才有tag
				String curTag = curElement.getTag();
				String curId = curElement.getId();
				String value = null;
				// 记录报文消息类型
				if (curTag != null && curTag.equalsIgnoreCase("MSGType")) {
					for (int j = 0; j < data.size(); j++) {
						ParseElement element = (ParseElement) data.get(j);
						if (curTag.equals(element.getTag())) {
							MSGType = element.getValue();

							if (element.getLllvar() != null && !element.getLllvar().equals("")) {
								// 变长
								MSGType = element.getValueLength() + MSGType;
							}
							break;
						}
					}
				} else {
					if (curId != null && !curId.equals("")) {
						int id = 0;
						try {
							id = Integer.valueOf(curId);
						} catch (Exception e) {
							showLog("配置文件ID配置有误");
							return null;
						}
						if (id < 2) {
							continue;
						}
						tempInfo = id + "|";
						for (int j = 0; j < data.size(); j++) {
							ParseElement element = (ParseElement) data.get(j);
							if (curId.equals(element.getId())) {
								value = element.getValue();
								if (value.length() % 2 != 0) {
									if ("23".equals(curId)) {
										value = "0" + value;
									} else {
										value += "0";
									}
								}

								if (element.getLllvar() != null && !element.getLllvar().equals("")) {
									// 变长
									builder.append(element.getValueLength() + "");
								}
								break;
							}
						}
						if (value != null) {
							mapInfo.append(tempInfo);
							builder.append(value);
						}
					}
				}
			}
			// 当传入的数据是从"消息类型"开始的时候
			if (MSGType != null && MSGType.length() != 0) {
				builder.insert(0, BitMapUitls.getBitMap(mapInfo.toString()));
				builder.insert(0, MSGType);
			}

		}

		return builder.toString();
	}

	private String process8583MSGBuild(String lookupKey2, List<?> p, Map<String, Object> data) {
		return processBuild(lookupKey2, p, data);
	}

	private void showLog(String logInfo) {
		if (isLogPrintPermitted()) {
			// System.out.println(logInfo);
			logger.debug(logInfo);
		}
	}

}

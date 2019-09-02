package com.whty.zdrj.libset.msg;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.whty.zdrj.libset.data.model.TransactionElements;
import com.whty.zdrj.libset.msg.utils.CustomLogger;
import com.whty.zdrj.libset.msg.utils.SAXParserHandler;

public class MessageParseResult {
	private boolean success;
	private String resultCode;
	private String resultRemarks;
	private List<?> resultList;

	// 报文所属交易类型
	private TransactionElements transactionType;

	private static CustomLogger logger = CustomLogger.getLogger(MessageParseResult.class.getSimpleName());

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultRemarks() {
		return resultRemarks;
	}

	public void setResultRemarks(String resultRemarks) {
		this.resultRemarks = resultRemarks;
	}

	public List<?> getResultList() {
		return resultList;
	}

	public void setResultList(List<?> resultList) {
		this.resultList = resultList;
	}

	public TransactionElements getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionElements transactionType) {
		this.transactionType = transactionType;
	}

	public static class ResultCodeMatcher {
		private static SAXParserHandler handler = new SAXParserHandler();

		static {
			setResultDescriptionConfigLocation(
					ResultCodeMatcher.class.getClassLoader().getResourceAsStream("template/iso8583-rescode.xml"));
		}

		public static boolean setResultDescriptionConfigLocation(Object configFileUri) {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			try {
				SAXParser parser = factory.newSAXParser();
				String uri = null;
				InputStream is = null;
				if (configFileUri != null) {
					if (configFileUri instanceof String) {
						uri = (String) configFileUri;
						logger.debug("config file location:" + uri);
						parser.parse(uri, handler);
					} else if (configFileUri instanceof InputStream) {
						is = (InputStream) configFileUri;
						logger.debug("config file input stream");
						parser.parse(is, handler);
					}
				}
				if (uri == null && is == null) {
					logger.debug("no config file found");
					return false;
				}
				logger.debug("工程配置中总共有" + handler.getResultCodeList().size() + "个结果码配置信息");
				return true;
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}

		public static String getDescription(String resultCode) {
			if (handler.getResultCodeList().size() == 0) {
				return null;
			}
			try {
				for (ResultInstruction tempResult : handler.getResultCodeList()) {
					if (tempResult.getCode().equalsIgnoreCase(resultCode)) {
						logger.debug("结果码匹配成功:" + tempResult.getCode() + "_" + tempResult.getComments());
						return tempResult.getComments();
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
}

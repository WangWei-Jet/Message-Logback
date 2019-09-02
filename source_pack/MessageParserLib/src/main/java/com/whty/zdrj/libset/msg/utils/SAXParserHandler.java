package com.whty.zdrj.libset.msg.utils;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.whty.zdrj.libset.msg.ResultInstruction;

public class SAXParserHandler extends DefaultHandler {
	String value = null;
	ResultInstruction resultCode = null;
	private ArrayList<ResultInstruction> resultCodeList = new ArrayList<ResultInstruction>();
	// private Logger logger = LoggerFactory.getLogger(getClass());
	private CustomLogger logger = CustomLogger.getLogger(getClass());

	public ArrayList<ResultInstruction> getResultCodeList() {
		return resultCodeList;
	}

	int resultCodeIndex = 0;

	/**
	 * 用来标识解析开始
	 */
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		// System.out.println("SAX解析开始");
		logger.debug("SAX解析开始");
	}

	/**
	 * 用来标识解析结束
	 */
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
		// System.out.println("SAX解析结束");
		logger.debug("SAX解析结束");
	}

	/**
	 * 解析xml元素
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// 调用DefaultHandler类的startElement方法
		super.startElement(uri, localName, qName, attributes);
		if (qName.equals("resultCode")) {
			resultCodeIndex++;
			// 创建一个book对象
			resultCode = new ResultInstruction();
			// 开始解析book元素的属性
			// System.out.println("======================开始遍历某一结果码的内容=================");
			logger.debug("======================开始遍历某一结果码的内容=================");
			// 不知道book元素下属性的名称以及个数，如何获取属性名以及属性值
			// int num = attributes.getLength();
			// for (int i = 0; i < num; i++) {
			// System.out.print("book元素的第" + (i + 1) + "个属性名是：" +
			// attributes.getQName(i));
			// System.out.println("---属性值是：" + attributes.getValue(i));
			// if (attributes.getQName(i).equals("code")) {
			// resultCode.setCode(attributes.getValue(i));
			// } else if (attributes.getQName(i).equals("comments")) {
			// resultCode.setComments(attributes.getValue(i));
			// }
			// }
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// 调用DefaultHandler类的endElement方法
		super.endElement(uri, localName, qName);
		// 判断是否针对一本书已经遍历结束
		if (qName.equals("resultCode")) {
			resultCodeList.add(resultCode);
			resultCode = null;
			// System.out.println("======================结束遍历某一结果码的内容=================");
			logger.debug("======================结束遍历某一结果码的内容=================");
		} else if (qName.equals("code")) {
			resultCode.setCode(value);
		} else if (qName.equals("comments")) {
			resultCode.setComments(value);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		value = new String(ch, start, length);
		if (!value.trim().equals("")) {
			// System.out.println("节点值是：" + value);
			logger.debug("节点值是：" + value);
		}
	}
}

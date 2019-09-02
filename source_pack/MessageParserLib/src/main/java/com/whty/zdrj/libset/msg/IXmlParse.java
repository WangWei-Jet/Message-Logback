package com.whty.zdrj.libset.msg;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public interface IXmlParse {

	/**
	 * 解析xml文件
	 * 
	 * @param is
	 *            文件输入流
	 * @return 每一个node（域）封装成一个类，输出该类的list
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public <T> List<T> parseXML(InputStream is) throws ParserConfigurationException, SAXException, IOException;

}

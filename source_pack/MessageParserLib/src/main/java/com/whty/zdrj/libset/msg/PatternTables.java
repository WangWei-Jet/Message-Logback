package com.whty.zdrj.libset.msg;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.whty.zdrj.libset.msg.utils.CustomLogger;

/**
 * 协议加载类
 * 
 * @author OneWay
 * 
 */
public class PatternTables {
	private static final Map<String, List<?>> PATTERN_TABLES = new HashMap<String, List<?>>();

	private static CustomLogger logger = CustomLogger.getLogger(PatternTables.class.getSimpleName());

	public static Map<String, List<?>> getPatternTables() {
		return PATTERN_TABLES;
	}

	public static List<?> getPattern(String key) {
		return PATTERN_TABLES.get(key);
	}

	/**
	 * 加载协议模板到指定的key下面
	 * 
	 * @param key
	 *            为模板设定的对应的key
	 * @param is
	 *            xml文件流
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T> void loadPattern(String key, InputStream is) throws ParserConfigurationException, SAXException,
			IOException, InstantiationException, IllegalAccessException {
		XmlParser parser = new XmlParser();
		// System.out.println(targetClass.getSimpleName());
		// logger.debug(targetClass.getSimpleName());
		List<T> list = parser.parseXML(is);
		// System.out.println("KEY:" + key);
		logger.debug("key:" + key);
		PatternTables.getPatternTables().put(key, list);
	}
}

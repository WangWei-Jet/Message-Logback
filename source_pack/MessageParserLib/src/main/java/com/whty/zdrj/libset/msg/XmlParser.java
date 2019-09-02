package com.whty.zdrj.libset.msg;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.whty.zdrj.libset.msg.utils.CustomLogger;

public class XmlParser implements IXmlParse {

	private CustomLogger logger = CustomLogger.getLogger(this.getClass());

	public synchronized <T> List<T> parseXML(InputStream is)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(is);
		Element rootEl = doc.getDocumentElement();
		List<T> list = new ArrayList<T>();

		String type = rootEl.getAttribute("type");
		logger.debug("message model：" + type);
		processParse(rootEl, list);

		return list;
	}

	private <T> void processParse(Element rootEl, List<T> list) {
		NodeList items = rootEl.getChildNodes();
		for (int i = 0; i < items.getLength(); i++) {
			Node item = items.item(i);
			if (item.getNodeName().equals("#text"))
				continue;
			T pe = parsePeElement(item);
			if (pe != null) {
				list.add(pe);
			}
		}

	}

	/**
	 * 递归查找元素
	 * 
	 * @param item
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T parsePeElement(Node item) {
		Element elt = (Element) item;
		NodeList props = elt.getChildNodes();
		ParseElement pe = new ParseElement();
		try {
			// logger.debug("T类型:" + ((T) pe).getClass().getSimpleName());
			// ((T) pe).getClass().getSimpleName();
		} catch (Exception e) {
			logger.debug("类型匹配失败，不进行解析");
			return null;
		}
		// logger.debug("进来了:" + pe.getClass().getSimpleName());
		for (int j = 0; j < props.getLength(); j++) {
			Node property = props.item(j);
			String nodeName = property.getNodeName();
			if (nodeName.equals("#text"))
				continue;

			if (nodeName.equals("id")) {
				pe.setId(property.getFirstChild().getNodeValue());
			}

			if (nodeName.equals("len")) {
				pe.setLen(property.getFirstChild().getNodeValue());
			}

			if (nodeName.equals("tag")) {
				pe.setTag(property.getFirstChild().getNodeValue());
			}

			if (nodeName.equals("lllvar")) {
				pe.setLllvar(property.getFirstChild().getNodeValue());
			}

			if (nodeName.equals("code")) {
				pe.setCode(property.getFirstChild().getNodeValue());
			}

			if (nodeName.equals("comments")) {
				pe.setComments(property.getFirstChild().getNodeValue());
			}

			if (nodeName.equals("node")) {
				if (parsePeElement(property) != null) {
					pe.getChildren().add((ParseElement) parsePeElement(property));
				}
			}

			String targetClass = elt.getAttribute("class");

			if (targetClass != null && !targetClass.equals(""))
				pe.setTargetClass(targetClass);

			String parseAttr = elt.getAttribute("parseMethod");

			if (parseAttr != null && !parseAttr.equals(""))
				pe.setParseMethod(parseAttr);

			String buildAttr = elt.getAttribute("buildMethod");

			if (buildAttr != null && !buildAttr.equals(""))
				pe.setBuildMethod(buildAttr);

			String parseMethodParam = elt.getAttribute("parseMethodParam");

			if (parseMethodParam != null && !parseMethodParam.equals(""))
				pe.setParseMethodParam(parseMethodParam);

			String buildMethodParam = elt.getAttribute("buildMethodParam");

			if (buildMethodParam != null && !buildMethodParam.equals(""))
				pe.setBuildMethodParam(buildMethodParam);

		}
		try {
			return (T) pe;
		} catch (Exception e) {
			logger.error("type error");
		}
		return null;
	}

}

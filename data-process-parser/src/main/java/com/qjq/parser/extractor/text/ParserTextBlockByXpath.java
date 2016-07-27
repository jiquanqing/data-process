package com.qjq.parser.extractor.text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.qjq.parser.extractor.utils.CommonUtil;

public class ParserTextBlockByXpath extends BasicParserTextBlock {

	private String html;

	public Map<String, String> process(String html, Map<String, String> xpath) {
		Map<String, String> result = new HashMap<String, String>();
		this.html = html;
		Document document = getDocument(html);
		result = extratorContent(xpath, document);
		return result;
	}
	public Map<String, String> extratorContent(Map<String, String> xpath, Document document) {
		Map<String, String> res = new HashMap<String, String>();
		Set<String> set = xpath.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = xpath.get(key);
			if (value.contains("@")) {
				String exs[] = value.split("@");
				String xpathValue = extractorByXpath(exs[0], document);
				if (xpathValue != null && !xpathValue.equals("")) {
					res.put(key, xpathValue);
				} else {
					xpathValue = extractorByHtmlInfo(exs[1]);
					res.put(key, xpathValue);
				}
			} else {
				String xpathValue = extractorByXpath(value, document);
				res.put(key, xpathValue);
			}
		}
		return res;
	}
	public String extractorByHtmlInfo(String htmlInfo) {
		String result = "";
		Source source = new Source(html);
		List<Element> elements = source.getAllElements();
		for (int i = 0; i < elements.size(); i++) {
			Element element = elements.get(i);
			if (successByAttribute(element, htmlInfo)) {
				result = element.getContent().toString();
				break;
			}
		}
		return result;
	}
	protected boolean successByAttribute(Element element, String attribute) {
		String tableTag = element.getStartTag().toString();
		tableTag = CommonUtil.removeHuanHang(tableTag);
		attribute = CommonUtil.removeHuanHang(attribute);
		if (tableTag.equalsIgnoreCase(attribute))
			return true;
		return false;
	}
	public String extractorByXpath(String xpathStr, Document document) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		if (xpathStr == null || xpathStr.equals(""))
			return "";
		StringBuffer value = new StringBuffer();
		try {
			NodeList lists = (NodeList) xpath.evaluate(xpathStr, document, XPathConstants.NODESET);
			for (int j = 0; j < lists.getLength(); j++) {
				Node node = lists.item(j);
				value.append(CommonUtil.removeHeadBlank(node.getTextContent()));
			}
		} catch (XPathExpressionException e) {
		}
		return value.toString();
	}
}

package com.qjq.parser.extractor.wiki;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.qjq.parser.extractor.ExtractorPage;
import com.qjq.parser.extractor.builder.DefaultPageBuilder;
import com.qjq.parser.extractor.configuration.ApplicationConfig;
import com.qjq.parser.extractor.domain.PageDesc;
import com.qjq.parser.extractor.exception.WrappedException;
import com.qjq.parser.extractor.html.HtmlPageParser;
import com.qjq.parser.extractor.utils.CommonUtil;

public class WiKiPageBuilder extends DefaultPageBuilder {
	private static Logger LOG = LoggerFactory.getLogger(WiKiPageBuilder.class);

	final static String CONIF_NAME = "wiki";
	public ExtractorPage extract(PageDesc desc, String rawContent) {
		WiKiExtractorPage page = new WiKiExtractorPage();
		page.setUrl(desc.getUrl());
		HtmlPageParser htmlPageParser = HtmlPageParser.getHtmlPageParser();
		// rawContent = delAllTable(rawContent);
		Document document = htmlPageParser.parser(desc.getUrl(), rawContent);
		page.setXmlDoc(document);
		processDoc(page, document);
		return page;
	}
	/*
	 * public String delAllTable(String htmlStr) { htmlStr =
	 * htmlStr.toLowerCase(); System.out.println(htmlStr.length()); Pattern
	 * pattern = Pattern.compile("<table[^>]*?>.*?</table>"); Matcher matcher =
	 * pattern.matcher(htmlStr); while (matcher.find()) {
	 * System.out.println(matcher.group()); }
	 * 
	 * String newhtmlStr = htmlStr.replaceAll("<table[^>]*?>.*?</table>", "");
	 * System.out.println(newhtmlStr.length()); return newhtmlStr; }
	 */
	private void processDoc(WiKiExtractorPage page, Document document) {
		String url = page.getUrl();
		WiKiConfiguration configuration = (WiKiConfiguration) ApplicationConfig.getConfigFromApplication().getConf(CONIF_NAME);
		List<WiKiTemplate> templates = configuration.getZhWiKiTemplateByUrl(url);
		if (templates == null || templates.size() <= 0) {
			LOG.info("no templates find by url : " + url);
			return;
		}
		WiKiTemplate template = extractorTitle(page, document, templates);
		if (template == null)
			return;
		extractorContent(page, document, template);
		extractorTableContent(page, document, template);
		delTabel(page);
	}
	private void delTabel(WiKiExtractorPage page) {
		String content = page.getContent();
		List<String> tables = page.getTables();
		for (String string : tables) {
			if (content.contains(string)) {
				// System.out.println("table is = " + string);
				content = content.replace(string, "");
				// content = content.replaceAll(string, "");
			}
		}
		page.setContent(content);
	}
	private WiKiTemplate extractorTitle(WiKiExtractorPage page, Document document, List<WiKiTemplate> templates) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		for (int i = 0; i < templates.size(); i++) {
			WiKiTemplate template = templates.get(i);
			String xpathTitle = template.getTitle();
			if (xpathTitle == null || xpathTitle.equals(""))
				return null;
			String value = "";
			try {
				value = xpath.evaluate(xpathTitle, document);
				value = CommonUtil.removeHuanHang(value);
			} catch (XPathExpressionException e) {
				LOG.debug("xpath error and xpath = " + xpath);
			}
			if (value != null && !value.toString().equals("")) {
				page.setTitle(value);
				return template;
			}
		}
		return null;
	}

	private void extractorContent(WiKiExtractorPage page, Document document, WiKiTemplate template) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		String xpathContent = template.getContent();
		if (xpathContent == null || xpathContent.equals(""))
			return;
		try {
			String vaule = xPath.evaluate(xpathContent, document);
			vaule = CommonUtil.removeHuanHang(vaule);
			page.setContent(vaule);
			// page.setAbstractInfo(vaule);
			// page.setKeywordTag(CommonUtil.removeHuanHang(vaule));
			// page.setPublishSource(vaule);
		} catch (XPathExpressionException e) {
			throw new WrappedException(e);
		}
	}
	private void extractorTableContent(WiKiExtractorPage page, Document document, WiKiTemplate template) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		String xpathTable = template.getTable();
		if (xpathTable == null || xpathTable.equals(""))
			return;
		try {
			List<String> list = new ArrayList<String>();
			NodeList lists = (NodeList) xPath.evaluate(xpathTable, document, XPathConstants.NODESET);
			for (int j = 0; j < lists.getLength(); j++) {
				Node node = lists.item(j);
				String str = node.getTextContent();
				list.add(CommonUtil.removeHuanHang(str));
			}
			page.setTables(list);
		} catch (Exception e) {
			LOG.info("extractor table content is failed");
		}
	}

}

package com.qjq.parser.extractor.cnki;

import java.util.List;

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

public class CnKiPageBuilder extends DefaultPageBuilder {
	private static Logger LOG = LoggerFactory.getLogger(CnKiPageBuilder.class);

	final static String CONIF_NAME = "cnki";
	public ExtractorPage extract(PageDesc desc, String rawContent) {
		CnKiExtractorPage page = new CnKiExtractorPage();
		page.setUrl(desc.getUrl());
		HtmlPageParser htmlPageParser = HtmlPageParser.getHtmlPageParser();
		Document document = htmlPageParser.parser(desc.getUrl(), rawContent);
		page.setXmlDoc(document);
		processDoc(page, document);
		return page;
	}
	private void processDoc(CnKiExtractorPage page, Document document) {
		String url = page.getUrl();
		CnKiConfiguration configuration = (CnKiConfiguration) ApplicationConfig
		        .getConfigFromApplication().getConf(CONIF_NAME);
		List<CnKiTemplate> templates = configuration.getZhWiKiTemplateByUrl(url);
		if (templates == null || templates.size() <= 0) {
			LOG.info("no templates find by url : " + url);
			return;
		}
		CnKiTemplate template = extractorClcNum(page, document, templates);
		if (template == null)
			return;
		extractorKeyWords(page, document, template);
		extractorAbstract(page, document, template);
		extractorContent(page, document, template);
	}
	private CnKiTemplate extractorClcNum(CnKiExtractorPage page, Document document,
	        List<CnKiTemplate> templates) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		for (int i = 0; i < templates.size(); i++) {
			CnKiTemplate template = templates.get(i);
			String xpathClcNum = template.getClcNum();
			if (xpathClcNum == null || xpathClcNum.equals(""))
				return null;
			StringBuffer value = new StringBuffer();
			try {
				String str = "";
				NodeList lists = (NodeList) xpath.evaluate(xpathClcNum, document,
				        XPathConstants.NODESET);
				for (int j = 0; j < lists.getLength(); j++) {
					Node node = lists.item(j);
					str = node.getTextContent();
					if (str.contains("分类号"))
						break;
				}

				value.append(CommonUtil.removeHuanHang(str));
			} catch (XPathExpressionException e) {
				LOG.debug("xpath error and xpath = " + xpath);
			}
			if (value != null && !value.toString().equals("")) {
				String s = value.toString().replaceAll("【分类号】", "");
				if (s.contains("【")) {
					s = s.substring(0, s.indexOf("【"));
				}
				page.setClcNum(s);
				return template;
			}
		}
		return null;
	}
	private void extractorKeyWords(CnKiExtractorPage page, Document document, CnKiTemplate template) {
		page.setKeywordTag(getXpathStringContent(document, template.getKeyWords()));
		page.setKeyworden(getXpathStringContent(document, template.getKeyworden()));
		page.setKeywordenhref(getXpathStringList(document, template.getKeywordenhref()));
		page.setKeywordzh(getXpathStringContent(document, template.getKeywordzh()));
		page.setKeywordzhhref(getXpathStringList(document, template.getKeywordzhhref()));
	}
	private void extractorContent(CnKiExtractorPage page, Document document, CnKiTemplate template) {
		page.setContent(getXpathStringContent(document, template.getContent()));
		page.setJigou(getXpathStringContent(document, template.getJigou()));
		page.setJigouhref(getXpathStringList(document, template.getJigouhref()));
		page.setPublishSource(getXpathStringContent(document, template.getPublishinfo()));
		page.setAuthor(getXpathStringContent(document, template.getAuthor()));
		page.setAuthoren(getXpathStringContent(document, template.getAuthoren()));
		page.setAuthorhref(getXpathStringList(document, template.getAuthorhref()));
		page.setAuthorenhref(getXpathStringList(document, template.getAuthorenhref()));
		page.setChTitle(getXpathStringContent(document, template.getChTitle()));
		page.setEnTitle(getXpathStringContent(document, template.getEnTitle()));
	}
	private void extractorAbstract(CnKiExtractorPage page, Document document, CnKiTemplate template) {
		page.setAbstrachen(getXpathStringContent(document, template.getAbstrachen()));
		page.setAbstrachzh(getXpathStringContent(document, template.getAbstrachzh()));
		page.setAbstractInfo(getXpathStringContent(document, template.getAbstractinfo()));
	}
	private String getXpathStringList(Document document, String xpathStr) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		StringBuilder builder = new StringBuilder();
		if (xpathStr == null || xpathStr.equals(""))
			return "";
		try {
			NodeList list = (NodeList) xPath.evaluate(xpathStr, document, XPathConstants.NODESET);
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (!builder.toString().equals(""))
					builder.append("@");
				builder.append(CommonUtil.removeHuanHang(node.getTextContent()));
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.toString();
	}
	private String getXpathStringContent(Document document, String xpathStr) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		String value = "";
		if (xpathStr == null || xpathStr.equals(""))
			return "";
		try {
			value = xPath.evaluate(xpathStr, document);
		} catch (XPathExpressionException e) {
			throw new WrappedException(e);
		}
		return CommonUtil.removeHeadBlank(value);
	}
}

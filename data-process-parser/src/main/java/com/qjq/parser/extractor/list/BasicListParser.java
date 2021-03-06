package com.qjq.parser.extractor.list;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import net.sf.json.JSONArray;

import org.jaxen.Navigator;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.qjq.parser.extractor.html.HtmlPageParser;
import com.qjq.parser.extractor.table.Cell;
import com.qjq.parser.extractor.table.Table;
import com.qjq.parser.extractor.utils.CommonUtil;

public class BasicListParser implements ListParser {

	protected List<Table> tables;
	protected String html;
	protected JSONArray jsonTables;
	protected String needHtmlContent;

	public List<Table> process(String html, String attribute, String xpath, String tag) {
		return null;
	}
	protected Table parserHtml(String tag) {
		Table table = new Table();
		Source source = new Source(needHtmlContent);
		List<Element> elements = source.getAllElements(tag);
		table.setRows(elements.size());
		table.setCols(1);
		Cell cells[][] = new Cell[table.getRows()][table.getCols()];
		for (int i = 0; i < elements.size(); i++) {
			cells[i][0] = new Cell();
			cells[i][0].setRows(i);
			cells[i][0].setCols(0);
			cells[i][0].setMark("");
			cells[i][0].setText(elements.get(i).getTextExtractor().toString());
		}
		table.setSuccess(true);
		table.setCells(cells);
		return table;
	}
	protected String getXpathContent(String xpathStr) {
		StringBuilder builder = new StringBuilder();
		Document document = HtmlPageParser.getHtmlPageParser().parser("", html);
		try {
			XPath expression = new DOMXPath(xpathStr);
			if (xpathStr == null || xpathStr.equals(""))
				return "";
			List results = expression.selectNodes(document);
			Iterator iterator = results.iterator();
			Navigator navigator = expression.getNavigator();
			while (iterator.hasNext()) {
				Node result = (Node) iterator.next();
				builder.append(getHtmlByNode(result));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	protected String getHtmlByNode(Node node) {
		String xmlStr = "";
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty("encoding", "UTF-8");// 解决中文问题，试过用GBK不行
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			t.transform(new DOMSource(node), new StreamResult(bos));
			xmlStr = bos.toString();
		} catch (Exception e) {
		}
		if (!xmlStr.equals(""))
			xmlStr = xmlStr.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
		return xmlStr;
	}
	protected boolean successByAttribute(Element element, String attribute) {
		String tableTag = element.getStartTag().toString();
		tableTag = CommonUtil.removeHuanHang(tableTag);
		attribute = CommonUtil.removeHuanHang(attribute);
		if (tableTag.equalsIgnoreCase(attribute))
			return true;
		return false;
	}
	public JSONArray getJsonTables() {
		if (tables == null)
			return null;
		jsonTables = JSONArray.fromObject(tables);
		return jsonTables;
	}
}

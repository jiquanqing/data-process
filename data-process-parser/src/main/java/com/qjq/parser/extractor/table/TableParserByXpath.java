package com.qjq.parser.extractor.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

public class TableParserByXpath extends BasicTableParser {

	public List<Table> process(String html, String attribute, Map<String, String> reg, String xpath) {
		this.html = html;
		this.xpathContent = getXpathContent(xpath);
		table = new ArrayList<Table>();
		//processHiddenCode();
		Source source = new Source(xpathContent);
		List<Element> elementList = source.getAllElements(HTMLElementName.TABLE);
		for (Element element : elementList) {
			this.processHtmlMergeTable(element);
		}
		source = new Source(xpathContent);
		elementList = source.getAllElements(HTMLElementName.TABLE);
		for (Element element : elementList) {
			table.add(getTableContent(element));

		}

		return table;
	}

}

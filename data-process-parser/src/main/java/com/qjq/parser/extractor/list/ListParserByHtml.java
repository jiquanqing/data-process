package com.qjq.parser.extractor.list;

import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import com.qjq.parser.extractor.table.Table;

public class ListParserByHtml extends BasicListParser {

	public List<Table> process(String html, String attribute, String xpath, String tag) {
		tables = new ArrayList<Table>();
		this.html = html;
		Source source = new Source(html);
		List<Element> elementList = source.getAllElements();
		for (Element element : elementList) {
			if (successByAttribute(element, attribute)) {
				needHtmlContent = element.toString();
				tables.add(parserHtml(tag));
			}
		}
		return tables;
	}
}

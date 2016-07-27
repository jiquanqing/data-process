package com.qjq.parser.extractor.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.sf.json.JSONArray;

public class TableParserByHtmlParser extends BasicTableParser {
	
	public List<Table> process(String html, String attribute, Map<String, String> reg, String xpath) {
		table = new ArrayList<Table>();
		this.html = html;
		Source source = new Source(html);
		List<Element> elementList = source.getAllElements(HTMLElementName.TABLE);
		for (Element element : elementList) {
			if (isNeedTable(element, attribute, reg, xpath)) {
				table.add(getTableContent(element));
			}
		}
		return table;
	}
	private boolean isNeedTable(Element element, String attribute, Map<String, String> reg, String xpath) {
		if (successByAttribute(element, attribute)) { // 通过属性的方式进行抽取
			return true;
		}
		return false;
	}
	
	public List<Table> getTable() {
		return table;
	}

	public void setTable(List<Table> table) {
		this.table = table;
	}

	public void setJsonTables(JSONArray jsonTables) {
		this.jsonTables = jsonTables;
	}
}

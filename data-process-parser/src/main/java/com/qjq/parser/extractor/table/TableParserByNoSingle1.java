package com.qjq.parser.extractor.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.qjq.parser.extractor.utils.CommonUtil;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class TableParserByNoSingle1 extends BasicTableParser {

	private String needHtmlContent;

	public List<Table> process(String html, String attraibute, String xpath, Map<String, String> rowtag, Map<String, String> coltag) {
		needHtmlContent = "";
		table = new ArrayList<Table>();
		if (xpath != null && !xpath.equals("")) {
			needHtmlContent = getXpathContent(xpath);
		} else {
			needHtmlContent = getHtmlCodeByAttraibute(html, attraibute);
		}
		Table t = parserHtmlCode(needHtmlContent, rowtag, coltag);
		table.add(t);
		return table;
	}
	private Table parserHtmlCode(String htmlCode, Map<String, String> rowtag, Map<String, String> coltag) {

		Table table = new Table();
		Cell[][] cells = null;
		int rows = getTableRows(htmlCode, rowtag);
		int cols = getTableCols(htmlCode, coltag);
		cols = cols / rows;
		table.setRows(rows);
		table.setCols(cols);
		cells = new Cell[rows][cols];

		Source source = new Source(htmlCode);
		List<Element> elements = source.getAllElements();
		int i = 0;
		for (Element element : elements) {
			if (isNeedTag(element.getStartTag().toString(), rowtag)) { // 找到需要的行
				String trStr = element.toString();
				Source source2 = new Source(trStr);
				List<Element> tdElements = source2.getAllElements();
				int j = 0;
				for (Element element2 : tdElements) {
					if (isNeedTag(element2.getStartTag().toString(), coltag)) { // 找到需要的列
						cells[i][j] = new Cell();
						cells[i][j].setMark("");
						cells[i][j].setRows(i);
						cells[i][j].setCols(j);
						cells[i][j].setText(element2.getTextExtractor().toString());
						j++;
					}
				}
				i++;
			}
		}
		table.setCells(cells);
		return table;
	}
	private String getHtmlCodeByAttraibute(String html, String attr) {
		StringBuilder builder = new StringBuilder();
		Source source = new Source(html);
		List<Element> elements = source.getAllElements();
		for (Element element : elements) {
			if (successByAttribute(element, attr)) {
				builder.append(element.toString());
				break;
			}
		}
		return builder.toString();

	}
	private int getTableRows(String html, Map<String, String> rowtag) {
		Source source = new Source(html);
		List<Element> elements = source.getAllElements();
		int row = 0;
		for (Element element : elements) {
			if (isNeedTag(element.getStartTag().toString(), rowtag)) {
				row++;
			}
		}
		return row;
	}
	private boolean isNeedTag(String str, Map<String, String> map) {
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();

		while (it.hasNext()) {
			String key = it.next();
			String value = map.get(key);
			if (key.startsWith("equals")) {
				value = CommonUtil.removeHuanHang(value);
				str = CommonUtil.removeHuanHang(str);
				if (value.equals(str)) {
					return true;
				}
			} else if (key.startsWith("reg")) {
				if (CommonUtil.matchStr(value, str)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private int getTableCols(String html, Map<String, String> coltag) {
		Source source = new Source(html);
		List<Element> elements = source.getAllElements();
		int col = 0;
		for (Element element : elements) {
			if (isNeedTag(element.getStartTag().toString(), coltag)) {
				col++;
			}
		}
		return col;
	}
	
}

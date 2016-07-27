package com.qjq.parser.extractor.list;

import java.util.ArrayList;
import java.util.List;

import com.qjq.parser.extractor.table.Table;

public class ListParserByXpath extends BasicListParser{
	
	public List<Table> process(String html, String attribute, String xpath,String tag){
		tables = new ArrayList<Table>();
		this.html = html;
		needHtmlContent = getXpathContent(xpath);
		
		Table table = parserHtml(tag);
		tables.add(table);
		
		return tables;
	}
}

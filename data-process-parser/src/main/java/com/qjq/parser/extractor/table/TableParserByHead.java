package com.qjq.parser.extractor.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.Source;

public class TableParserByHead extends BasicTableParser {

	public List<Table> process(String html, String attribute, Map<String, String> reg, String xpath) {
		table = new ArrayList<Table>();
		Source source = new Source(html);
		
		return table;
	}

}

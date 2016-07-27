package com.qjq.parser.extractor.list;

import java.util.List;

import com.qjq.parser.extractor.table.Table;

public interface ListParser {
	public List<Table> process(String html, String attribute, String xpath,String tag);
}

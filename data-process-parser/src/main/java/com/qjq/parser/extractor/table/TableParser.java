package com.qjq.parser.extractor.table;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

public interface TableParser {

	public List<Table> process(String html, String attribute, Map<String, String> reg, String xpath);
	public List<Table> process(String html, String attraibute, String xpath, Map<String, String> rowtag, Map<String, String> colreg);
	
	public List<Table> process(String html, String attraibute, String xpath, String rowtag, String colreg);

	public JSONArray getJsonTables();
}

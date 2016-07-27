package com.qjq.parser.extractor.text;

import java.util.Map;

public interface ParserTextBlock {

	public Map<String, String> process(String html, Map<String, String> xpath);
	

}

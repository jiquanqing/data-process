package com.qjq.parser.extractor.text;

import java.util.Map;

import org.w3c.dom.Document;

import com.qjq.parser.extractor.html.HtmlPageParser;

public class BasicParserTextBlock implements ParserTextBlock {

	public Map<String, String> process(String html, Map<String, String> xpath) {
		return null;
	}

	public Document getDocument(String html) {
		HtmlPageParser htmlPageParser = HtmlPageParser.getHtmlPageParser();
		Document document = htmlPageParser.parser("", html);
		return document;
	}
}

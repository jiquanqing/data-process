package com.qjq.parser.extractor.autotemplate;

import java.util.ArrayList;
import java.util.List;

public class AutoPattern {
	private String name;
	private List<String> xpath = new ArrayList<String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getXpath() {
		return xpath;
	}

	public void setXpath(List<String> xpath) {
		this.xpath = xpath;
	}
}

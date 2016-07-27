package com.qjq.parser.extractor.wiki;

import java.util.List;

import com.qjq.parser.extractor.ExtractorPage;

public class WiKiExtractorPage extends ExtractorPage {

	private String clcNum;
	private List<String> tables;

	public String getClcNum() {
		return clcNum;
	}

	public void setClcNum(String clcNum) {
		this.clcNum = clcNum;
	}

	public List<String> getTables() {
	    return tables;
    }

	public void setTables(List<String> tables) {
	    this.tables = tables;
    }
}

package com.qjq.parser.extractor.wiki;

import com.qjq.parser.extractor.configuration.DefaultTemplate;

/*
 * functions:ciki configuration template
 * author:qingjiquan
 * date:2014-11-20
 */
public class WiKiTemplate extends DefaultTemplate {

	private String content;
	private String table;
	
	public String getTable() {
    	return table;
    }
	public void setTable(String table) {
    	this.table = table;
    }
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}

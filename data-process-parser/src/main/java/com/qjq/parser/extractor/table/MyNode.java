package com.qjq.parser.extractor.table;

public class MyNode {
	
	private String name;
	private boolean hasText;
	private String childs;
	private int loc;
	private String content;
	private boolean isTr;
	
	public String getName() {
    	return name;
    }
	public void setName(String name) {
    	this.name = name;
    }
	public boolean isHasText() {
    	return hasText;
    }
	public void setHasText(boolean hasText) {
    	this.hasText = hasText;
    }
	public String getChilds() {
    	return childs;
    }
	public void setChilds(String childs) {
    	this.childs = childs;
    }
	public int getLoc() {
	    return loc;
    }
	public void setLoc(int loc) {
	    this.loc = loc;
    }
	public boolean isTr() {
	    return isTr;
    }
	public void setTr(boolean isTr) {
	    this.isTr = isTr;
    }
	@Override
    public String toString() {
	    return "MyNode [name=" + name + ", hasText=" + hasText + ", childs=" + childs + ", loc="
	            + loc + ", isTr=" + isTr + "]";
    }
	public String getContent() {
	    return content;
    }
	public void setContent(String content) {
	    this.content = content;
    }
	
	
}

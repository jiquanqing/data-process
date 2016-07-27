package com.qjq.parser.extractor.cnki;

import com.qjq.parser.extractor.configuration.DefaultTemplate;

/*
 * functions:ciki configuration template
 * author:qingjiquan
 * date:2014-11-20
 */
public class CnKiTemplate extends DefaultTemplate {


	private String keyWords;
	private String clcNum;
	private String abstractinfo;
	private String content;
	private String author;
	private String authorhref;
	private String authoren;
	private String authorenhref;
	private String jigou;
	private String jigouhref;
	private String abstrachzh;
	private String abstrachen;
	private String keywordzh;
	private String keyworden;
	private String keywordzhhref;
	private String keywordenhref;
	private String chTitle;
	private String enTitle;
	
	
	

	public String getChTitle() {
    	return chTitle;
    }
	public void setChTitle(String chTitle) {
    	this.chTitle = chTitle;
    }
	public String getEnTitle() {
    	return enTitle;
    }
	public void setEnTitle(String enTitle) {
    	this.enTitle = enTitle;
    }
	public String getKeywordzh() {
    	return keywordzh;
    }
	public void setKeywordzh(String keywordzh) {
    	this.keywordzh = keywordzh;
    }
	public String getKeyworden() {
    	return keyworden;
    }
	public void setKeyworden(String keyworden) {
    	this.keyworden = keyworden;
    }
	public String getKeywordzhhref() {
    	return keywordzhhref;
    }
	public void setKeywordzhhref(String keywordzhhref) {
    	this.keywordzhhref = keywordzhhref;
    }
	public String getKeywordenhref() {
    	return keywordenhref;
    }
	public void setKeywordenhref(String keywordenhref) {
    	this.keywordenhref = keywordenhref;
    }
	private String publishinfo;
	
	
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getAbstractinfo() {
	    return abstractinfo;
    }
	public void setAbstractinfo(String abstractinfo) {
	    this.abstractinfo = abstractinfo;
    }
	public String getContent() {
	    return content;
    }
	public void setContent(String content) {
	    this.content = content;
    }
	public String getAuthor() {
    	return author;
    }
	public void setAuthor(String author) {
    	this.author = author;
    }
	public String getAuthorhref() {
    	return authorhref;
    }
	public void setAuthorhref(String authorhref) {
    	this.authorhref = authorhref;
    }
	public String getAuthoren() {
    	return authoren;
    }
	public void setAuthoren(String authoren) {
    	this.authoren = authoren;
    }
	public String getAuthorenhref() {
    	return authorenhref;
    }
	public void setAuthorenhref(String authorenhref) {
    	this.authorenhref = authorenhref;
    }
	public String getJigou() {
    	return jigou;
    }
	public void setJigou(String jigou) {
    	this.jigou = jigou;
    }
	public String getJigouhref() {
    	return jigouhref;
    }
	public void setJigouhref(String jigouhref) {
    	this.jigouhref = jigouhref;
    }
	public String getAbstrachzh() {
    	return abstrachzh;
    }
	public void setAbstrachzh(String abstrachzh) {
    	this.abstrachzh = abstrachzh;
    }
	public String getAbstrachen() {
    	return abstrachen;
    }
	public void setAbstrachen(String abstrachen) {
    	this.abstrachen = abstrachen;
    }
	public String getPublishinfo() {
    	return publishinfo;
    }
	public void setPublishinfo(String publishinfo) {
    	this.publishinfo = publishinfo;
    }
	public String getClcNum() {
	    return clcNum;
    }
	public void setClcNum(String clcNum) {
	    this.clcNum = clcNum;
    }
	

}

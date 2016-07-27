package com.qjq.parser.extractor.baike;

import java.util.List;

import com.qjq.parser.extractor.ExtractorPage;

/*
 * functions:extractor baike wiki pages
 * author:qingjiquan
 * date:2014-11-06
 */
public class BaikeExtractorPage extends ExtractorPage {

    private List<String> synonymEntries;
    private List<String> polysemyEntries;
    private List<String> relatedEntries;
    private List<String> parentEntries;
    private List<String> subEntries;
    private List<String> synonymUrls;
    private List<String> polysemyUrls;
    private List<String> relatedUrls;
    private List<String> parentUrls;
    private List<String> subUrls;
    private String allContent;
    private String summary;
    private String h2;
    private String h3;
    private String abstractStr;    

	public String getAllContent() {
		return allContent;
	}

	public void setAllContent(String allContent) {
		this.allContent = allContent;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getH2() {
		return h2;
	}

	public void setH2(String h2) {
		this.h2 = h2;
	}

	public String getH3() {
		return h3;
	}

	public void setH3(String h3) {
		this.h3 = h3;
	}

	public List<String> getSynonymEntries() {
        return synonymEntries;
    }

    public void setSynonymEntries(List<String> synonymEntries) {
        this.synonymEntries = synonymEntries;
    }

    public List<String> getPolysemyEntries() {
        return polysemyEntries;
    }

    public void setPolysemyEntries(List<String> polysemyEntries) {
        this.polysemyEntries = polysemyEntries;
    }

    public List<String> getRelatedEntries() {
        return relatedEntries;
    }

    public void setRelatedEntries(List<String> relatedEntries) {
        this.relatedEntries = relatedEntries;
    }

    public List<String> getParentEntries() {
        return parentEntries;
    }

    public void setParentEntries(List<String> parentEntries) {
        this.parentEntries = parentEntries;
    }

    public List<String> getSubEntries() {
        return subEntries;
    }

    public void setSubEntries(List<String> subEntries) {
        this.subEntries = subEntries;
    }

    public List<String> getSynonymUrls() {
        return synonymUrls;
    }

    public void setSynonymUrls(List<String> synonymUrls) {
        this.synonymUrls = synonymUrls;
    }

    public List<String> getPolysemyUrls() {
        return polysemyUrls;
    }

    public void setPolysemyUrls(List<String> polysemyUrls) {
        this.polysemyUrls = polysemyUrls;
    }

    public List<String> getRelatedUrls() {
        return relatedUrls;
    }

    public void setRelatedUrls(List<String> relatedUrls) {
        this.relatedUrls = relatedUrls;
    }

    public List<String> getParentUrls() {
        return parentUrls;
    }

    public void setParentUrls(List<String> parentUrls) {
        this.parentUrls = parentUrls;
    }

    public List<String> getSubUrls() {
        return subUrls;
    }

    public void setSubUrls(List<String> subUrls) {
        this.subUrls = subUrls;
    }

    public String getAbstractStr() {
        return abstractStr;
    }

    public void setAbstractStr(String abstractStr) {
        this.abstractStr = abstractStr;
    }

}

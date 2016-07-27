package com.qjq.parser.extractor;

/*
 * function: provide interface 
 * author: qingjiquan
 * date: 2014-10-24
 */
import java.io.Reader;
import org.w3c.dom.Document;

public class ExtractorPage {

    protected String url;
    protected String uid;
    protected String content;
    protected String title;
    protected Document xmlDoc;
    private String abstractInfo;
    protected boolean isIngorXmlDoc = true;
    
    private Reader contentReader;
    private String publishDate;
    private String publishSource = "";
    private String keywordTag = "";
    private String authorId = "";

    public ExtractorPage() {

    }

    public void extract() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Reader getContentReader() {
        return contentReader;
    }

    public void setContentReader(Reader contentReader) {
        this.contentReader = contentReader;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Document getXmlDoc() {
        return xmlDoc;
    }

    public void setXmlDoc(Document xmlDoc) {
        if (!this.isIngorXmlDoc)
            this.xmlDoc = xmlDoc;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishSource() {
        return publishSource;
    }

    public void setPublishSource(String publishSource) {
        this.publishSource = publishSource;
    }

    public String getKeywordTag() {
        return keywordTag;
    }

    public void setKeywordTag(String keywordTag) {
        this.keywordTag = keywordTag;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public boolean isIngorXmlDoc() {
        return isIngorXmlDoc;
    }

    public void setIngorXmlDoc(boolean isIngorXmlDoc) {
        this.isIngorXmlDoc = isIngorXmlDoc;
    }

	public String getAbstractInfo() {
	    return abstractInfo;
    }

	public void setAbstractInfo(String abstractInfo) {
	    this.abstractInfo = abstractInfo;
    }

}

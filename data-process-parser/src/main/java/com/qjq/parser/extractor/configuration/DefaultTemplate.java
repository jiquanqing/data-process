package com.qjq.parser.extractor.configuration;

import java.util.ArrayList;
import java.util.List;

/*
 * functions: defaule xml xpath extractor template
 * author:qingjiquan
 * date:2014-10-30
 */
public class DefaultTemplate {
    public List<String> urlPatterns = new ArrayList<String>();
    public String title;
    public String body;
    public String author;
    public String date;
    public String source;

    public List<String> getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(List<String> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}

package com.qjq.crawler.download.dao.mongo;

import java.util.List;

import org.data.process.model.HtmlObject;


public interface AbstractRepository {

    public void insert(HtmlObject htmlObject);

    public void insert(HtmlObject htmlObject, String collectionName);

    public HtmlObject findOne(String uid);
    
    public List<HtmlObject> find(String query);
}

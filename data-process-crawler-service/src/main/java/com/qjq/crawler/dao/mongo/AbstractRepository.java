package com.qjq.crawler.dao.mongo;

import java.util.List;

import com.qjq.crawler.domain.HtmlObject;

public interface AbstractRepository {

    public void insert(HtmlObject htmlObject);

    public void insert(HtmlObject htmlObject, String collectionName);

    public HtmlObject findOne(String uid);
    
    public List<HtmlObject> find(String query);
}

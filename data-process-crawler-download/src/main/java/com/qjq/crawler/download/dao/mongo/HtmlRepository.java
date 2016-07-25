package com.qjq.crawler.download.dao.mongo;

import java.util.List;

import org.data.process.model.HtmlObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;



@Repository
public class HtmlRepository implements AbstractRepository, InitializingBean {

    @Autowired
    MongoTemplate mongoTemplate;

    @Value("${scrapy.html.collection:html}")
    private String collectionName;

    public void insert(HtmlObject htmlObject) {

        mongoTemplate.save(htmlObject, collectionName);
    }

    public void insert(HtmlObject htmlObject, String collectionName) {
        mongoTemplate.save(htmlObject, collectionName);
    }

    public void afterPropertiesSet() throws Exception {

    }

    public HtmlObject findOne(String uid) {
        return mongoTemplate.findOne(new Query(Criteria.where("uid").is(uid)), HtmlObject.class, collectionName);
    }

    public List<HtmlObject> find(String query) {
        return null;
    }

}

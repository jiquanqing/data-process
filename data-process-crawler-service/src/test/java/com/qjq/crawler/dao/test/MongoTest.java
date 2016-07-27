package com.qjq.crawler.dao.test;

import org.data.process.model.HtmlObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.qjq.crawler.dao.mongo.HtmlRepository;

public class MongoTest extends AbstractTest {

    private static Logger logger = LoggerFactory.getLogger(MongoTest.class);

    @Autowired
    HtmlRepository htmlRepository;

    @Test
    public void testConnect() {
        logger.info("测试开始");
        HtmlObject htmlObject = new HtmlObject();
        htmlObject.setContent("测试");
        htmlObject.setDowntime("111");
        htmlObject.setUid("123");
        htmlObject.setUrl("test");
        htmlRepository.insert(htmlObject);
        logger.info("测试结束");
    }
    
    @Test
    public void testQuery(){
        HtmlObject htmlObject = htmlRepository.findOne("5986ff8df3ee50ce");
        System.out.println(htmlObject.getContent());
        System.out.println(htmlObject.getUrl());
    }
}

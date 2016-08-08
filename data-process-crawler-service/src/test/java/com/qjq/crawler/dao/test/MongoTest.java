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
    public void testQuery() {
        HtmlObject htmlObject = htmlRepository.findOne("5986ff8df3ee5ce");
        
        
        if (htmlObject == null) {
            System.out.println("null");
        } else {
            System.out.println(htmlObject.getContent());
            System.out.println(htmlObject.getUrl());
        }
    }
    
    @Test
    public void testDelete(){
        htmlRepository.delete("f4713fda7f98a523");
        htmlRepository.delete("a8bbbb4e6c13aacc");
        htmlRepository.delete("c840fe015a72079");
        htmlRepository.delete("eb2e9482d5491b56");
        htmlRepository.delete("d9b304dbfe607a2b");
        htmlRepository.delete("c86d188cfa746fca");
        htmlRepository.delete("82d48ef1d11cc6c8");
        htmlRepository.delete("cb0fb059cafe66ab");
        htmlRepository.delete("651f90c4b93cbaef");
        htmlRepository.delete("e54c41f382ea6958");
    }
}

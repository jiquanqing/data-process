package com.qjq.crawler.jms.recvie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CrawlerServiceJmsListenerStrategyImpl implements JmsListenerStrategy {

    private static Logger logger = LoggerFactory.getLogger(CrawlerServiceJmsListenerStrategyImpl.class);

    private String jmsQueue = "my-queue";

    @Override
    public String getJmsQueue() {
        return jmsQueue;
    }

    @Override
    public void onMessage(String text) {
        logger.info(jmsQueue + "接收到mq消息:", text);
        System.out.println(text);
    }

    public void setJmsQueue(String jmsQueue) {
        this.jmsQueue = jmsQueue;
    }

}

package com.qjq.crawler.download.jms.send;

public interface MessageSender {
    public void hander(String text);
    
    public void hander(String text,String queueName);
}

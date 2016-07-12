package com.qjq.crawler.jms;

public interface JmsListenerStrategy {

    public String getJmsQueue();

    public void onMessage(String text);

}

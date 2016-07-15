package com.qjq.crawler.jms.recvie;

public interface JmsListenerStrategy {

    public String getJmsQueue();

    public void onMessage(String text);

}

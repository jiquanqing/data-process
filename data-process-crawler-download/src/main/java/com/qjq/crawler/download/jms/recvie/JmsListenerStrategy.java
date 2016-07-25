package com.qjq.crawler.download.jms.recvie;

public interface JmsListenerStrategy {

    public String getJmsQueue();

    public void onMessage(String text);

}

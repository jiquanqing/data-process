package com.qjq.economics.information.jms;

public interface JmsListenerStrategy {

    public String getJmsQueue();

    public void onMessage(String text);

}

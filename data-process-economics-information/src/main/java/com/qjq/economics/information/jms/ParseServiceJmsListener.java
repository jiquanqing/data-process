package com.qjq.economics.information.jms;

public class ParseServiceJmsListener implements JmsListenerStrategy {

    private String jmsQueue = "Parse_Service_Job_Queue";

    @Override
    public String getJmsQueue() {
        return null;
    }

    @Override
    public void onMessage(String text) {

    }

}

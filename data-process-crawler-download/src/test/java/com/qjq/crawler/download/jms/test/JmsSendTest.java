package com.qjq.crawler.download.jms.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.qjq.crawler.download.jms.send.MessageSender;


public class JmsSendTest extends AbstractTest {

    @Autowired
    MessageSender messageSender;

    @Test
    public void testSend() {
        messageSender.hander("test");
    }
}

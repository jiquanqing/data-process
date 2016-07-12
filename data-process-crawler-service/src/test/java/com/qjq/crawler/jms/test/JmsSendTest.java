package com.qjq.crawler.jms.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.qjq.crawler.dao.test.AbstractTest;
import com.qjq.crawler.jms.MessageSender;

public class JmsSendTest extends AbstractTest {

    @Autowired
    MessageSender messageSender;

    @Test
    public void testSend() {
        messageSender.hander("test");
    }
}

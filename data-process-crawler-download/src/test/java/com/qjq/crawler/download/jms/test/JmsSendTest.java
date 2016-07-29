package com.qjq.crawler.download.jms.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.qjq.crawler.download.jms.send.MessageSender;
import com.qjq.crawler.download.utils.SpringContextUtil;


public class JmsSendTest extends AbstractTest {

    MessageSender messageSender;

    @Test
    public void testSend() {
        messageSender = (MessageSender) SpringContextUtil.getBean("messageSenderImpl");
        System.out.println(messageSender.toString());
        messageSender.hander("test");
    }
}
